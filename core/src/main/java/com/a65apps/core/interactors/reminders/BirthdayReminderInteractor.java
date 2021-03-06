package com.a65apps.core.interactors.reminders;

import java.util.GregorianCalendar;

import io.reactivex.rxjava3.annotations.NonNull;

public interface BirthdayReminderInteractor {
    boolean setBirthdayReminder(@NonNull final String personId, @NonNull String fullName, @NonNull String date);
    boolean unsetBirthdayReminder(@NonNull final String personId);
    boolean isReminderOn(@NonNull final String personId);
}
