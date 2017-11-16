package com.cotans.driverapp;


import com.cotans.driverapp.models.InputValidator;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.presenters.NewOrderScreenPresenter;
import com.cotans.driverapp.views.Interfaces.LoginActivityInterface;
import com.cotans.driverapp.views.Interfaces.NewOrderScreenInterface;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;



import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
public class NewOrderScreenPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private NewOrderScreenInterface view;
    @Mock
    private ServerApi serverApi;
    @Mock
    private SessionManager sessionManager;
    @Mock
    private DatabaseApi databaseApi;

    @Test
    public void mock() {
        new NewOrderScreenPresenter(serverApi, databaseApi, sessionManager);
    }

    @Test
    public void viewShouldBeBinned() {
        NewOrderScreenPresenter presenter = new NewOrderScreenPresenter(serverApi, databaseApi, sessionManager);

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

}
