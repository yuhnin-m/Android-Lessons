package com.a65apps.yuhnin.lesson1.di.app;

import android.content.Context;

import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFromSystem;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    private ContactRepository contactRepository;

    public RepositoryModule(Context context) {
        contactRepository = new ContactRepositoryFromSystem(context);
    }

    @Provides
    @Singleton
    public ContactRepository provideContactRepository() {
        return contactRepository;
    }
}
