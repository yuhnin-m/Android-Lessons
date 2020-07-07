package com.a65apps.application.contactlist;


import com.a65apps.application.birthdayreminder.BirthdayReminderModule;
import com.a65apps.application.scopes.ContactDetailsScope;
import com.a65apps.library.di.containers.ContactsContainer;

import dagger.Subcomponent;

@ContactDetailsScope
@Subcomponent(modules = {ContactListModule.class, BirthdayReminderModule.class})
public interface ContactListComponent extends ContactsContainer {
}
