package com.cotans.driverapp.views.implementations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.AppStartScreenPresenter;
import com.cotans.driverapp.views.Interfaces.AppStartActivityInterface;

import javax.inject.Inject;


public class AppStartActivity extends AppCompatActivity implements AppStartActivityInterface {

    @Inject
    public AppStartScreenPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);
        presenter.bindView(this);
        presenter.init();

    }

    @Override
    public void startLoading() {

    }


    @Override
    public void onSuccess() {

    }

    @Override
    public void stop() {
        finish();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
