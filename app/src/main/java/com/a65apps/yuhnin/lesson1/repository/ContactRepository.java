package com.a65apps.yuhnin.lesson1.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface ContactRepository {
    Observable<List<PersonModelCompact>> getAllPersons(@Nullable String searchString);

    Observable<List<ContactInfoModel>> getContactByPerson(@NonNull String id);

    Observable<PersonModelAdvanced> getPersonById(@NonNull String id);
}
