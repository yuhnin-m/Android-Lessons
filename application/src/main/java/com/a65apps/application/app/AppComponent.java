package com.a65apps.application.app;

import com.a65apps.application.birthdayreminder.BirthdayReminderComponent;
import com.a65apps.application.persondetails.PersonDetailsComponent;
import com.a65apps.application.personlist.PersonListComponent;
import com.a65apps.library.di.containers.AppContainer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        RepositoryModule.class})
public interface AppComponent extends AppContainer {
    PersonDetailsComponent plusPersonDetailsComponent();
    PersonListComponent plusPersonListComponent();
    BirthdayReminderComponent plusBirthdayReminderComponent();
}