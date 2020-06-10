package com.a65apps.yuhnin.lesson1.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.callbacks.PersonDetailsCallback;
import com.a65apps.yuhnin.lesson1.callbacks.PersonListCallback;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.a65apps.yuhnin.lesson1.ui.listeners.ContactsResultListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.PersonListResultListener;

import java.util.List;

public interface ContactRepository {
    @Nullable
    List<PersonModelCompact> getAllPersons(PersonListCallback callback);

    @NonNull
    List<ContactInfoModel> getContactByPerson(PersonDetailsCallback callback, String id);

    @Nullable
    PersonModelAdvanced getPersonById(PersonDetailsCallback callback, String id);
}
