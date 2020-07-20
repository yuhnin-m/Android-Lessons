package com.a65apps.application.app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.a65apps.library.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {
    @Provides
    @Singleton
    @NonNull
    public AppDatabase provideDataBase(@NonNull Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "person_locations_db")
                .build();
    }
}
