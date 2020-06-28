package com.a65apps.yuhnin.lesson1.di.contactdetails;

import com.a65apps.yuhnin.lesson1.di.contactlist.ContactListModule;
import com.a65apps.yuhnin.lesson1.di.scopes.ContactDetailsScope;
import com.a65apps.yuhnin.lesson1.ui.fragments.ContactDetailsFragment;

import dagger.Subcomponent;

@ContactDetailsScope
@Subcomponent(modules = {ContactListModule.class})
public interface ContactDetailsComponent {
    void inject(ContactDetailsFragment contactDetailsFragment);
}
