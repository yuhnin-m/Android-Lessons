package com.a65apps.application.personlocation;

import com.a65apps.application.scopes.PersonLocationScope;
import com.a65apps.library.di.containers.PersonLocationContainer;

import dagger.Subcomponent;

@PersonLocationScope
@Subcomponent(modules = {PersonLocationModule.class})
public interface PersonLocationComponent extends PersonLocationContainer {

}
