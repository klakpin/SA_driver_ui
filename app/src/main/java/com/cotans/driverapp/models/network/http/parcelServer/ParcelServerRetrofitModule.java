package com.cotans.driverapp.models.network.http.parcelServer;


import com.cotans.driverapp.models.GlobalConstants;
import com.cotans.driverapp.models.scopes.ApplicationScope;
import com.cotans.driverapp.models.network.GsonModule;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@ApplicationScope
@Module(includes = GsonModule.class)
public class ParcelServerRetrofitModule {

    @ApplicationScope
    @Provides
    @Named("ParcelServerRetrofit")
    public Retrofit retrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(GlobalConstants.PARCEL_SERVER_URI)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
}
