package com.a65apps.yuhnin.lesson1.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.callbacks.PersonDetailsCallback;
import com.a65apps.yuhnin.lesson1.callbacks.PersonListCallback;

import java.util.List;

public interface ContactRepository {
    void getAllPersons(PersonListCallback callback);

    void getContactByPerson(PersonDetailsCallback callback, String id);

    void getPersonById(PersonDetailsCallback callback, String id);
}
