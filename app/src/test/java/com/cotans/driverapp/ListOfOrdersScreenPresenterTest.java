package com.cotans.driverapp;


import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.GetDriverDeliveriesResponse;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.presenters.ListOfOrdersScreenPresenter;
import com.cotans.driverapp.views.Interfaces.ListOfOrdersInterface;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;
import java.util.ArrayList;

import io.reactivex.Single;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ListOfOrdersScreenPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ListOfOrdersInterface view;
    @Mock
    ServerApi serverApi;
    @Mock
    DatabaseApi databaseApi;
    @Mock
    SessionManager sessionManager;

    @Test
    public void createMock() {
        new ListOfOrdersScreenPresenter(serverApi, databaseApi, sessionManager);
    }

    @Test
    public void viewShouldBeBinded() {
        ListOfOrdersScreenPresenter presenter = new ListOfOrdersScreenPresenter(serverApi, databaseApi, sessionManager);

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
    public void shouldUpdateOrdersWhenResponseIsGoodListOnInit() {
        ListOfOrdersScreenPresenter presenter = new ListOfOrdersScreenPresenter(serverApi, databaseApi, sessionManager);
        presenter.bindView(view);

        final boolean[] wasCalled = {false};
        //This single returns orders
        Single<GetDriverDeliveriesResponse> single = Single.create(e -> {
            e.onSuccess(new GetDriverDeliveriesResponse("ok", new ArrayList<>()));
            wasCalled[0] = true;
        });
        when(serverApi.getDriverDeliveriesSingle(any())).thenReturn(single);

        presenter.init();

        verify(databaseApi, times(1)).insertOnlyNewOrders(any());
        verify(view, times(1)).fill(any());
        verify(view, never()).onErrorWhileDownloadingOrders(any());
        assertThat(wasCalled[0], is(true));
    }

    @Test
    public void shouldSendErrorWhenResponseIsBad() {
        ListOfOrdersScreenPresenter presenter = new ListOfOrdersScreenPresenter(serverApi, databaseApi, sessionManager);
        presenter.bindView(view);

        final boolean[] wasCalled = {false};

        //This single returns orders
        Single<GetDriverDeliveriesResponse> single = Single.create(e -> {
            wasCalled[0] = true;
            e.onError(new Throwable("error"));
        });

        when(serverApi.getDriverDeliveriesSingle(any())).thenReturn(single);

        presenter.init();

        verify(databaseApi, never()).insertOnlyNewOrders(any());
        verify(view, times(1)).onErrorWhileDownloadingOrders(any());
        verify(view, never()).fill(any());
        assertThat(wasCalled[0], is(true));
    }
}
