package com.a65apps.yuhnin.lesson1.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;

import java.util.List;

public interface ContactRepository {
    @Nullable
    List<PersonModelCompact> getAllPersons();

    @NonNull
    List<ContactInfoModel> getContactByPerson(String id);

    @Nullable
    PersonModelAdvanced getPersonById(String id);
}
