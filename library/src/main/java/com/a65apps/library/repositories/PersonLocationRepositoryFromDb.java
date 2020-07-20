package com.a65apps.library.repositories;

import android.content.Context;

import com.a65apps.core.entities.Location;
import com.a65apps.core.interactors.locations.PersonLocationRepository;
import com.a65apps.library.database.AppDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class PersonLocationRepositoryFromDb implements PersonLocationRepository {
    private final AppDatabase database;

    private final Context context;

    public PersonLocationRepositoryFromDb(AppDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    @Override
    public Single<Location> getLocationByPerson(String personId) {
        // TODO: реализовать
        return null;
    }

    @Override
    public Single<List<Location>> getAllPersonLocation() {
        // TODO: реализовать
        return null;
    }

    @Override
    public Single<Location> createPersonLocation(Location location) {
        // TODO: реализовать
        return null;
    }
}
