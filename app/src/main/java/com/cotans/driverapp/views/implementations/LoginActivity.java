package com.cotans.driverapp.views.implementations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.LoginScreenPresenter;
import com.cotans.driverapp.views.Interfaces.LoginActivityInterface;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginActivityInterface {


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @BindView(R.id.loginEmail)
    EditText email;
    @BindView(R.id.loginPassword)
    EditText password;

    @BindView(R.id.arrowButton)
    Button loginButton;

    @BindView(R.id.al_progressbar)
    ProgressBar pBar;

    @Inject
    public LoginScreenPresenter loginScreenPresenterPresenter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);
        loginScreenPresenterPresenter.bindView(this);
        loginScreenPresenterPresenter.init();

    }

    @Override
    public void askPermissions() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void startProgressBar() {
        pBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar() {
        pBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onInvalidEmail() {
        Toast.makeText(this, "Please, enter valid email", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onInvalidPassword() {
        Toast.makeText(this, "Please, enter valid password ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWrongEmailOrPassword() {
        Toast.makeText(this, "Entered email or password is wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getEmail() {
        return email.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }


    @Override
    public void onUnregistered() {
        Toast.makeText(this, "Entered email is not registered in our system", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLoginFailure(String s) {
        Toast.makeText(this, "Login failed" + s, Toast.LENGTH_SHORT).show();
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
    public void setLoginButtonListener(View.OnClickListener listener) {
        loginButton.setOnClickListener(listener);
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
