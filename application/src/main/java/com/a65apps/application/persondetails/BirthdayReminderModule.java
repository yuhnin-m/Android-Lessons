package com.a65apps.application.persondetails;

import com.a65apps.application.scopes.PersonDetailsScope;
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor;
import com.a65apps.core.interactors.reminders.BirthdayReminderModel;
import com.a65apps.core.interactors.reminders.BirthdayReminderRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class BirthdayReminderModule {
    @Provides
    @PersonDetailsScope
    BirthdayReminderInteractor provideBirthdayReminderInteractor(BirthdayReminderRepository repository) {
        return new BirthdayReminderModel(repository);
    }
}
