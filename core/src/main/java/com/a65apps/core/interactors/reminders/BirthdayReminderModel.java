package com.a65apps.core.interactors.reminders;

import java.util.GregorianCalendar;

import io.reactivex.rxjava3.annotations.NonNull;

public class BirthdayReminderModel implements BirthdayReminderInteractor{
    BirthdayReminderRepository repository;

    public BirthdayReminderModel(BirthdayReminderRepository repository) {
        this.repository = repository;
    }
    @Override
    public boolean setBirthdayReminder(@NonNull final String personId, @NonNull final String fullName, @NonNull final String date) {
        return this.repository.enableBirthdayReminder(personId, fullName, date);
    }

    @Override
    public boolean unsetBirthdayReminder(@NonNull final String personId) {
        return this.repository.disableBirthdayReminder(personId);
    }

    @Override
    public boolean isReminderOn(@NonNull final String personId) {
        return repository.isBirthdayReminderEnabled(personId);
    }
}
