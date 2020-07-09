package com.a65apps.application.personlist;

import com.a65apps.application.scopes.PersonListScope;
import com.a65apps.library.di.containers.PersonListContainer;

import dagger.Subcomponent;

@PersonListScope
@Subcomponent(modules = {PersonListModule.class})
public interface PersonListComponent extends PersonListContainer {
}