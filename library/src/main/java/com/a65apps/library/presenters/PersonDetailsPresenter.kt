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
import io.reactivex.rxjava3.disposables.Disposable
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
                            .map { contactList: List<Contact> -> Pair(person, contactList) }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgressBar() }
                .doFinally { viewState.hideProgressBar() }
                .subscribe(
                        { personModelAdvancedListPair: Pair<Person, List<Contact>> ->
                            personModelAdvancedListPair.first?.let {
                                viewState.fetchContactDetails(personModelDataMapper.transform(it))
                            }
                            personModelAdvancedListPair.second?.let {
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

    fun checkBirthdayReminderEnabled(personId: @NonNull String?): Boolean {
        return reminderInteractor.isReminderOn(personId)
    }

    fun birthdayReminderEnable(person: @NonNull PersonModelAdvanced?): Boolean {
        return reminderInteractor.setBirthdayReminder(person!!.id, person.fullName, person.stringBirthday)
    }

    fun birthdayReminderDisable(personId: @NonNull String?): Boolean {
        return reminderInteractor.unsetBirthdayReminder(personId)
    }
}