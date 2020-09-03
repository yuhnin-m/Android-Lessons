package com.a65apps.core.interactors.reminders

class BirthdayReminderModel(var repository: BirthdayReminderRepository) : BirthdayReminderInteractor {
    override fun setBirthdayReminder(personId: String, fullName: String, date: String): Boolean {
        return repository.enableBirthdayReminder(personId, fullName, date)
    }

    override fun unsetBirthdayReminder(personId: String): Boolean {
        return repository.disableBirthdayReminder(personId)
    }

    override fun isReminderOn(personId: String): Boolean {
        return repository.isBirthdayReminderEnabled(personId)
    }
}
