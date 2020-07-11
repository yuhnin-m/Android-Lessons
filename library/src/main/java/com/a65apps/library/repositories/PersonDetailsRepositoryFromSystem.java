package com.a65apps.library.repositories;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.a65apps.core.entities.Contact;
import com.a65apps.core.entities.ContactType;
import com.a65apps.core.entities.Person;
import com.a65apps.core.interactors.contacts.PersonDetailsRepository;
import com.a65apps.library.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;

public class PersonDetailsRepositoryFromSystem implements PersonDetailsRepository {
    private final String LOG_TAG = "contact_list_repository";
    private final Uri KEY_CONTENT_PHONE = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private final Uri KEY_CONTENT_EMAIL = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;


    @NonNull
    private Context context;

    public PersonDetailsRepositoryFromSystem(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Метод для получения списка контактной информации у контакта
     * @param personId идентификатор контакта
     * @return список с контактной информацией
     */
    @Nullable
    private List<Contact> getContacts(@NonNull final String personId) {
        List<Contact> contactInfoModels = new ArrayList<Contact>();
        try {
            List<Contact> phoneNumbers = getContactsByPerson(KEY_CONTENT_PHONE, personId, context.getContentResolver());
            if (phoneNumbers != null) {
                contactInfoModels.addAll(phoneNumbers);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения списка телефонов контакта id=" + personId +
                    ". Текст ошибки: " + e.getMessage());
        }
        try {
            List<Contact> emails = getContactsByPerson(KEY_CONTENT_EMAIL, personId, context.getContentResolver());
            if (emails != null) {
                contactInfoModels.addAll(emails);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения списка адресов эл.почты контакта " + personId +
                    ". Текст ошибки: " + e.getMessage());
        }
        return contactInfoModels;
    }


    /**
     * Метод, в зависимости от параметра, возвращающий номера телефонов или адреса
     * электронной почты для определенного контакта с заданным ID
     * @param uri ссылка для поиска контактов: либо KEY_CONTENT_PHONE, либо KEY_CONTENT_EMAIL
     * @param personId идентификатор контакта
     * @param contentResolver экземпляр ContentResolver
     * @return Список номеров телефонов, либо адресов email
     */
    @Nullable
    private List<Contact> getContactsByPerson(@NonNull Uri uri, @NonNull String personId, @NonNull ContentResolver contentResolver) {
        Log.d(LOG_TAG, "Читаем номера телефонов по id=" + personId);
        List<Contact> phoneList = new ArrayList<Contact>();
        Cursor cursorPhone = contentResolver.query(uri, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + personId,
                null, null);
        try {
            if (cursorPhone != null) {
                while (cursorPhone.moveToNext()) {
                    String phoneNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String phoneId = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    if (phoneId != null && phoneNumber != null && !phoneNumber.isEmpty()) {
                        Log.d(LOG_TAG, "Найден номер телефона: " + phoneNumber);
                        phoneList.add(new Contact(
                                Long.parseLong(phoneId),
                                personId,
                                uri.equals(KEY_CONTENT_PHONE) ? ContactType.PHONE_NUMBER : ContactType.EMAIL,
                                phoneNumber));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения номеров телефона контакта " + personId +
                    ". Текст ошибки: " + e.getMessage());
        }
        finally {
            if (cursorPhone != null){
                cursorPhone.close();
            }
        }
        return phoneList;
    }

    /**
     * Метод получения информации о контакте
     * @param personId идентификатор контакта
     * @return возвращает экземпляр PersonModelAdvanced
     */
    @Nullable
    public Person getPerson(@NonNull final String personId) {
        Person personModelAdvanced = null;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,
                ContactsContract.Contacts._ID + " = " + personId,null ,null);
        try {
            if (cursor != null) {
                cursor.moveToNext();
                String displaName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String strPhotoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                String dateBirthDay = getDateBirthday(id, contentResolver);
                String description = getCompanyName(id, contentResolver);
                personModelAdvanced = new Person(
                        personId,
                        displaName,
                        description,
                        strPhotoUri == null ? Constants.URI_DRAWABLE_AVATAR_NOT_FOUND : strPhotoUri,
                        dateBirthDay);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Ошибка получения информации о контакте" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return personModelAdvanced;
    }

    /**
     * Метод возвращающий дату рождения для определенного контакта с заданным ID
     * FIXME: Не работает абсолютно. На Xiaomi выдает все что угодно, кроме даты рождения
     * @param id Идентификатор контакта
     * @param contentResolver Экземпляр ContentResolver
     * @return строка - дата рождения
     */
    @Nullable
    private String getDateBirthday(@NonNull String id, @NonNull ContentResolver contentResolver) {
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String birthDay = null;
        String[] projection = new String[] {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
        };
        String where =
                ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + " AND " +
                        ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = " + id;

        Cursor cursorBirthday = contentResolver.query(uri, projection, where, null, null,null);
        try {
            if (cursorBirthday != null){
                while (cursorBirthday.moveToNext()){
                    birthDay = cursorBirthday.getString(cursorBirthday.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                    Log.d(LOG_TAG, "Найден день рождения контакта " + id + ": " + birthDay);
                }
            }
        }
        finally {
            if (cursorBirthday != null){
                cursorBirthday.close();
            }
        }
        return birthDay;
    }

    /**
     * Метод возвращающий название организации контакта с определенным ID
     * FIXME: Не работает, выдает все подряд
     * @param personId Идентификатор контакта
     * @param contentResolver Экземпляр ContentResolver
     * @return Имя компании и сопутствующие данные
     */
    @Nullable
    private String getCompanyName(@NonNull String personId, @NonNull ContentResolver contentResolver) {
        Log.d(LOG_TAG, "Читаем место работы контакта id=" + personId);
        String personJobDescription = "";
        Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + personId,
                null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (personJobDescription.isEmpty())
                        personJobDescription = cursor.getString(cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Organization.COMPANY));
                    else
                        personJobDescription += " " + cursor.getString(cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Organization.COMPANY));
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения места работы контакта " + personId +
                    ". Текст ошибки: " + e.getMessage());
        }
        finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return personJobDescription;
    }

    @Override
    public Single<List<Contact>> getContactsByPerson(@NonNull String personId) {
        return Single.fromCallable(() -> getContacts(personId));
    }

    @Override
    public Single<Person> getPersonDetails(@NonNull String personId) {
        return Single.fromCallable(() -> getPerson(personId));
    }
}
