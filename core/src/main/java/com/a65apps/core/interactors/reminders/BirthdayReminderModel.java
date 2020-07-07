package com.a65apps.core.interactors.reminders;

import java.util.GregorianCalendar;

import io.reactivex.rxjava3.annotations.NonNull;

public class BirthdayReminderModel implements BirthdayReminderInteractor{
    BirthdayReminderRepository repository;

    public BirthdayReminderModel(BirthdayReminderRepository repository) {
        this.repository = repository;
    }
    @Override
    public boolean onBirthdayReminder(@NonNull final String personId, @NonNull final GregorianCalendar date) {
        return this.repository.enableBirthdayReminder(personId, date);
    }

    @Override
    public boolean offBirthdayReminder(@NonNull final String personId) {
        return this.repository.disableBirthdayReminder(personId);
    }
}
