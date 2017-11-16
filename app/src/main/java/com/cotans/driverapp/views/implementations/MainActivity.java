package com.cotans.driverapp.views.implementations;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.MainActivityPresenter;
import com.cotans.driverapp.views.Interfaces.MainActivityInterface;
import com.cotans.driverapp.views.adapters.ViewPagerAdapter;
import com.google.android.gms.maps.model.CameraPosition;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {


    BottomNavigationView bottomNavigation;

    public CameraPosition savedPosition;

    //This is our viewPager
    @BindView(R.id.main_container)
    public ViewPager viewPager;

    ListOfNotificationsFragment listOfNotificationsFragment;
    ListOfOrdersFragment listOfOrdersFragment;
    MapScreenFragment mapScreenFragment;
    AccountInfoFragment accountInfoFragment;

    MenuItem prevMenuItem;


    AlertDialog alertDialog;
    View dialogView;

    @Inject
    public MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_main_activity);

        ButterKnife.bind(this);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.inflateMenu(R.menu.v2_bottom_nav);


        BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
            switch (item.getItemId()) {
                case R.id.item_notifications:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.item_map:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.item_list_of_parcels:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.item_account_info:
                    viewPager.setCurrentItem(3);
                    break;
            }
            return false;
        };

        bottomNavigation.setOnNavigationItemSelectedListener(listener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        setupViewPager(viewPager);

    }

    /**
     * Initialise presenter, begin it's work
     */
    @Override
    protected void onStart() {
        super.onStart();

        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);
        presenter.bindView(this);
        presenter.init();
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d("MainActivity", "OnNewIntent");
        switch (viewPager.getCurrentItem()) {
            case 0:
                listOfNotificationsFragment.onResume();
                break;
            case 1:
                mapScreenFragment.onResume();
                break;
            case 2:
                listOfOrdersFragment.onResume();
                break;
            case 3:
                accountInfoFragment.onResume();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopNotificationReciving();
    }


    @Override
    public void alertNewMessage(String message) {
        runOnUiThread(() -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            dialogView = inflater.inflate(R.layout.alert_message, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
            TextView messageText = alertDialog.findViewById(R.id.am_message);
            messageText.setText(message);
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        listOfNotificationsFragment = new ListOfNotificationsFragment();
        mapScreenFragment = new MapScreenFragment();
        listOfOrdersFragment = new ListOfOrdersFragment();
        accountInfoFragment = new AccountInfoFragment();
        adapter.addFragment(listOfNotificationsFragment);
        adapter.addFragment(mapScreenFragment);
        adapter.addFragment(listOfOrdersFragment);
        adapter.addFragment(accountInfoFragment);
        viewPager.setAdapter(adapter);
    }

}
