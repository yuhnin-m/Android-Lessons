package com.a65apps.application.persondetails;

import com.a65apps.application.scopes.PersonDetailsScope;
import com.a65apps.core.interactors.contacts.PersonDetailsInteractor;
import com.a65apps.core.interactors.contacts.PersonDetailsModel;
import com.a65apps.core.interactors.contacts.PersonDetailsRepository;
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor;
import com.a65apps.library.mapper.ContactModelDataMapper;
import com.a65apps.library.mapper.PersonModelAdvancedDataMapper;
import com.a65apps.library.presenters.PersonDetailsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonDetailsModule {
    @Provides
    @PersonDetailsScope
    public PersonDetailsPresenter provideContactDetailsPresenter(
            PersonDetailsInteractor personDetailsInteractor,
            BirthdayReminderInteractor birthdayReminderInteractor,
            PersonModelAdvancedDataMapper personModelDataMapper,
            ContactModelDataMapper contactModelDataMapper) {
        return new PersonDetailsPresenter(personDetailsInteractor, birthdayReminderInteractor, personModelDataMapper, contactModelDataMapper);
    }

    @Provides
    @PersonDetailsScope
    public PersonDetailsInteractor providePersonDetailsInteractor(PersonDetailsRepository personDetailsRepository) {
        return new PersonDetailsModel(personDetailsRepository);
    }

    @Provides
    @PersonDetailsScope
    public PersonModelAdvancedDataMapper providePersonModelAdvancedDataMapper() {
        return new PersonModelAdvancedDataMapper();
    }

    @Provides
    @PersonDetailsScope
    public ContactModelDataMapper provideContactModelDataMapper() {
        return new ContactModelDataMapper();
    }
}
