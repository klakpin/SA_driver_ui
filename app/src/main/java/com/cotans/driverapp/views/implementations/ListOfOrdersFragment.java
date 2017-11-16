package com.cotans.driverapp.views.implementations;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.db.Order;
import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.ListOfOrdersScreenPresenter;
import com.cotans.driverapp.presenters.OrdersAdapter;
import com.cotans.driverapp.views.Interfaces.ListOfOrdersInterface;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ListOfOrdersFragment extends Fragment implements ListOfOrdersInterface, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    public ListOfOrdersScreenPresenter presenter;

    RecyclerView rv;
    OrdersAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_of_deliveries, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        rv = getActivity().findViewById(R.id.rv);

        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);
        presenter.bindView(this);

        mSwipeRefreshLayout = getActivity().findViewById(R.id.lod_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        presenter.init();
    }

    @Override
    public void onRefresh() {
        //Remove animation
        new Handler().postDelayed(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            presenter.fillInOrders();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }, 1);
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.fillInOrders();
    }

    public void fill(final List<Order> orders) {
        final Context currContext = getActivity();
        final List<Order> ords = orders;
        getActivity().runOnUiThread(() -> {
            Log.d("ListOfOrders", "Putting orders " + orders.toString());
            LinearLayoutManager llm = new LinearLayoutManager(currContext);
            rv.setLayoutManager(llm);
            if (adapter == null) {
                adapter = new OrdersAdapter(currContext, ords);
                rv.setAdapter(adapter);
            } else {
                rv.swapAdapter(new OrdersAdapter(currContext, ords), true);
            }
        });
    }

    @Override
    public void onErrorWhileDownloadingOrders(String s) {
        Toast.makeText(getActivity(), "Error while downloading orders. " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setNewOrders(List<Order> orders) {
        //TODO:this method should fill in orders from List to new order section
    }

    @Override
    public void setCurrentOrders(List<Order> orders) {
        //TODO:this method should fill in orders from List to current order section
    }

    @Override
    public void clearList() {
        if (adapter != null) {
            adapter.emptyList();
        }
    }

    @Override
    public void notifyDataChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}

