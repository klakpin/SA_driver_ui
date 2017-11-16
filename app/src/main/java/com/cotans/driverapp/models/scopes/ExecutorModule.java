package com.cotans.driverapp.models.scopes;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;

@ApplicationScope
@Module
public class ExecutorModule {

    @ApplicationScope
    @Provides
    public Executor executor() {
        return Executors.newFixedThreadPool(1);

    }
}
