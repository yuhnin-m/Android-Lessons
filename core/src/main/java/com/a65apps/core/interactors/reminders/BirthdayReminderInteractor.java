package com.a65apps.core.interactors.reminders;

import java.util.GregorianCalendar;

import io.reactivex.rxjava3.annotations.NonNull;

public interface BirthdayReminderInteractor {
    boolean onBirthdayReminder(@NonNull final String personId, @NonNull GregorianCalendar date);
    boolean offBirthdayReminder(@NonNull final String personId);
}
