package com.a65apps.library.repositories;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.a65apps.core.interactors.reminders.BirthdayReminderRepository;
import com.a65apps.library.Constants;
import com.a65apps.library.R;
import com.a65apps.library.receivers.BirthdayReminderReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class ReminderRepository implements BirthdayReminderRepository {
    private final String LOG_TAG = "lib_reminder_repository";
    private final Context context;
    private final AlarmManager alarmManager;

    public ReminderRepository(@NonNull Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);;
        Log.d(LOG_TAG, "ReminderRepository создан");
    }

    @Override
    public boolean enableBirthdayReminder(@NonNull String personId, @NonNull String fullName, @NonNull String dateString) {
        Log.d(LOG_TAG, "Включение напоминания для контакта " + fullName);
        Intent intent = new Intent(context, BirthdayReminderReceiver.class);
        intent.putExtra(Constants.KEY_PERSON_ID, personId);
        intent.putExtra(Constants.KEY_BIRTHDAY, dateString);
        intent.putExtra(Constants.KEY_FULL_NAME, fullName);
        intent.putExtra(Constants.KEY_NOTIFICATION_TEXT, String.format(context.getString(R.string.text_remind_birthday), fullName));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, personId.hashCode(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long millisToRemind = 0;
        try {
            millisToRemind = createMillisToRemind(dateString);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Невозможно распарсить дату: " + dateString + ". Ошибка: " + e);
            return false;
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, millisToRemind, alarmIntent);
        Log.d(LOG_TAG, "Напоминание для контакта " + fullName + " установлено");
        return true;
    }

    @Override
    public boolean disableBirthdayReminder(String personId) {
        Log.d(LOG_TAG, "Выключение напоминания для контакта " + personId);
        Intent intent = new Intent(context, BirthdayReminderReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, personId.hashCode(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();
        return true;
    }

    @Override
    public boolean isBirthdayReminderEnabled(@NonNull String personId) {
        return (PendingIntent.getBroadcast(context.getApplicationContext(), personId.hashCode(),
                new Intent(context, BirthdayReminderReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }

    /**
     * Метод возвращает количество миллисекунд
     * @param date строка, содержащая дату в формате
     * @see Constants#DATE_STRING_FORMAT
     * @return дата в миллисекундах
     * @throws ParseException исключение о невозможности распарсить дату
     */
    private long createMillisToRemind(String date) throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new SimpleDateFormat(Constants.DATE_STRING_FORMAT, Locale.ENGLISH).parse(date));
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

    private String calendarToStringDate(@NonNull GregorianCalendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat(Constants.DATE_STRING_FORMAT);
        return fmt.format(calendar);
    }

}
