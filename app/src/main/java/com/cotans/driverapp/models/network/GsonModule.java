package com.cotans.driverapp.models.network;


import com.cotans.driverapp.models.scopes.ApplicationScope;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;

@ApplicationScope
@Module
public class GsonModule {

    @ApplicationScope
    @Provides
    public Gson gson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }
}
