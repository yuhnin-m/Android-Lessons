package com.a65apps.library.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.a65apps.core.interactors.reminders.BirthdayReminderInteractor
import com.a65apps.library.Constants
import com.a65apps.library.R
import com.a65apps.library.di.containers.HasAppContainer
import com.a65apps.library.ui.activities.MainActivity
import javax.inject.Inject

private const val LOG_TAG = "alarm_receiver"

class BirthdayReminderReceiver : BroadcastReceiver() {
    @Inject
    lateinit var reminderInteractor: BirthdayReminderInteractor

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_TAG, "BirthdayReminderReceiver - НАПОМИНАНИЕ ПОЛУЧЕНО")
        val birthdayNotificationComponent = (context.applicationContext as HasAppContainer)
                .appContainer().plusBirthdayReminderComponent()
        birthdayNotificationComponent.inject(this)
        val personId = intent.getStringExtra(Constants.KEY_PERSON_ID) ?: ""
        val fullName = intent.getStringExtra(Constants.KEY_FULL_NAME) ?: ""
        val notificationString = intent.getStringExtra(Constants.KEY_NOTIFICATION_TEXT) ?: ""
        val birthDayString = intent.getStringExtra(Constants.KEY_BIRTHDAY) ?: ""
        if (personId.isNotEmpty() && notificationString.isNotEmpty()) {
            Log.d(LOG_TAG, "Отправляем уведомление")
            createNotification(context, personId, notificationString)
            Log.d(LOG_TAG, "Переустанавливаем напоминалку")
            reminderInteractor.setBirthdayReminder(personId, fullName, birthDayString)
        }
    }

    /**
     * Метод отправки уведомления пользователю
     * @param context контект
     * @param personId идентификатор контакта
     * @param notificationString текст уведомления
     */
    private fun createNotification(context: Context, personId: String, notificationString: String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.putExtra(Constants.KEY_PERSON_ID, personId)
        val pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, Constants.NOTIF_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.text_birthday))
                .setContentText(notificationString)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Constants.NOTIFY_ID, builder.build())
    }
}
