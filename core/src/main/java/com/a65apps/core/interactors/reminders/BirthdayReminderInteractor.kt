package com.a65apps.core.interactors.reminders

interface BirthdayReminderInteractor {
    fun setBirthdayReminder(personId: String, fullName: String, date: String): Boolean
    fun unsetBirthdayReminder(personId: String): Boolean
    fun isReminderOn(personId: String): Boolean
}
