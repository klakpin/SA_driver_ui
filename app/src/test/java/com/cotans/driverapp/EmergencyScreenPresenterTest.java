package com.cotans.driverapp;


import com.cotans.driverapp.models.network.EmergencyAlarmService;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.presenters.EmergencyScreenPresenter;
import com.cotans.driverapp.views.Interfaces.EmergencyScreenInterface;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmergencyScreenPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    EmergencyScreenInterface view;
    @Mock
    EmergencyAlarmService alarmService;
    @Mock
    SessionManager sessionManager;

    @Test
    public void createMock() throws Exception {
        new EmergencyScreenPresenter(alarmService, sessionManager);
    }

    @Test
    public void viewShouldBeBinded() {
        EmergencyScreenPresenter presenter = new EmergencyScreenPresenter(alarmService, sessionManager);

        presenter.bindView(view);

        Class<?> clazz = presenter.getClass();
        Field field = null;

        try {
            field = clazz.getDeclaredField("view");
        } catch (NoSuchFieldException e) {
            assertThat(false, is(true));
            e.printStackTrace();
        }
        assert field.equals(view);
    }


    @Test
    public void checkStartStopAlarm() {
        EmergencyScreenPresenter presenter = new EmergencyScreenPresenter(alarmService, sessionManager);

        when(sessionManager.getToken()).thenReturn("tok");

        final boolean[] obsCalled = {false};

        Observable<String> observable = Observable.create(e -> obsCalled[0] = true);

        when(alarmService.getAlarmObservable("tok")).thenReturn(observable);
        presenter.bindView(view);
        presenter.init();

        assertThat(obsCalled[0], is(true));
        verify(view, times(1)).setCancelButtonListener(any());

        presenter.cancelAlarm();

        try {
            Class<?> clazz = presenter.getClass();
            Field field = clazz.getDeclaredField("resultObserver");
            field.setAccessible(true);
            DisposableObserver<String> obs = (DisposableObserver<String>) field.get(presenter);

            assertThat(obs.isDisposed(), is(true));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            assertThat(false, is(true));
            e.printStackTrace();
        }

        verify(view, times(1)).cancelAlert();
    }

    @Test
    public void testSuccessRequestBehaviour() {
        EmergencyScreenPresenter presenter = new EmergencyScreenPresenter(alarmService, sessionManager);
        presenter.bindView(view);

        try {
            Class<?> clazz = presenter.getClass();
            Field resultObserver = clazz.getDeclaredField("resultObserver");
            resultObserver.setAccessible(true);
            resultObserver.set(presenter, new DisposableObserver() {
                @Override
                public void onNext(Object o) {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
            DisposableObserver<String> obs = (DisposableObserver<String>) resultObserver.get(presenter);

            Field cancelRequestField = clazz.getDeclaredField("cancelRequest");
            cancelRequestField.setAccessible(true);
            final boolean[] cancelRequestCalled = {false};
            cancelRequestField.set(presenter, Completable.create(e -> cancelRequestCalled[0] = true));
            presenter.onRequestSuccess();

            assertThat(cancelRequestCalled[0], is(true));
            assertThat(obs.isDisposed(), is(true));
            verify(view, times(1)).setEsText(any());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            assertThat(false, is(true));
            e.printStackTrace();
        }
    }

    @Test
    public void testFailedRequestBehaviour() {
        EmergencyScreenPresenter presenter = new EmergencyScreenPresenter(alarmService, sessionManager);
        presenter.bindView(view);
        presenter.onRequestError("Error");
        verify(view, times(1)).setEsText(any());
    }
}
