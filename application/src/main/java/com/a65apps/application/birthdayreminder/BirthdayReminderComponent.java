package com.a65apps.application.birthdayreminder;

import com.a65apps.application.contactlist.ContactListModule;
import com.a65apps.application.scopes.ContactDetailsScope;
import com.a65apps.library.di.containers.BirthdayReminderContainer;

import dagger.Subcomponent;

@ContactDetailsScope
@Subcomponent(modules = {
        BirthdayReminderModule.class,
        ContactListModule.class})
public interface BirthdayReminderComponent extends BirthdayReminderContainer {
}
