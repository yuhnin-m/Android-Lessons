package com.a65apps.core.interactors.contacts;

import com.a65apps.core.entities.Contact;
import com.a65apps.core.entities.Person;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public class ContactListModel implements ContactListInteractor {
    private final ContactListRepository repository;

    public ContactListModel(ContactListRepository repository) {
        this.repository = repository;
    }


    @Override
    public Single<List<Contact>> loadContactsByPerson(@NonNull final String personId) {
        return this.repository.getContactsByPerson(personId);
    }

    @Override
    public Single<Person> loadPersonDetails(@NonNull String personId) {
        return this.repository.getPersonDetails(personId);
    }
}
