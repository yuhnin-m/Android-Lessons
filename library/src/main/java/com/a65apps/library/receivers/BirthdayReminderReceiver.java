package com.a65apps.library.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.a65apps.core.interactors.contacts.ContactListInteractor;
import com.a65apps.library.Constants;

import javax.inject.Inject;

public class BirthdayReminderReceiver extends BroadcastReceiver {
    final String LOG_TAG = "alarm_reciever";

    @Inject
    ContactListInteractor contactDetailsInteractor;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "BirthdayReminderReceiver - НАПОМИНАНИЕ ПОЛУЧЕНО");
        String personId = intent.getStringExtra(Constants.KEY_PERSON_ID);
        String text = intent.getStringExtra(Constants.KEY_NOTIFICATION_TEXT);
    }
}