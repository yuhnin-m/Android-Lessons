package com.a65apps.application.personlist;

import com.a65apps.application.scopes.ContactDetailsScope;
import com.a65apps.application.scopes.PersonListScope;
import com.a65apps.core.interactors.contacts.ContactListInteractor;
import com.a65apps.core.interactors.persons.PersonListInteractor;
import com.a65apps.library.presenters.ContactDetailsPresenter;
import com.a65apps.library.presenters.PersonListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonListModule {

    @Provides
    @PersonListScope
    PersonListPresenter provideContactDetailsPresenter(PersonListInteractor interactor) {
        return new PersonListPresenter(interactor);
    }
}
