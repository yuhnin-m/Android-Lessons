package com.a65apps.yuhnin.lesson1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.a65apps.yuhnin.lesson1.ui.activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BirthdayReminderReceiver extends BroadcastReceiver {
    final String LOG_TAG = "alarm_reciever";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "BirthdayReminderReceiver - НАПОМИНАНИЕ ПОЛУЧЕНО");
        String text = intent.getStringExtra("KEY_TEXT");
        int person_id = intent.getIntExtra("KEY_ID", -1);
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.putExtra("KEY_PERSON_ID", person_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.text_birthday))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(intent.getIntExtra("KEY_ID", -1), builder.build());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        try {
            calendar.setTime(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(intent.getStringExtra("birthDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.add(Calendar.YEAR, 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, person_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }
}
