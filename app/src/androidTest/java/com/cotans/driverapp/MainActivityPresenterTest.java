package com.cotans.driverapp;


import android.support.test.espresso.core.deps.guava.eventbus.EventBus;
import android.support.test.runner.AndroidJUnit4;

import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.eventbus.MessageEvent;
import com.cotans.driverapp.presenters.MainActivityPresenter;
import com.cotans.driverapp.views.implementations.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private DatabaseApi databaseApi;
    @Mock
    private MainActivity view;


    @Test
    public void mock() {
        new MainActivityPresenter(databaseApi);
    }
    @Test
    public void viewShouldBeBinded() {
        MainActivityPresenter presenter = new MainActivityPresenter(databaseApi);
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
    public void whenEventSentShouldShowAlert() {
        MainActivityPresenter presenter = new MainActivityPresenter(databaseApi);
        presenter.bindView(view);
        presenter.init();
        String message = "msg";
        org.greenrobot.eventbus.EventBus.getDefault().post(new MessageEvent(message));
        verify(view, times(1)).alertNewMessage(message);
    }


    @Test
    public void whenEventBusInactiveNoAlerts() {
        MainActivityPresenter presenter = new MainActivityPresenter(databaseApi);
        presenter.bindView(view);
        presenter.init();

        presenter.stopNotificationReciving();

        String message = "msg";
        org.greenrobot.eventbus.EventBus.getDefault().post(new MessageEvent(message));
        verify(view, never()).alertNewMessage(any());
    }


}
