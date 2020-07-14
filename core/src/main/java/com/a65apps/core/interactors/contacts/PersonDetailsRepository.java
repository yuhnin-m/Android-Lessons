package com.a65apps.core.interactors.contacts;

import com.a65apps.core.entities.Contact;
import com.a65apps.core.entities.Person;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public interface PersonDetailsRepository {
    Single<List<Contact>> getContactsByPerson(@NonNull final String personId);
    Single<Person> getPersonDetails(@NonNull final String personId);
}
