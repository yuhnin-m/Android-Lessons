package com.a65apps.library.di.containers;

public interface AppContainer {
    PersonDetailsContainer plusPersonDetailsComponent();
    PersonListContainer plusPersonListComponent();
    BirthdayReminderContainer plusBirthdayReminderComponent();
    PersonLocationContainer plusPersonLocationContainer();

}
