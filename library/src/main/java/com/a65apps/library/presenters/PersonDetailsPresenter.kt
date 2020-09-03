package com.a65apps.library.presenters

import com.a65apps.core.interactors.contacts.PersonDetailsInteractor
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor
import com.a65apps.library.mapper.ContactModelDataMapper
import com.a65apps.library.mapper.PersonModelAdvancedDataMapper
import com.a65apps.library.models.PersonModelAdvanced
import com.a65apps.library.views.PersonDetailsView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@InjectViewState
class PersonDetailsPresenter(
        private val personDetailsInteractor: PersonDetailsInteractor,
        private val reminderInteractor: BirthdayReminderInteractor) : MvpPresenter<PersonDetailsView>() {

    private val personModelDataMapper: PersonModelAdvancedDataMapper = PersonModelAdvancedDataMapper()
    private val contactModelDataMapper: ContactModelDataMapper = ContactModelDataMapper()
    private val job = SupervisorJob()
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    fun requestContactsByPerson(personId: String) {
        try {
            scope.launch {
                viewState.showProgressBar()
                personDetailsInteractor.loadPersonDetails(personId).flowOn(Dispatchers.IO).flatMapMerge { personDetails ->
                    personDetailsInteractor.loadContactsByPerson(personId)
                            .map { contactList -> personDetails to contactList }
                }.collect() { (personDetails, contactList) ->
                    viewState.fetchContactDetails(personModelDataMapper.transform(personDetails))
                    viewState.fetchContactsInfo(contactModelDataMapper.transform(contactList))
                    viewState.hideProgressBar()
                }
            }
        } catch (e: Exception) {
            e.message?.let {
                viewState.fetchError(it)
                viewState.hideProgressBar()
            }
        }
    }

    fun checkBirthdayReminderEnabled(personId: String): Boolean {
        return reminderInteractor.isReminderOn(personId)
    }

    fun birthdayReminderEnable(person: PersonModelAdvanced): Boolean {
        return reminderInteractor.setBirthdayReminder(person.id, person.displayName, person.dateBirthday
                ?: "")
    }

    fun birthdayReminderDisable(personId: String): Boolean {
        return reminderInteractor.unsetBirthdayReminder(personId)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}