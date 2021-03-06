package com.a65apps.application.app;


import android.content.Context;

import com.a65apps.core.interactors.contacts.PersonDetailsRepository;
import com.a65apps.core.interactors.persons.PersonListRepository;
import com.a65apps.core.interactors.reminders.BirthdayReminderRepository;
import com.a65apps.library.repositories.PersonDetailsRepositoryFromSystem;
import com.a65apps.library.repositories.PersonListRepositoryFromSystem;
import com.a65apps.library.repositories.ReminderRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    public PersonDetailsRepository providePersonDetailsRepository(Context context) {
        return new PersonDetailsRepositoryFromSystem(context);
    }

    @Provides
    @Singleton
    public PersonListRepository providePersonListRepository(Context context){
        return new PersonListRepositoryFromSystem(context);
    }

    @Provides
    @Singleton
    public BirthdayReminderRepository provideBirthdayReminderRepository(Context context){
        return new ReminderRepository(context);
    }
}
