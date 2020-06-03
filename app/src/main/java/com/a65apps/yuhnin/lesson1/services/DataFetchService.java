package com.a65apps.yuhnin.lesson1.services;

import android.app.Person;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ListView;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFakeImp;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFromSystem;
import com.a65apps.yuhnin.lesson1.ui.listeners.ContactsResultListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.PersonListResultListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.PersonResultListener;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

public class DataFetchService extends Service {

    // Экземпляр Binder для клиентов
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    public DataFetchService() {
    }


    public class LocalBinder extends Binder {
        public DataFetchService getService() {
            return DataFetchService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }

    public void fetchPersons(PersonListResultListener callback) {
        final WeakReference<PersonListResultListener> ref = new WeakReference(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PersonModel> personModels = ContactRepositoryFromSystem.getInstance(getApplicationContext()).getAllPersons();
                PersonListResultListener local = ref.get();
                if (local != null) {
                    local.onFetchPersonList(personModels);
                }
            }
        }).start();
    }

    public void fetchPersonById(PersonResultListener callback, final long personId) {
        final WeakReference<PersonResultListener> ref = new WeakReference(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PersonModel personModel = ContactRepositoryFakeImp.getInstance().getPersonById(personId);
                PersonResultListener local = ref.get();
                if (local != null) {
                    local.onFetchPersonModel(personModel);
                }
            }
        }).start();
    }

    public void fetchContactInfo(ContactsResultListener callback, final long personId) {
        final WeakReference<ContactsResultListener> ref = new WeakReference(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ContactInfoModel> contactInfoModels = ContactRepositoryFakeImp
                        .getInstance().getContactByPerson(personId);
                ContactsResultListener local = ref.get();
                if (local != null) {
                    local.onFetchContacts(contactInfoModels);
                }
            }
        }).start();
    }


}
