package com.a65apps.yuhnin.lesson1.presenters;

import android.os.Handler;
import android.os.Looper;

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
    private final ContactRepository contactRepository;
    @NonNull
    private final Handler handler;

    public ContactDetailsPresenter(@NonNull ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void requestContactsByPerson(String personId) {
        contactRepository.getContactByPerson(this, personId);
    }

    public void requestPersonDetails(String personId) {
        contactRepository.getPersonById(this, personId);
        getViewState().showProgressBar();
    }

    @Override
    public void onFetchPersonDetails(final PersonModelAdvanced personModel) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getViewState().getContactDetails(personModel);
                getViewState().hideProgressBar();
            }
        });
    }

    @Override
    public void onFetchPersonContacts(final List<ContactInfoModel> contactList) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getViewState().getContactsInfo(contactList);
                getViewState().hideProgressBar();
            }
        });
    }

}
