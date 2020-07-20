package com.a65apps.core.interactors.locations;

import com.a65apps.core.entities.Location;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface PersonLocationRepository {

    Single<Location> getLocationByPerson(String personId);

    Single<List<Location>> getAllPersonLocation();

    Single <Location> createPersonLocation(Location location);

}
