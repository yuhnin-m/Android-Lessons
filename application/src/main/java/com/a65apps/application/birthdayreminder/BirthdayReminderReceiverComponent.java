package com.a65apps.application.birthdayreminder;

import com.a65apps.application.scopes.BirthdayReminderScope;
import com.a65apps.library.di.containers.BirthdayReminderContainer;

import dagger.Subcomponent;

@BirthdayReminderScope
@Subcomponent(modules = {BirthdayReminderReceiverModule.class})
public interface BirthdayReminderReceiverComponent extends BirthdayReminderContainer {
}
