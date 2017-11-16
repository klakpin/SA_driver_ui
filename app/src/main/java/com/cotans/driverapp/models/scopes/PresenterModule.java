package com.cotans.driverapp.models.scopes;


import com.cotans.driverapp.models.DirectionsJSONParser;
import com.cotans.driverapp.models.InputValidator;
import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.network.EmergencyAlarmService;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.session.SessionManager;
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
import com.google.firebase.iid.FirebaseInstanceId;

import javax.xml.validation.Validator;

import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module(includes = MyApplicationModule.class)
public class PresenterModule {


    @Provides
    @ActivityScope
    EmergencyScreenPresenter emergencyScreenPresenter(EmergencyAlarmService alarmService, SessionManager sessionManager) {
        return new EmergencyScreenPresenter(alarmService, sessionManager);
    }

    @Provides
    @ActivityScope
    AccountInfoScreenPresenter accountInfoScreenPresenter(ServerApi serverApi, SessionManager sessionManager, DatabaseApi databaseApi) {
        return new AccountInfoScreenPresenter(serverApi, sessionManager, databaseApi, MyApplication.getInstance());
    }

    @Provides
    @ActivityScope
    ListOfOrdersScreenPresenter listOfOrdersScreenPresenter(ServerApi serverApi, DatabaseApi databaseApi, SessionManager sessionManager) {
        return new ListOfOrdersScreenPresenter(serverApi, databaseApi, sessionManager);
    }

    @Provides
    @ActivityScope
    MainActivityPresenter mainActivityPresenter(DatabaseApi databaseApi) {
        return new MainActivityPresenter(databaseApi);
    }

    @Provides
    @ActivityScope
    MapScreenPresenter mapScreenPresenter(DirectionsJSONParser jsonPareser, DatabaseApi databaseApi, ServerApi serverApi) {
        return new MapScreenPresenter(jsonPareser, databaseApi, serverApi);
    }

    @Provides
    @ActivityScope
    AppStartScreenPresenter appStartScreenPresenter() {
        return new AppStartScreenPresenter(MyApplication.getInstance());
    }

    @Provides
    @ActivityScope
    LoginScreenPresenter loginScreenPresenter(ServerApi serverApi, SessionManager sessionManager, InputValidator validator, FirebaseInstanceId firebaseInstanceId) {
        return new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
    }

    @Provides
    @ActivityScope
    NewOrderScreenPresenter newOrderScreenPresenter(ServerApi serverApi, DatabaseApi databaseApi, SessionManager sessionManager) {
        return new NewOrderScreenPresenter(serverApi, databaseApi, sessionManager);
    }

    @Provides
    @ActivityScope
    OrderInfoScreenPresenter orderInfoScreenPresenter(DatabaseApi databaseApi, ServerApi serverApi, SessionManager sessionApi) {
        return new OrderInfoScreenPresenter(databaseApi, serverApi, sessionApi);
    }

    @Provides
    @ActivityScope
    OrderCompletionScreenPresenter orderCompletionScreenPresenter(ServerApi serverApi, DatabaseApi databaseApi) {
        return new OrderCompletionScreenPresenter(serverApi, databaseApi);
    }

    @Provides
    @ActivityScope
    ListOfNotificationsPresenter listOfNotificationsPresenter(DatabaseApi databaseApi) {
        return new ListOfNotificationsPresenter(databaseApi);
    }

    @Provides
    @ActivityScope
    DirectionsJSONParser directionsJSONParser() {
        return new DirectionsJSONParser();
    }

    @Provides
    @ActivityScope
    InputValidator inputValidator() {
        return new InputValidator();
    }

    @Provides
    @ActivityScope
    EmergencyAlarmService emergencyAlarmService() {
        return new EmergencyAlarmService(MyApplication.getInstance().serverApi.mParcelServer);
    }

    @Provides
    @ActivityScope
    FirebaseInstanceId firebaseInstanceId() {
        return FirebaseInstanceId.getInstance();
    }

}
