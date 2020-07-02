package com.a65apps.yuhnin.lesson1.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface ContactRepository {
    
    Single<List<PersonModelCompact>> getAllPersons(@Nullable String searchString);

    Single<List<ContactInfoModel>> getContactByPerson(@NonNull String id);

    Single<PersonModelAdvanced> getPersonById(@NonNull String id);
}
