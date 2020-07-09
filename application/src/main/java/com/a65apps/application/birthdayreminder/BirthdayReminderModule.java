package com.a65apps.application.birthdayreminder;

import android.content.Context;

import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor;
import com.a65apps.core.interactors.reminders.BirthdayReminderModel;
import com.a65apps.core.interactors.reminders.BirthdayReminderRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class BirthdayReminderModule {
    @Provides
    BirthdayReminderInteractor provideBirthdayReminderInteractor(BirthdayReminderRepository repository) {
        return new BirthdayReminderModel(repository);
    }
}
