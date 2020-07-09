package com.a65apps.application.app;

import com.a65apps.application.birthdayreminder.BirthdayReminderComponent;
import com.a65apps.application.contactlist.ContactListComponent;
import com.a65apps.application.personlist.PersonListComponent;
import com.a65apps.library.di.containers.AppContainer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        RepositoryModule.class})
public interface AppComponent extends AppContainer {
    ContactListComponent plusContactListContainer();
    PersonListComponent plusContactDetailsContainer();
    BirthdayReminderComponent plusBirthdayReminderComponent();
}