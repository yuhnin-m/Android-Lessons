package com.a65apps.yuhnin.lesson1.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;

import java.util.List;

public interface ContactRepository {
    @Nullable
    List<PersonModel> getAllPersons();

    @Nullable
    List<ContactInfoModel> getContactByPerson(@NonNull PersonModel personModel);

    @NonNull
    List<ContactInfoModel> getContactByPerson(long id);

    @Nullable
    PersonModel getPersonById(long id);
}
