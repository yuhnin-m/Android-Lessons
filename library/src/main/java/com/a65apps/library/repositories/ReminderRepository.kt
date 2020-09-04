package com.a65apps.library.repositories

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.a65apps.core.interactors.reminders.BirthdayReminderRepository
import com.a65apps.library.Constants
import com.a65apps.library.R
import com.a65apps.library.receivers.BirthdayReminderReceiver
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val LOG_TAG = "lib_reminder_repository"
class ReminderRepository(private val context: Context) : BirthdayReminderRepository {
    private var alarmManager: AlarmManager? = null

    init {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d(LOG_TAG, "ReminderRepository создан")
    }

    override fun enableBirthdayReminder(personId: String, fullName: String, date: String): Boolean {
        Log.d(LOG_TAG, "Включение напоминания для контакта $fullName")
        val intent = Intent(context, BirthdayReminderReceiver::class.java)
        intent.putExtra(Constants.KEY_PERSON_ID, personId)
        intent.putExtra(Constants.KEY_BIRTHDAY, date)
        intent.putExtra(Constants.KEY_FULL_NAME, fullName)
        intent.putExtra(Constants.KEY_NOTIFICATION_TEXT,
                String.format(context.getString(R.string.text_remind_birthday), fullName))
        val alarmIntent = PendingIntent.getBroadcast(context, personId.hashCode(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var millisToRemind = 0L
        try {
            millisToRemind = createMillisToRemind(date)
        } catch (e: ParseException) {
            Log.e(LOG_TAG, "Невозможно распарсить дату: $date. Ошибка: $e")
            return false
        }
        alarmManager?.let {
            it[AlarmManager.RTC_WAKEUP, millisToRemind] = alarmIntent
            Log.d(LOG_TAG, "Напоминание для контакта $fullName установлено")
        }
        return true
    }

    override fun disableBirthdayReminder(personId: String): Boolean {
        Log.d(LOG_TAG, "Выключение напоминания для контакта $personId")
        val intent = Intent(context, BirthdayReminderReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, personId.hashCode(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager?.cancel(alarmIntent)
        alarmIntent.cancel()
        return true
    }

    override fun isBirthdayReminderEnabled(personId: String): Boolean {
        return PendingIntent.getBroadcast(context.applicationContext, personId.hashCode(),
                Intent(context, BirthdayReminderReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE) != null
    }


    /**
     * Метод возвращает количество миллисекунд
     * @param date строка, содержащая дату в формате
     * @see Constants.DATE_STRING_FORMAT
     * @return дата в миллисекундах
     * @throws ParseException исключение о невозможности распарсить дату
     */
    private fun createMillisToRemind(date: String): Long {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = SimpleDateFormat(Constants.DATE_STRING_FORMAT, Locale.ENGLISH).parse(date)
        calendar[Calendar.YEAR] = Calendar.getInstance()[Calendar.YEAR]
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.YEAR, 1)
        }
        calendar[Calendar.HOUR_OF_DAY] = 12
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        if (calendar[Calendar.MONTH] == Calendar.FEBRUARY &&
                calendar[Calendar.DAY_OF_MONTH] == 29) {
            if (!(GregorianCalendar.getInstance() as GregorianCalendar).isLeapYear(calendar[Calendar.YEAR])) {
                calendar[Calendar.DAY_OF_MONTH] = 28
            }
        }
        return calendar.timeInMillis
    }

    /**
     * Метод конвертации из GregorianCalendar в строку в формате описанном в
     * @see Constants.DATE_STRING_FORMAT
     * @param calendar экземпляр GregorianCalendar
     * @return строкас датой
     */
    private fun calendarToStringDate(calendar: GregorianCalendar): String {
        val fmt = SimpleDateFormat(Constants.DATE_STRING_FORMAT)
        return fmt.format(calendar)
    }
}