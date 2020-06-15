package com.a65apps.yuhnin.lesson1.presenters;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.a65apps.yuhnin.lesson1.callbacks.PersonListCallback;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.views.ContactListView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> implements PersonListCallback {
    @NonNull
    ContactRepository contactRepository;
    @NonNull
    Handler handler;

    public ContactListPresenter(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        handler = new Handler(Looper.getMainLooper());
    }

    public void requestContactList() {
        contactRepository.getAllPersons(this);
    }

    @Override
    public void getPersonList(final List<PersonModelCompact> personList) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getViewState().getContactList(personList);
            }
        });
    }
}
