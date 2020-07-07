package com.a65apps.application.personlist;

import com.a65apps.application.contactlist.ContactListModule;
import com.a65apps.application.scopes.ContactDetailsScope;
import com.a65apps.core.interactors.persons.PersonListModel;
import com.a65apps.library.di.containers.ContactsContainer;

import dagger.Subcomponent;

@ContactDetailsScope
@Subcomponent(modules = {PersonListModule.class})
public interface PersonListComponent extends ContactsContainer {
}