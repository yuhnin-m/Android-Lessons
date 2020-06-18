package com.a65apps.yuhnin.lesson1.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.callbacks.PersonDetailsCallback;
import com.a65apps.yuhnin.lesson1.callbacks.PersonListCallback;

import java.util.List;

public interface ContactRepository {
    /**
     * Метод отправляет запрос на получение списка всех людей контактов в телефоне
     * Если строка пустая или NULL, то выводит всех, иначе производит поиск по имени
     * @param callback Экземпляр PersonListCallback
     * @param searchString Строка для поиска по имени
     */
    void getAllPersons(PersonListCallback callback, @Nullable String searchString);

    /**
     * Метод отправляет запрос на получения всех контактных телефонов и адресов эл. почты
     * @param callback Экземпляр PersonDetailsCallback
     * @param id Идентификатор контакта
     */
    void getContactByPerson(@NonNull PersonDetailsCallback callback, @NonNull String id);


    /**
     * Метод отправляет запрос на получения подробной информации о контакте
     * @param callback Экземпляр PersonDetailsCallback
     * @param id Идентификатор контакта
     */
    void getPersonById(@NonNull PersonDetailsCallback callback, @NonNull String id);
}
