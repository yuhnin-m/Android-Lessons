package com.a65apps.core.interactors.locations;

import com.a65apps.core.entities.Location;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class PersonLocationModel implements PersonLocationInteractor {

    private final PersonLocationRepository personLocationRepository;

    public PersonLocationModel(PersonLocationRepository personLocationRepository) {
        this.personLocationRepository = personLocationRepository;
    }


    @Override
    public Single<Location> loadLocationByPerson(String personId) {
        return personLocationRepository.getLocationByPerson(personId);
    }

    @Override
    public Single<List<Location>> loadAllPersonLocations() {
        return personLocationRepository.getAllPersonLocation();
    }

    @Override
    public Single<Location> createPersonLocation(Location location) {
        return personLocationRepository.createPersonLocation(location);
    }
}
