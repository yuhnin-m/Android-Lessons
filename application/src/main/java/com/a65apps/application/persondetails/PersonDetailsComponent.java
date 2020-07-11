package com.a65apps.application.persondetails;


import com.a65apps.application.birthdayreminder.BirthdayReminderModule;
import com.a65apps.application.scopes.PersonDetailsScope;
import com.a65apps.library.di.containers.PersonDetailsContainer;

import dagger.Subcomponent;

@PersonDetailsScope
@Subcomponent(modules = {PersonDetailsModule.class, BirthdayReminderModule.class})
public interface PersonDetailsComponent extends PersonDetailsContainer {
}
