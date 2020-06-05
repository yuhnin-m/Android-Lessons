package com.a65apps.yuhnin.lesson1.ui.listeners;

public interface EventDataFetchServiceListener {
    void getPersonList(PersonListResultListener callback);
    void getPersonById(String id, PersonResultListener callback);
    void getContactsByPerson(String id, ContactsResultListener callback);
}
