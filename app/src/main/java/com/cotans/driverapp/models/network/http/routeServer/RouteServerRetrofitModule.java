package com.cotans.driverapp.models.network.http.routeServer;

import com.cotans.driverapp.models.GlobalConstants;
import com.cotans.driverapp.models.scopes.ApplicationScope;
import com.cotans.driverapp.models.network.GsonModule;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@ApplicationScope
@Module(includes = GsonModule.class)

public class RouteServerRetrofitModule {
    @ApplicationScope
    @Provides
    @Named("RouteServerRetrofit")
    public Retrofit retrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(GlobalConstants.ROUTE_SERVER_URI_FOR_RETROFIT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
}

