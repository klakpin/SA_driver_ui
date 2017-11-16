package com.cotans.driverapp.views.implementations;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.db.Notification;
import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.ListOfNotificationsPresenter;
import com.cotans.driverapp.views.Interfaces.ListOfNotificationsInterface;
import com.cotans.driverapp.views.adapters.NotificationsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfNotificationsFragment extends Fragment implements ListOfNotificationsInterface{


    @BindView(R.id.lon_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.lon_rv)
    RecyclerView recyclerView;

    @Inject
    ListOfNotificationsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_of_notifications, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);
        presenter.bindView(this);

        mSwipeRefreshLayout.setOnRefreshListener(presenter);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        presenter.init();
    }

    @Override
    public void fill(List<Notification> notifications) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        NotificationsAdapter adapter = new NotificationsAdapter(notifications);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void stopRefreshDelayed(int i) {
        new Handler().postDelayed(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
        }, i);

    }
}
