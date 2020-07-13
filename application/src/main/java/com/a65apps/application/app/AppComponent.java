package com.a65apps.application.app;

import com.a65apps.application.birthdayreminder.BirthdayReminderComponent;
import com.a65apps.application.persondetails.PersonDetailsComponent;
import com.a65apps.application.persondetails.PersonDetailsModule;
import com.a65apps.application.personlist.PersonListComponent;
import com.a65apps.application.personlist.PersonListModule;
import com.a65apps.library.di.containers.AppContainer;
import com.a65apps.library.di.containers.BirthdayReminderContainer;
import com.a65apps.library.di.containers.PersonDetailsContainer;
import com.a65apps.library.di.containers.PersonListContainer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class})

public interface AppComponent extends AppContainer {

    Re
    @Override
    PersonDetailsContainer plusPersonDetailsContainer();

    @Override
    PersonListContainer plusPersonListContainer();

    @Override
    BirthdayReminderContainer plusBirthdayReminderContainer();
}