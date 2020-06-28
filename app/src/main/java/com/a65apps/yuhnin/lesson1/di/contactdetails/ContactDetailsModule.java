package com.a65apps.yuhnin.lesson1.di.contactdetails;

import com.a65apps.yuhnin.lesson1.di.scopes.ContactDetailsScope;
import com.a65apps.yuhnin.lesson1.presenters.ContactDetailsPresenter;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactDetailsModule {

    @Provides
    @ContactDetailsScope
    public ContactDetailsPresenter provideContactDetailsPresenter(ContactRepository contactRepository) {
        return new ContactDetailsPresenter(contactRepository);
    }
}
