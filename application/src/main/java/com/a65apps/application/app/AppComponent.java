package com.a65apps.application.app;

import com.a65apps.application.birthdayreminder.BirthdayReminderReceiverComponent;
import com.a65apps.application.persondetails.PersonDetailsComponent;
import com.a65apps.application.personlist.PersonListComponent;
import com.a65apps.application.personlocation.PersonLocationComponent;
import com.a65apps.library.di.containers.AppContainer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class, DbModule.class})

public interface AppComponent extends AppContainer {
    @Override
    PersonDetailsComponent plusPersonDetailsComponent();

    @Override
    PersonListComponent plusPersonListComponent();

    @Override
    BirthdayReminderReceiverComponent plusBirthdayReminderComponent();

    @Override
    PersonLocationComponent plusPersonLocationComponent();

}