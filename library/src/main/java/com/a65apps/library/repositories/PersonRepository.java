package com.a65apps.library.repositories;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.a65apps.core.entities.Person;
import com.a65apps.core.interactors.persons.PersonListRepository;
import com.a65apps.library.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;

public class PersonRepository implements PersonListRepository {
    final String LOG_TAG = "person_repository";
    @NonNull
    private Context context;

    public PersonRepository(@NonNull Context context) {
        this.context = context;
    }

    @Nullable
    public List<Person> getPersonList(@Nullable String searchString) {
        List<Person> personList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor;
        if (searchString == null || searchString.isEmpty()) {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
        } else {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null,ContactsContract.Contacts.DISPLAY_NAME + " LIKE \'%" + searchString + "%\'",
                    null,null);
        }
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    try {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String displaName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String strPhotoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                        if (strPhotoUri == null) strPhotoUri = Constants.URI_DRAWABLE_AVATAR_NOT_FOUND;
                        Log.d(LOG_TAG, "Найден контакт: id=" + id + "; ФИО: " + displaName + " фото="+strPhotoUri);
                        if (id != null && displaName != null) {
                            personList.add(new Person(id, displaName, null, strPhotoUri, null));
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "Произошла ошибка получения контакта: " + e.getMessage());
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(LOG_TAG, "Найдено " + personList.size() + " контактов");
        return personList;
    }

    @Override
    public Single<List<Person>> getAllPersons(@Nullable String searchString) {
        return Single.fromCallable(() -> getPersonList(searchString));
    }


}
