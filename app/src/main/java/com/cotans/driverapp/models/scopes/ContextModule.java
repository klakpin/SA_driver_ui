package com.cotans.driverapp.models.scopes;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@ApplicationScope
@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @ApplicationScope
    @Provides
    public Context context() {
        return context;
    }
}
