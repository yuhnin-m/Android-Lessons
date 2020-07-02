package com.a65apps.yuhnin.lesson1.di.app;

import com.a65apps.yuhnin.lesson1.di.contactdetails.ContactDetailsComponent;
import com.a65apps.yuhnin.lesson1.di.contactlist.ContactListComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (modules = {AppModule.class, RepositoryModule.class})
public interface AppComponent {
    ContactDetailsComponent plusContactDetailsComponent();
    ContactListComponent plusContactListComponent();
}
