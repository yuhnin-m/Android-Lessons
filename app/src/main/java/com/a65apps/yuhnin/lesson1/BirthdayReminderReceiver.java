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
import java.util.Date;
import java.util.GregorianCalendar;
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
        // Выводим уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.text_birthday))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(intent.getIntExtra("KEY_ID", -1), builder.build());

        // Добавляем новое напоминание о ДР через год
        long millisToRemind = 0;
        try {
            millisToRemind = createMillisToRemind(intent.getStringExtra("KEY_BIRTHDAY"));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Невозможно распарсить дату рождения " + e.getMessage());
            return;
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, person_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, millisToRemind, alarmIntent);
    }


    private long createMillisToRemind(String date) throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(date));
        } catch (ParseException e) {

        }

        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.YEAR, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if ((calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) &&
                (calendar.get(Calendar.DAY_OF_MONTH) == 29)) {
            if (!((GregorianCalendar) GregorianCalendar.getInstance()).isLeapYear(calendar.get(Calendar.YEAR))) {
                calendar.set(Calendar.DAY_OF_MONTH, 28);
            }
        }

        return calendar.getTimeInMillis();
    }
}
