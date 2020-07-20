package com.a65apps.library.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.a65apps.library.database.dao.LocationDao;
import com.a65apps.library.models.LocationModel;

import io.reactivex.rxjava3.annotations.NonNull;

@Database(entities = {LocationModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    @NonNull
    public abstract LocationDao userDao();
}
