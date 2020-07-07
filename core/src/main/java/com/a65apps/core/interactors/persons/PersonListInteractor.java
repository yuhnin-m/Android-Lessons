package com.a65apps.core.interactors.persons;


import com.a65apps.core.entities.Person;
import java.util.List;
import io.reactivex.rxjava3.core.Single;


public interface PersonListInteractor {
    Single<List<Person>> loadAllPersons(final String query);
}
