package com.a65apps.core.interactors.reminders;

import java.util.GregorianCalendar;

import io.reactivex.rxjava3.annotations.NonNull;

public interface BirthdayReminderRepository {
    boolean enableBirthdayReminder(@NonNull String personId, @NonNull String fullName, @NonNull String date);
    boolean disableBirthdayReminder(@NonNull String personId);
    boolean isBirthdayReminderEnabled(@NonNull String personId);
}