package com.a65apps.yuhnin.lesson1.di.contactlist;

import com.a65apps.yuhnin.lesson1.di.scopes.ContactListScope;
import com.a65apps.yuhnin.lesson1.presenters.ContactListPresenter;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactListModule {

    @Provides
    @ContactListScope
    ContactListPresenter provideContactListPresenter(ContactRepository contactRepository) {
        return new ContactListPresenter(contactRepository);
    }
}
