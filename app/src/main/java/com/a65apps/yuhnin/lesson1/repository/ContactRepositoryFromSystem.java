package com.a65apps.yuhnin.lesson1.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContactRepositoryFromSystem implements ContactRepository {
    final String LOG_TAG = "contact_repository";

    private static ContactRepositoryFromSystem instance;
    private Context context;


    public static synchronized ContactRepositoryFromSystem getInstance(Context context) {
        if (instance == null) {
            instance = new ContactRepositoryFromSystem();
            instance.context = context;
        }
        return instance;
    }

    @Nullable
    @Override
    public List<PersonModel> getAllPersons() {
        ArrayList<PersonModel> personModels = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.d(LOG_TAG, "Найден пользователь: id=" + id + "; ФИО: " + name);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public List<ContactInfoModel> getContactByPerson(@NonNull PersonModel personModel) {
        return null;
    }

    @NonNull
    @Override
    public List<ContactInfoModel> getContactByPerson(long id) {
        return null;
    }


    @Override
    public PersonModel getPersonById(long id) {
        return null;
    }




    /**
     * Метод возвращающий дату рождения для определенного контакта с заданным ID
     * @param id Идентификатор контакта
     * @return строка - дата рождения
     */
    private String readBirthday(String id) {
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
        String[] selectionArgs = new String[] {
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        };
        String strDateBirthday = null;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursorBirthday = contentResolver.query(uri, projection, where, selectionArgs, null,null);
        try {
            if (cursorBirthday != null){
                while (cursorBirthday.moveToNext()){
                    birthDay = cursorBirthday.getString(cursorBirthday.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                    Log.d(LOG_TAG, "Найден день рождения контакта " + id + ": " + birthDay);
                }
            }
        }finally {
            if (cursorBirthday != null){
                cursorBirthday.close();
            }
        }
        return birthDay;
    }

    /**
     * Метод возвращающий номера телефонов для определенного контакта с заданным ID
     * @param id Идентификатор контакта
     * @return Список номеров телефона
     */
    @Nullable
    private List<String> readPhones(String id) {
        Log.d(LOG_TAG, "Читаем номера телефонов по id=" + id);
        List<String> phoneList = new ArrayList<String>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                null, null);
        try {
            if (cursorPhone != null) {
                while (cursorPhone.moveToNext()) {
                    String phone_number = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (phone_number != null && !phone_number.isEmpty()) {
                        Log.d(LOG_TAG, "Найден номер телефона: " + phone_number);
                        phoneList.add(phone_number);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения номеров телефона контакта " + id +
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
     * Метод возвращающий адреса электронной почты для определенного контакта с заданным ID
     * @param id идентификатор контакта
     * @param contentResolver экземпляр ContentResolver
     * @return список адресов электронной почты
     */
    private List<String> readEmails(String id, ContentResolver contentResolver) {
        List<String> emailList = new ArrayList<String>();
        Cursor cursorEmail = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
                null, null);
        try {
            if (cursorEmail != null) {
                while (cursorEmail.moveToNext()) {
                    String email = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if (email != null && !email.isEmpty()) {
                        Log.d(LOG_TAG, "Найдена электронная почта: " + email);
                        emailList.add(email);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения email'ов контакта " + id +
                    ". Текст ошибки: " + e.getMessage());
        } finally {
            if (cursorEmail != null){
                cursorEmail.close();
            }
        }
        return emailList;
    }

}
