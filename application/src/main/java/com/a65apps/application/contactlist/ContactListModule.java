package com.a65apps.application.contactlist;

import com.a65apps.application.scopes.ContactDetailsScope;
import com.a65apps.application.scopes.PersonListScope;
import com.a65apps.core.interactors.contacts.ContactListInteractor;
import com.a65apps.library.presenters.ContactDetailsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactListModule {

    @Provides
    @ContactDetailsScope
    ContactDetailsPresenter provideContactDetailsPresenter(ContactListInteractor interactor) {
        return new ContactDetailsPresenter(interactor);
    }
}
