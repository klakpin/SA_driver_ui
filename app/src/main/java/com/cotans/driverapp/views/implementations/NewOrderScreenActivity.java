package com.cotans.driverapp.views.implementations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.NewOrderScreenPresenter;
import com.cotans.driverapp.views.adapters.DeliveryItemsAdapter;
import com.cotans.driverapp.views.Interfaces.NewOrderScreenInterface;
import com.cotans.driverapp.views.items.OrderProperties;
import com.cotans.driverapp.views.items.SimpleDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewOrderScreenActivity extends AppCompatActivity implements NewOrderScreenInterface {

    @Inject
    public NewOrderScreenPresenter newOrderScreenPresenterPresenter;

    @BindView(R.id.not_progressbar)
    ProgressBar pBar;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);
        newOrderScreenPresenterPresenter.bindView(this);
        newOrderScreenPresenterPresenter.init();
    }

    @Override
    public void startProgressBar(){
        pBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar(){
        pBar.setVisibility(View.INVISIBLE);
        pBar.setVisibility(View.INVISIBLE);
    }

    @Override
    @OnClick(R.id.notification_accept_button)
    public void onAcceptButtonPress() {
        newOrderScreenPresenterPresenter.onAcceptButtonPressed();

    }

    @Override
    public void fill(List<OrderProperties> properties) {
        final Context currContext = this;
        final List<OrderProperties> props = properties;
        runOnUiThread(new Runnable() {
            public void run() {
                RecyclerView rv = findViewById(R.id.notification_rv);
                LinearLayoutManager llm = new LinearLayoutManager(currContext);
                rv.setLayoutManager(llm);
                rv.addItemDecoration(new SimpleDividerItemDecoration(currContext));
                DeliveryItemsAdapter adapter = new DeliveryItemsAdapter(currContext, props);
                rv.setAdapter(adapter);
            }
        });

    }

    @Override
    public Bundle getExtras() {
        return getIntent().getExtras();
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void stop() {
        finish();
    }

    @Override
    public void onOrderConfirm() {
        Toast.makeText(this, "Succesfully confirmed!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUnsuccessfulOrderConfirm(String status) {
        Toast.makeText(this, "Problems with confirmation, try again or call your supervisor", Toast.LENGTH_LONG).show();
    }
}

