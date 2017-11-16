package com.cotans.driverapp.models.scopes;


import com.cotans.driverapp.presenters.AccountInfoScreenPresenter;
import com.cotans.driverapp.presenters.AppStartScreenPresenter;
import com.cotans.driverapp.presenters.EmergencyScreenPresenter;
import com.cotans.driverapp.presenters.ListOfNotificationsPresenter;
import com.cotans.driverapp.presenters.ListOfOrdersScreenPresenter;
import com.cotans.driverapp.presenters.LoginScreenPresenter;
import com.cotans.driverapp.presenters.MainActivityPresenter;
import com.cotans.driverapp.presenters.MapScreenPresenter;
import com.cotans.driverapp.presenters.NewOrderScreenPresenter;
import com.cotans.driverapp.presenters.OrderCompletionScreenPresenter;
import com.cotans.driverapp.presenters.OrderInfoScreenPresenter;
import com.cotans.driverapp.views.implementations.AppStartActivity;
import com.cotans.driverapp.views.implementations.ListOfNotificationsFragment;
import com.cotans.driverapp.views.implementations.LoginActivity;
import com.cotans.driverapp.views.implementations.MainActivity;
import com.cotans.driverapp.views.implementations.NewOrderScreenActivity;
import com.cotans.driverapp.views.implementations.OrderInfoActivity;
import com.cotans.driverapp.views.implementations.AccountInfoFragment;
import com.cotans.driverapp.views.implementations.ListOfOrdersFragment;
import com.cotans.driverapp.views.implementations.MapScreenFragment;

import dagger.Component;

@ActivityScope
@Component(modules = PresenterModule.class)
public interface ActivityComponent {

    void inject(AccountInfoFragment accountInfoFragment);
    void inject(ListOfOrdersFragment listOfOrdersFragment);
    void inject(MainActivity mainActivity);
    void inject(MapScreenFragment mapScreenFragment);
    void inject(AppStartActivity appStartActivity);
    void inject(LoginActivity loginActivity);
    void inject(NewOrderScreenActivity newOrderScreenActivity);
    void inject(OrderInfoActivity orderInfoActivity);
    void inject(ListOfNotificationsFragment listOfNotificationsFragment);

    AccountInfoScreenPresenter accountInfoScreenPresenter();
    ListOfOrdersScreenPresenter listOfOrdersScreenPresenter();
    MainActivityPresenter mainActivityPresenter();
    MapScreenPresenter mapScreenPresenter();
    AppStartScreenPresenter appStartScreenPresenter();
    LoginScreenPresenter loginScreenPresenter();
    NewOrderScreenPresenter newOrderScreenPresenter();
    OrderInfoScreenPresenter orderInfoScreenPresenter();
    OrderCompletionScreenPresenter orderCompletionScreenPresenter();
    EmergencyScreenPresenter emergencyScreenPresenter();
    ListOfNotificationsPresenter listOfNotificationsPresenter();
}
