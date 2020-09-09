package com.a65apps.core.interactors.reminders

interface BirthdayReminderRepository {
    fun enableBirthdayReminder(personId: String, fullName: String, date: String): Boolean
    fun disableBirthdayReminder(personId: String): Boolean
    fun isBirthdayReminderEnabled(personId: String): Boolean
}
