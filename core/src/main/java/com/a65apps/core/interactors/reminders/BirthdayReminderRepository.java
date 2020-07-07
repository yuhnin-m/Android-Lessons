package com.a65apps.core.interactors.reminders;

import java.util.GregorianCalendar;

public interface BirthdayReminderRepository {
    boolean enableBirthdayReminder(String personId, GregorianCalendar date);
    boolean disableBirthdayReminder(String personId);
}