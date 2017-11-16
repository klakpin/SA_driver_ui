package com.cotans.driverapp.presenters;

import com.cotans.driverapp.models.network.EmergencyAlarmService;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.views.Interfaces.EmergencyScreenInterface;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.observers.DisposableObserver;

public class EmergencyScreenPresenter {

    EmergencyScreenInterface view;

    EmergencyAlarmService alarmService;
    SessionManager sessionManager;
    DisposableObserver<String> resultObserver;

    Completable cancelRequest = Completable.create(CompletableEmitter::onComplete).delay(1500, TimeUnit.MILLISECONDS);


    public EmergencyScreenPresenter(EmergencyAlarmService alarmService, SessionManager sessionManager) {
        this.alarmService = alarmService;
        this.sessionManager = sessionManager;
    }

    public void bindView(EmergencyScreenInterface view) {
        this.view = view;
    }

    public void init() {
        resultObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                if (s.equals("ok")) {
                    onRequestSuccess();
                } else {
                    onRequestError(s);
                }
            }

            @Override
            public void onError(Throwable e) {
                onRequestError(e.toString());
            }

            @Override
            public void onComplete() {
            }
        };

        view.setCancelButtonListener(v -> cancelAlarm());
        alarmService.getAlarmObservable(sessionManager.getToken())
                .subscribe(resultObserver);
    }

    public void onRequestSuccess() {
        resultObserver.dispose();
        view.setEsText("Success! Operator will connect with you as soon as possible.");
        cancelRequest.subscribe(() -> view.cancelAlert());
    }

    public void onRequestError(String s) {
        view.setEsText("Problems with request, " + s + " . Resending...");
    }

    public void cancelAlarm() {
        resultObserver.dispose();
        view.cancelAlert();
    }
}
