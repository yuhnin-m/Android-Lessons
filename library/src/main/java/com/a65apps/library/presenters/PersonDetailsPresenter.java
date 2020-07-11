package com.a65apps.library.presenters;

import androidx.core.util.Pair;

import com.a65apps.core.entities.Contact;
import com.a65apps.core.entities.Person;
import com.a65apps.core.interactors.contacts.PersonDetailsInteractor;
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor;
import com.a65apps.library.mapper.ContactModelDataMapper;
import com.a65apps.library.mapper.PersonModelAdvancedDataMapper;
import com.a65apps.library.models.PersonModelAdvanced;
import com.a65apps.library.views.PersonDetailsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@InjectViewState
public class PersonDetailsPresenter extends MvpPresenter<PersonDetailsView> {

    @NonNull
    final PersonDetailsInteractor personDetailsInteractor;

    @NonNull
    final BirthdayReminderInteractor reminderInteractor;

    @NonNull
    CompositeDisposable compositeDisposable;

    @NonNull
    ContactModelDataMapper contactModelDataMapper;

    @NonNull
    PersonModelAdvancedDataMapper personModelDataMapper;

    public PersonDetailsPresenter(@NonNull PersonDetailsInteractor personDetailsInteractor, BirthdayReminderInteractor reminderInteractor) {
        this.personDetailsInteractor = personDetailsInteractor;
        this.contactModelDataMapper = new ContactModelDataMapper();
        this.personModelDataMapper = new PersonModelAdvancedDataMapper();
        this.reminderInteractor = reminderInteractor;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void requestContactsByPerson(@NonNull String personId) {
        compositeDisposable.add(personDetailsInteractor.loadPersonDetails(personId)
                .flatMap(person -> personDetailsInteractor.loadContactsByPerson(personId)
                        .map(contactList -> new Pair<Person, List<Contact>>(person, contactList)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().showProgressBar())
                .doFinally(() -> getViewState().hideProgressBar())
                .subscribe(
                        personModelAdvancedListPair -> {
                            getViewState().fetchContactDetails(personModelDataMapper.transform(personModelAdvancedListPair.first));
                            getViewState().fetchContactsInfo(contactModelDataMapper.transform(personModelAdvancedListPair.second));
                        },
                        e -> getViewState().fetchError(e.getMessage())
                ));
    }

    public boolean checkBirthdayReminderEnabled(@NonNull String personId) {
        return reminderInteractor.isReminderOn(personId);
    }

    public boolean birthdayReminderEnable(@NonNull PersonModelAdvanced person) {
        return reminderInteractor.setBirthdayReminder(person.getId(), person.getFullName(), person.getStringBirthday());
    }

    public boolean birthdayReminderDisable(@NonNull String personId) {
        return reminderInteractor.unsetBirthdayReminder(personId);
    }

    @Override
    public void onDestroy() {
        this.compositeDisposable.dispose();
        this.contactModelDataMapper = null;
        this.personModelDataMapper = null;
        super.onDestroy();
    }
}
