package com.a65apps.yuhnin.lesson1.di.contactlist;

import com.a65apps.yuhnin.lesson1.di.contactdetails.ContactDetailsModule;
import com.a65apps.yuhnin.lesson1.di.scopes.ContactListScope;
import com.a65apps.yuhnin.lesson1.ui.fragments.ContactListFragment;

import dagger.Subcomponent;

@ContactListScope
@Subcomponent(modules = {ContactDetailsModule.class})
public interface ContactListComponent {
    void inject(ContactListFragment contactListFragment);
}
