package com.a65apps.yuhnin.lesson1.ui.listeners;

public interface EventDataFetchServiceListener {
    void getPersonList(PersonListResultListener callback);
    void getPersonById(long id, PersonResultListener callback);
    void getContactsByPerson(long id, ContactsResultListener callback);
}
