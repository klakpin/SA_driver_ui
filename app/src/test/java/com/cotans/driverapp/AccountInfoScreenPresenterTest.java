package com.cotans.driverapp;


import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.gps.GpsTracker;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.LogOutResponce;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.presenters.AccountInfoScreenPresenter;
import com.cotans.driverapp.views.Interfaces.AccountInfoInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountInfoScreenPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AccountInfoInterface view;
    @Mock
    ServerApi serverApi;
    @Mock
    SessionManager sessionManager;
    @Mock
    DatabaseApi databaseApi;
    @Mock
    MyApplication myApplication;

    @Test
    public void createMock() throws Exception {
        new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, myApplication);
    }

    @Test
    public void viewShouldBeBinded() {
        AccountInfoScreenPresenter presenter = new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, myApplication);

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
    public void ifGpsOfflineThenSwitchIsOffline() throws Exception {
        AccountInfoScreenPresenter presenter = new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, myApplication);

        when(myApplication.getTracker()).thenReturn(null);

        presenter.bindView(view);
        presenter.init();

        // Check that gps status in view is false
        verify(view, times(1)).setGpsSenderStatus(false);
        verify(view, never()).setGpsSenderStatus(true);
    }

    @Test
    public void ifGpsOnlineThenSwitchIsOnline() throws Exception {
        AccountInfoScreenPresenter presenter = new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, myApplication);
        when(myApplication.getTracker()).thenReturn(mock(GpsTracker.class));

        presenter.bindView(view);
        presenter.init();

        // Check that gps status in view is false
        verify(view, times(1)).setGpsSenderStatus(true);
        verify(view, never()).setGpsSenderStatus(false);
    }

    @Test
    public void listenersShouldBeDefinedAfterInit() {
        AccountInfoScreenPresenter presenter = new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, myApplication);
        presenter.bindView(view);
        presenter.init();

        verify(view, times(1)).setGpsSwitchChangeListener(any());
        verify(view, times(1)).setLogoutButtonListener(any());
    }

    @Test
    public void graphicsShouldBeFilled() {
        AccountInfoScreenPresenter presenter = new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, myApplication);
        presenter.bindView(view);
        presenter.init();

        verify(view, times(1)).setFuelConsumptionData(any());
        verify(view, times(1)).setKmData(any());
    }

    @Test
    public void logoutButtonShouldClearDbAndPrefsCallObservableAndStartLoginActivityOnSuccessfulLogout() {
        AccountInfoScreenPresenter presenter = new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, myApplication);

        final int[] observerCalls = {0};
        when(sessionManager.getToken()).thenReturn("token");
        when(serverApi.getLogoutSingle("token")).thenReturn(Single.create(e -> {
            observerCalls[0]++;
            e.onSuccess(mock(LogOutResponce.class));
        }));

        presenter.bindView(view);
        presenter.init();

        presenter.onLogoutButton();
        testLogout(observerCalls);
    }

    @Test
    public void logoutButtonShouldClearDbAndPrefsCallObservableAndStartLoginActivityOnErrorWhileLogout() {
        AccountInfoScreenPresenter presenter = new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, myApplication);

        final int[] observerCalls = {0};
        when(sessionManager.getToken()).thenReturn("token");
        when(serverApi.getLogoutSingle("token")).thenReturn(Single.create(e -> {
            observerCalls[0]++;
            e.onError(mock(Throwable.class));
        }));

        presenter.bindView(view);
        presenter.init();

        presenter.onLogoutButton();
        testLogout(observerCalls);
    }

    public void testLogout(final int[] observerCalls) {
        verify(sessionManager, times(1)).logout();
        verify(databaseApi, times(1)).clearOrdersTable();
        verify(serverApi, times(1)).getLogoutSingle("token");
        verify(view, times(1)).startLoginActivity();
        assertThat(observerCalls[0], is(1));
    }
}
