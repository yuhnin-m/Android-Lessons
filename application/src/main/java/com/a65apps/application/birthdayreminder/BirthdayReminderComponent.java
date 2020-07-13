package com.a65apps.application.birthdayreminder;

import com.a65apps.application.persondetails.PersonDetailsModule;
import com.a65apps.application.scopes.PersonDetailsScope;
import com.a65apps.library.di.containers.BirthdayReminderContainer;

import dagger.Subcomponent;

@PersonDetailsScope
@Subcomponent(modules = {
        BirthdayReminderModule.class})
public interface BirthdayReminderComponent extends BirthdayReminderContainer {
}
