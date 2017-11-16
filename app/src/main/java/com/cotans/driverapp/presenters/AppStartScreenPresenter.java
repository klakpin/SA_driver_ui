package com.cotans.driverapp.presenters;

import android.content.Intent;

import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.scopes.ContextModule;
import com.cotans.driverapp.models.scopes.DaggerMyApplicationComponent;
import com.cotans.driverapp.models.scopes.MyApplicationComponent;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.views.Interfaces.AppStartActivityInterface;
import com.cotans.driverapp.views.implementations.LoginActivity;
import com.cotans.driverapp.views.implementations.MainActivity;

public class AppStartScreenPresenter {

    private AppStartActivityInterface view;

    private MyApplication myApplication;

    public AppStartScreenPresenter(MyApplication application) {
        myApplication = application;
    }

    public void bindView(AppStartActivityInterface newView) {
        view = newView;
    }

    public void init() {
        startInitialization();
    }

    public void startInitialization() {
        view.startLoading();

        MyApplicationComponent component = DaggerMyApplicationComponent
                .builder()
                .contextModule(new ContextModule(view.getContext().getApplicationContext()))
                .build();

        component.inject(myApplication);

        view.onSuccess();

        startApplication();
    }

    private void startApplication() {
        SessionManager session = MyApplication.getInstance().sessionManager;
        if (session.isAuthorised()) {
            startMainActivity();
        } else {
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(intent);
        view.stop();
    }

    private void startMainActivity() {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(intent);
        view.stop();
    }

}
