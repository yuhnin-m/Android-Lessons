package com.a65apps.application.app;


import android.content.Context;

import com.a65apps.core.interactors.contacts.ContactListRepository;
import com.a65apps.core.interactors.persons.PersonListRepository;
import com.a65apps.core.interactors.reminders.BirthdayReminderRepository;
import com.a65apps.library.repositories.ContactsRepository;
import com.a65apps.library.repositories.PersonRepository;
import com.a65apps.library.repositories.ReminderRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    public ContactListRepository provideContactListRepository(Context context) {
        return new ContactsRepository(context);
    }

    @Provides
    @Singleton
    public PersonListRepository provideContactListContentResolverRepository(Context context){
        return new PersonRepository(context);
    }

    @Provides
    @Singleton
    public BirthdayReminderRepository provideBirthdayReminderRepository(Context context){
        return new ReminderRepository(context);
    }
}
