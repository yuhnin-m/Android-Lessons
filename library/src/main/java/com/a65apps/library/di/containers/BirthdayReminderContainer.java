package com.a65apps.library.di.containers;

import com.a65apps.library.receivers.BirthdayReminderReceiver;

public interface BirthdayReminderContainer {
    void inject(BirthdayReminderReceiver reminderReceiver);
}
