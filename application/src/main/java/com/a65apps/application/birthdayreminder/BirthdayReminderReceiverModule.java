package com.a65apps.application.birthdayreminder;

import com.a65apps.application.scopes.BirthdayReminderScope;
import com.a65apps.application.scopes.PersonDetailsScope;
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor;
import com.a65apps.core.interactors.reminders.BirthdayReminderModel;
import com.a65apps.core.interactors.reminders.BirthdayReminderRepository;

import javax.inject.Scope;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BirthdayReminderReceiverModule {
    @Provides
    @BirthdayReminderScope
    BirthdayReminderInteractor provideBirthdayReminderInteractor(BirthdayReminderRepository repository) {
        return new BirthdayReminderModel(repository);
    }
}
