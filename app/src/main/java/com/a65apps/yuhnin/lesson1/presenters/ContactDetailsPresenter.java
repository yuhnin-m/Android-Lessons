package com.a65apps.yuhnin.lesson1.presenters;

import androidx.annotation.NonNull;

import com.a65apps.yuhnin.lesson1.callbacks.PersonDetailsCallback;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.views.ContactDetailsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

@InjectViewState
public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> implements PersonDetailsCallback {
    @NonNull
    ContactRepository contactRepository;

    public ContactDetailsPresenter(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void requestContactsByPerson(String personId) {
        contactRepository.getContactByPerson(this, personId);
    }

    public void requestPersonDetails(String personId) {
        contactRepository.getPersonById(this, personId);
    }
    @Override
    public void onDestroy() {
        this.contactRepository = null;
        super.onDestroy();
    }

    @Override
    public void onFetchPersonDetails(PersonModelAdvanced personModel) {
        getViewState().getContactDetails(personModel);
    }

    @Override
    public void onFetchPersonContacts(List<ContactInfoModel> contactList) {
        getViewState().getContactsInfo(contactList);
    }

}
