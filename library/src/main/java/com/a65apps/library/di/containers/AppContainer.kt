package com.a65apps.library.di.containers

interface AppContainer {
    fun plusPersonDetailsComponent(): PersonDetailsContainer
    fun plusPersonListComponent(): PersonListContainer
    fun plusBirthdayReminderComponent(): BirthdayReminderContainer
    fun plusPersonLocationComponent(): PersonLocationContainer
}
