package com.cotans.driverapp.presenters;

import com.cotans.driverapp.models.InputValidator;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.SignInResponse;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.views.Interfaces.LoginActivityInterface;
import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;


public class LoginScreenPresenter {

    private ServerApi api;
    private SessionManager session;
    private InputValidator validator;
    private FirebaseInstanceId firebaseInstanceId;

    private LoginActivityInterface view;

    public LoginScreenPresenter(ServerApi serverApi, SessionManager sessionManager, InputValidator validator, FirebaseInstanceId firebaseInstanceId) {
        session = sessionManager;
        this.validator = validator;
        this.api = serverApi;
        this.firebaseInstanceId = firebaseInstanceId;
    }

    public void bindView(LoginActivityInterface loginView) {
        this.view = loginView;
    }

    public void init() {
        view.askPermissions();

        view.setLoginButtonListener(view -> onLoginButtonPressed());
    }

    public void onLoginButtonPressed() {
        view.startProgressBar();

        if (!validator.isEmailValid(view.getEmail())) {
            view.stopProgressBar();
            view.onInvalidEmail();
            return;
        }

        if (!validator.isPasswordValid(view.getPassword())) {
            view.stopProgressBar();
            view.onInvalidPassword();
            return;
        }

        Single<SignInResponse> responseSingle = api.getSendLoginRequestSingle(view.getEmail(), view.getPassword());
        responseSingle.subscribe(new DisposableSingleObserver<SignInResponse>() {
            @Override
            public void onSuccess(SignInResponse signInResponse) {
                onSuccessfulLogin(signInResponse.getResult().getToken());
                dispose();
            }

            @Override
            public void onError(Throwable e) {
                onUnsuccessfulLogin(e.getMessage());
                dispose();
            }
        });
    }

    public void onUnsuccessfulLogin(String status) {
        view.stopProgressBar();
        switch (status) {
            case "no_email":
                view.onUnregistered();
                break;
            case "wrong_pswd":
                view.onWrongEmailOrPassword();
                break;
            default:
                view.onLoginFailure(status);
                break;
        }
    }

    public void onSuccessfulLogin(String token) {
        view.stopProgressBar();

        session.setAuthorised();
        session.setEmail(view.getEmail());
        session.setToken(token);

        api.sendMessagingToken(view.getEmail(), firebaseInstanceId.getToken())
                .subscribe(responseBodyResponse -> {
                    //TODO handle possible errors from serevr
                });
        view.openMainActivity();
        view.stop();
    }
}
