package com.a65apps.library.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor;
import com.a65apps.library.Constants;
import com.a65apps.library.R;
import com.a65apps.library.di.containers.BirthdayReminderContainer;
import com.a65apps.library.di.containers.HasAppContainer;
import com.a65apps.library.ui.activities.MainActivity;

import javax.inject.Inject;

public class BirthdayReminderReceiver extends BroadcastReceiver {
    final String LOG_TAG = "alarm_reciever";

    @Inject
    BirthdayReminderInteractor reminderInteractor;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "BirthdayReminderReceiver - НАПОМИНАНИЕ ПОЛУЧЕНО");
        BirthdayReminderContainer birthdayNotificationComponent =
                ((HasAppContainer) context.getApplicationContext()).appContainer().plusBirthdayReminderComponent();
        birthdayNotificationComponent.inject(this);

        String personId = intent.getStringExtra(Constants.KEY_PERSON_ID);
        String fullName = intent.getStringExtra(Constants.KEY_FULL_NAME);
        String notificationString = intent.getStringExtra(Constants.KEY_NOTIFICATION_TEXT);
        String birthDayString = intent.getStringExtra(Constants.KEY_BIRTHDAY);
        Log.d(LOG_TAG, "Отправляем уведомление");
        createNotification(context, personId, notificationString);
        Log.d(LOG_TAG, "Переустанавливаем напоминалку");
        reminderInteractor.setBirthdayReminder(personId, fullName, birthDayString);
    }


    /**
     * Метод отправки уведомления пользователю
     * @param context контект
     * @param personId идентификатор контакта
     * @param notificationString текст уведомления
     */
    private void createNotification(Context context, String personId, String notificationString) {
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.putExtra(Constants.KEY_PERSON_ID, personId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIF_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.text_birthday))
                .setContentText(notificationString)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFY_ID, builder.build());
    }
}