package com.cotans.driverapp;


import android.content.Context;

import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.scopes.DaggerMyApplicationComponent;
import com.cotans.driverapp.presenters.AppStartScreenPresenter;
import com.cotans.driverapp.views.Interfaces.AppStartActivityInterface;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

//TODO complete
public class AppStartScreenPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AppStartActivityInterface view;

    @Mock
    MyApplication myApplication;

    @Test
    public void createMock() throws Exception {
        new AppStartScreenPresenter(myApplication);
    }

    @Test
    public void constructorShouldAssignApplicationClass() {
        AppStartScreenPresenter presenter = new AppStartScreenPresenter(myApplication);
        Class<?> clazz = presenter.getClass();
        Field field = null;
        try {
            field = clazz.getDeclaredField("myApplication");
        } catch (NoSuchFieldException e) {
            assertThat(false, is(true));
            e.printStackTrace();
        }
        assert field.equals(myApplication);
    }

    @Test
    public void viewShouldBeBinded() {
        AppStartScreenPresenter presenter = new AppStartScreenPresenter(myApplication);
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
