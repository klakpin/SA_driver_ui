package com.cotans.driverapp.models.session;

import android.content.Context;

import com.cotans.driverapp.models.scopes.ApplicationScope;
import com.cotans.driverapp.models.scopes.ContextModule;

import dagger.Module;
import dagger.Provides;

@ApplicationScope
@Module(includes = ContextModule.class)
public class SessionManagerModule {

    @ApplicationScope
    @Provides
    SessionManager sessionManager(Context context) {
        return new SessionManager(context);
    }
}
