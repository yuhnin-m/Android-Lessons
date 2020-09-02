package com.a65apps.library.presenters

import androidx.core.util.Pair
import com.a65apps.core.entities.Contact
import com.a65apps.core.entities.Person
import com.a65apps.core.interactors.contacts.PersonDetailsInteractor
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor
import com.a65apps.library.mapper.ContactModelDataMapper
import com.a65apps.library.mapper.PersonModelAdvancedDataMapper
import com.a65apps.library.models.PersonModelAdvanced
import com.a65apps.library.views.PersonDetailsView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class PersonDetailsPresenter(
        private val personDetailsInteractor: PersonDetailsInteractor,
        private val reminderInteractor: BirthdayReminderInteractor) : MvpPresenter<PersonDetailsView>() {

    private val personModelDataMapper: PersonModelAdvancedDataMapper = PersonModelAdvancedDataMapper()
    private val contactModelDataMapper: ContactModelDataMapper = ContactModelDataMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    fun requestContactsByPerson(personId: @NonNull String) {
        compositeDisposable.add(personDetailsInteractor.loadPersonDetails(personId)
                .flatMap { person: Person ->
                    personDetailsInteractor.loadContactsByPerson(personId)
                            .map { contactList -> person to contactList }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgressBar() }
                .doFinally { viewState.hideProgressBar() }
                .subscribe(
                        { (person, contacts) ->
                            viewState.fetchContactDetails(personModelDataMapper.transform(person))
                            contacts?.let {
                                viewState.fetchContactsInfo(contactModelDataMapper.transform(it))
                            }
                        }
                )
                { e: Throwable ->
                    e.message?.let {
                        viewState.fetchError(it)
                    }
                }
        )
    }

    fun checkBirthdayReminderEnabled(personId: String): Boolean {
        return reminderInteractor.isReminderOn(personId)
    }

    fun birthdayReminderEnable(person: PersonModelAdvanced): Boolean {
        return reminderInteractor.setBirthdayReminder(person.id, person.displayName, person.dateBirthday)
    }

    fun birthdayReminderDisable(personId: String): Boolean {
        return reminderInteractor.unsetBirthdayReminder(personId)
    }
}