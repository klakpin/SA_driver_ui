package com.cotans.driverapp.models.network.http;

import com.cotans.driverapp.models.scopes.ExecutorModule;
import com.cotans.driverapp.models.scopes.ApplicationScope;
import com.cotans.driverapp.models.network.http.notificationServer.NotificationServer;
import com.cotans.driverapp.models.network.http.notificationServer.NotificationServerRetrofitModule;
import com.cotans.driverapp.models.network.http.parcelServer.ParcelServer;
import com.cotans.driverapp.models.network.http.parcelServer.ParcelServerRetrofitModule;
import com.cotans.driverapp.models.network.http.routeServer.RouteServer;
import com.cotans.driverapp.models.network.http.routeServer.RouteServerRetrofitModule;

import java.util.concurrent.Executor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@ApplicationScope
@Module(includes = {ExecutorModule.class, RouteServerRetrofitModule.class,
        ParcelServerRetrofitModule.class, NotificationServerRetrofitModule.class})
public class HttpModule {

    @ApplicationScope
    @Provides
    public ServerApi serverApi(RouteServer routeServer, NotificationServer notificationServer, ParcelServer parcelServer, Executor executor) {
        return new ServerApi(routeServer, notificationServer, parcelServer, executor);
    }

    @ApplicationScope
    @Provides
    public RouteServer routeServer(@Named("RouteServerRetrofit") Retrofit retrofit) {
        return retrofit.create(RouteServer.class);
    }

    @ApplicationScope
    @Provides
    public NotificationServer notificationServer(@Named("NotificationServerRetrofit") Retrofit retrofit) {
        return retrofit.create(NotificationServer.class);
    }

    @ApplicationScope
    @Provides
    public ParcelServer parcelServer(@Named("ParcelServerRetrofit") Retrofit retrofit) {
        return retrofit.create(ParcelServer.class);
    }
}
