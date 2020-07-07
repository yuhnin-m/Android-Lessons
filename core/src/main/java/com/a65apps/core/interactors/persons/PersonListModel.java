package com.a65apps.core.interactors.persons;

import com.a65apps.core.entities.Person;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class PersonListModel implements PersonListInteractor {
    private final PersonListRepository repository;

    public PersonListModel(PersonListRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<List<Person>> loadAllPersons(final String searchString) {
        return this.repository.getAllPersons(searchString);
    }
}
