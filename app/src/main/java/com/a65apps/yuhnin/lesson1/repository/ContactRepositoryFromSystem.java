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
import com.a65apps.yuhnin.lesson1.pojo.ContactType;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;

import java.text.ParseException;
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
    public List<PersonModelCompact> getAllPersons() {
        ArrayList<PersonModelCompact> personList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    try {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String displaName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String strPhotoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                        Log.d(LOG_TAG, "Найден контакт: id=" + id + "; ФИО: " + displaName + " фото="+strPhotoUri);
                        personList.add(new PersonModelCompact(Integer.valueOf(id), displaName, strPhotoUri==null ? null : Uri.parse(strPhotoUri)));
                        //Log.d(LOG_TAG, "Контакт добавлен");
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
        return personList;
    }


    @NonNull
    @Override
    public List<ContactInfoModel> getContactByPerson(long id) {
        String personId = String.valueOf(id);
        List<ContactInfoModel> contactInfoModels = new ArrayList<ContactInfoModel>();
        try {
            List<ContactInfoModel> phoneNumbers = getPhoneList(personId, context.getContentResolver());
            if (phoneNumbers != null) {
                contactInfoModels.addAll(phoneNumbers);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения списка телефонов контакта id=" + id +
                    ". Текст ошибки: " + e.getMessage());
        }

        try {
            List<ContactInfoModel> emails = getEmailList(personId, context.getContentResolver());
            if (emails != null) {
                contactInfoModels.addAll(emails);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения списка адресов эл.почты контакта " + id +
                    ". Текст ошибки: " + e.getMessage());
        }

        return contactInfoModels;
    }


    @Override
    public PersonModelAdvanced getPersonById(long personId) {
        PersonModelAdvanced personModelAdvanced = null;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,
                ContactsContract.Contacts._ID + " = " + personId,null ,null);
        try{
            if (cursor != null) {
                cursor.moveToNext();
                String displaName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String strPhotoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                String dateBirthDay = getDateBirthday(id, contentResolver);
                String description = getCompanyName(id, contentResolver);
                personModelAdvanced = new PersonModelAdvanced(Integer.valueOf(id), displaName, description, strPhotoUri == null ? null : Uri.parse(strPhotoUri), dateBirthDay);
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
    private String getDateBirthday(String id, ContentResolver contentResolver) {
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
     * Метод возвращающий номера телефонов для определенного контакта с заданным ID
     * @param personId Идентификатор контакта
     * @param contentResolver Экземпляр ContentResolver
     * @return Список номеров телефона
     */
    @Nullable
    private List<ContactInfoModel> getPhoneList(String personId, ContentResolver contentResolver) {
        Log.d(LOG_TAG, "Читаем номера телефонов по id=" + personId);
        List<ContactInfoModel> phoneList = new ArrayList<ContactInfoModel>();
        Cursor cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + personId,
                null, null);
        try {
            if (cursorPhone != null) {
                while (cursorPhone.moveToNext()) {
                    String phoneNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String phoneId = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    if (phoneId != null && phoneNumber != null && !phoneNumber.isEmpty()) {
                        Log.d(LOG_TAG, "Найден номер телефона: " + phoneNumber);
                        phoneList.add(new ContactInfoModel(
                                Long.parseLong(phoneId),
                                Integer.parseInt(personId),
                                ContactType.PHONE_NUMBER,
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
     * Метод возвращающий название организации контакта с определенным ID
     * FIXME: Не работает, выдает все подряд
     * @param personId Идентификатор контакта
     * @param contentResolver Экземпляр ContentResolver
     * @return Имя компании и сопутствующие данные
     */
    @Nullable
    private String getCompanyName(String personId, ContentResolver contentResolver) {
        Log.d(LOG_TAG, "Читаем место работы контакта id=" + personId);
        String personJobDescription = "";
        Cursor cursorPhone = contentResolver.query(ContactsContract.Data.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + personId,
                null, null);
        try {
            if (cursorPhone != null) {
                while (cursorPhone.moveToNext()) {
                    if (personJobDescription.isEmpty())
                        personJobDescription = cursorPhone.getString(cursorPhone.getColumnIndex(
                                ContactsContract.CommonDataKinds.Organization.COMPANY));
                    else
                        personJobDescription += " " + cursorPhone.getString(cursorPhone.getColumnIndex(
                                ContactsContract.CommonDataKinds.Organization.COMPANY));
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения места работы контакта " + personId +
                    ". Текст ошибки: " + e.getMessage());
        }
        finally {
            if (cursorPhone != null){
                cursorPhone.close();
            }
        }
        return personJobDescription;
    }

    /**
     * Метод возвращающий адреса электронной почты для определенного контакта с заданным ID
     * @param personId идентификатор контакта
     * @param contentResolver Экземпляр ContentResolver
     * @return список адресов электронной почты
     */
    private List<ContactInfoModel> getEmailList(String personId, ContentResolver contentResolver) {
        List<ContactInfoModel> emailList = new ArrayList<ContactInfoModel>();
        Cursor cursorEmail = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + personId,
                null, null);
        try {
            if (cursorEmail != null) {
                while (cursorEmail.moveToNext()) {
                    String emailAddress = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    String emailId = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID));
                    if (emailId != null && emailAddress != null && !emailAddress.isEmpty()) {
                        Log.d(LOG_TAG, "Найдена электронная почта: " + emailAddress);
                        emailList.add(new ContactInfoModel(
                                Long.valueOf(emailId),
                                Integer.valueOf(personId),
                                ContactType.EMAIL,
                                emailAddress));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Произошла ошибка чтения email'ов контакта " + personId +
                    ". Текст ошибки: " + e.getMessage());
        } finally {
            if (cursorEmail != null){
                cursorEmail.close();
            }
        }
        return emailList;
    }

}
