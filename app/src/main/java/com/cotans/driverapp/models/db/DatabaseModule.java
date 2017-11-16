package com.cotans.driverapp.models.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.cotans.driverapp.models.scopes.ApplicationScope;
import com.cotans.driverapp.models.scopes.ContextModule;
import com.cotans.driverapp.models.scopes.ExecutorModule;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;

@ApplicationScope
@Module(includes = {ExecutorModule.class, ContextModule.class})
public class DatabaseModule {


    @ApplicationScope
    @Provides
    DatabaseApi databaseApi(AppDatabase database, Executor executor) {
        return new DatabaseApi(database, executor);
    }

    @ApplicationScope
    @Provides
    AppDatabase appDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, "database-name")
                .build();
    }
}
