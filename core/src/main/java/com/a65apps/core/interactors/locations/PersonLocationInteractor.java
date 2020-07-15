package com.a65apps.core.interactors.locations;

import com.a65apps.core.entities.Location;
import com.a65apps.core.entities.Person;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface PersonLocationInteractor {

    Single<Location> loadLocationByPerson(String personId);

    Single<List<Location>> loadAllPersonLocations();

    Single<Location> createPersonLocation(Location location);

}
