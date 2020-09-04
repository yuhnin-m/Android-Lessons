package com.a65apps.library.di.containers

import com.a65apps.library.receivers.BirthdayReminderReceiver

interface BirthdayReminderContainer {
    fun inject(reminderReceiver: BirthdayReminderReceiver)
}