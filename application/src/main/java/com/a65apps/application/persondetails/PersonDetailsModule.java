package com.a65apps.application.persondetails;

import com.a65apps.application.scopes.PersonDetailsScope;
import com.a65apps.core.interactors.contacts.PersonDetailsInteractor;
import com.a65apps.core.interactors.contacts.PersonDetailsModel;
import com.a65apps.core.interactors.contacts.PersonDetailsRepository;
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor;
import com.a65apps.library.presenters.PersonDetailsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonDetailsModule {


    @Provides
    @PersonDetailsScope
    public PersonDetailsPresenter provideContactDetailsPresenter(PersonDetailsInteractor personDetailsInteractor, BirthdayReminderInteractor birthdayReminderInteractor){
        return new PersonDetailsPresenter(personDetailsInteractor, birthdayReminderInteractor);
    }

    @Provides
    @PersonDetailsScope
    public PersonDetailsInteractor providePersonDetailsInteractor(PersonDetailsRepository personDetailsRepository){
        return new PersonDetailsModel(personDetailsRepository);
    }

}
