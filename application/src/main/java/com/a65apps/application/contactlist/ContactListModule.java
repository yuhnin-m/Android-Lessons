package com.a65apps.application.contactlist;

import com.a65apps.application.scopes.ContactDetailsScope;
import com.a65apps.core.interactors.contacts.ContactListInteractor;
import com.a65apps.core.interactors.contacts.ContactListModel;
import com.a65apps.core.interactors.contacts.ContactListRepository;
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor;
import com.a65apps.library.presenters.ContactDetailsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactListModule {


    @Provides
    @ContactDetailsScope
    public ContactDetailsPresenter provideContactDetailsPresenter(ContactListInteractor contactListInteractor,
                                                                  BirthdayReminderInteractor reminderInteractor){
        return new ContactDetailsPresenter(contactListInteractor, reminderInteractor);
    }

    @Provides
    @ContactDetailsScope
    public ContactListInteractor provideContactListInteractor(ContactListRepository contactListRepository){
        return new ContactListModel(contactListRepository);
    }

}
