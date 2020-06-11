package com.a65apps.yuhnin.lesson1.presenters;

import com.a65apps.yuhnin.lesson1.callbacks.PersonListCallback;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.views.ContactListView;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

public class ContactListPresenter extends MvpPresenter<ContactListView> implements PersonListCallback {
    ContactRepository contactRepository;

    public ContactListPresenter(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void requestContactList() {
        if (contactRepository != null)
            contactRepository.getAllPersons(this);
    }

    @Override
    public void onDestroy() {
        this.contactRepository = null;
        super.onDestroy();
    }

    @Override
    public void getPersonList(List<PersonModelCompact> personList) {
        getViewState().getContactList(personList);
    }
}
