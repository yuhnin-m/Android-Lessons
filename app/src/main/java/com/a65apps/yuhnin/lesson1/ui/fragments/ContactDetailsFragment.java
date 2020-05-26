package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.a65apps.yuhnin.lesson1.BirthdayReminderReceiver;
import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;
import com.a65apps.yuhnin.lesson1.ui.adapters.ContactListAdapter;
import com.a65apps.yuhnin.lesson1.ui.listeners.ContactsResultListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventActionBarListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventDataFetchServiceListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.PersonResultListener;

import java.util.Calendar;
import java.util.List;

public class ContactDetailsFragment extends Fragment
        implements ContactsResultListener, PersonResultListener, CompoundButton.OnCheckedChangeListener {
    static final String ARG_PARAM_PERSON_ID = "PERSON_ID";
    final String LOG_TAG = "details_fragment";

    @NonNull
    PersonModel person;
    @Nullable
    private AlarmManager alarmManager;
    @Nullable
    private PendingIntent alarmIntent;

    int personId = 0;

    @Nullable
    List<ContactInfoModel> contactInfoList;

    @Nullable
    private EventActionBarListener eventActionBarListener;

    @Nullable
    private EventDataFetchServiceListener eventDataFetchServiceListener;

    ImageView ivAvatar;
    TextView tvFullname;
    ListView lvContacts;
    TextView tvDescription;
    TextView tvBirthday;
    ToggleButton toggleBtnRemindBirthday;


    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@Nullable Context context) {
        if (context instanceof EventActionBarListener) {
            eventActionBarListener = (EventActionBarListener) context;
        }
        if (context instanceof EventDataFetchServiceListener) {
            eventDataFetchServiceListener = (EventDataFetchServiceListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        eventActionBarListener = null;
        eventDataFetchServiceListener = null;
        super.onDetach();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.personId = getArguments().getInt(ARG_PARAM_PERSON_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvFullname = view.findViewById(R.id.tv_fullname);
        lvContacts = view.findViewById(R.id.lv_contacts);
        tvDescription = view.findViewById(R.id.tv_person_description);
        tvBirthday = view.findViewById(R.id.tv_birthday);
        toggleBtnRemindBirthday = view.findViewById(R.id.togglebtn_remind_birthday);
        toggleBtnRemindBirthday.setOnCheckedChangeListener(this);
        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

        // Запрашиваем данные из сервиса
        if (eventDataFetchServiceListener != null) {
            eventDataFetchServiceListener.getPersonById(personId, this);
            eventDataFetchServiceListener.getContactsByPerson(personId, this);
        }

        return view;
    }


    @Override
    public void onResume() {
        if (eventActionBarListener != null) {
            eventActionBarListener.setVisibleToolBarBackButton(true);
        }
        requireActivity().setTitle(getString(R.string.toolbar_header_person_details));
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        ivAvatar = null;
        tvFullname = null;
        lvContacts = null;
        tvDescription = null;
        super.onDestroyView();
    }



    private void updateFields() {
        ivAvatar.setImageResource(person.getImageResource());
        tvFullname.setText(person.getFullName());
        tvDescription.setText(person.getDescription());
        tvBirthday.setText(String.format(getString(R.string.text_birthday_date), person.getStringBirthday()));
        toggleBtnRemindBirthday.setText(R.string.button_text_remind_birthday_on);
    }


    @Override
    public void onFetchContacts(List<ContactInfoModel> contactInfoList) {
        this.contactInfoList = contactInfoList;
        ContactListAdapter contactListAdapter = new ContactListAdapter(getContext(), contactInfoList);
        lvContacts.setAdapter(contactListAdapter);
    }

    @Override
    public void onFetchPersonModel(PersonModel personModels) {
        this.person = personModels;
        toggleBtnRemindBirthday.setEnabled(person.getDateBirthday() != null);
        if (alarmManager != null && toggleBtnRemindBirthday != null) {
            boolean reminderEnabled = checkBirthdayReminder();
            Log.d(LOG_TAG,"Напоминание " + (reminderEnabled ? " включено": " выключено"));
            toggleBtnRemindBirthday.setChecked(reminderEnabled);
        }
        updateFields();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Log.d(LOG_TAG, "Toggle checked");
        } else {
            Log.d(LOG_TAG, "Toggle uncheked");
        }

        setBirthdayReminderEnabled(isChecked);
    }


    private void setBirthdayReminderEnabled(boolean enabled) {
        if (person.getDateBirthday() != null) {
            Intent intent = new Intent(getContext(), BirthdayReminderReceiver.class);
            if (enabled) {
                Log.d(LOG_TAG, "Create birthday reminder");
                intent.putExtra("KEY_ID", person.getId());
                intent.putExtra("KEY_BIRTHDAY", person.getStringBirthday());
                intent.putExtra("KEY_TEXT", String.format(getString(R.string.text_remind_birthday), person.getFullName()));
                alarmIntent = PendingIntent.getBroadcast(getContext(), person.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.setTime(person.getDateBirthday());
                calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                // Временный блок - для проверки
                /*
                calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, Calendar.MAY);
                calendar.set(Calendar.DAY_OF_MONTH, 26);
                calendar.set(Calendar.HOUR, 13);
                calendar.set(Calendar.MINUTE, 18);
                calendar.set(Calendar.SECOND, 0);
                */
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        alarmIntent);
            } else {
                if (alarmManager != null) {
                    Log.d(LOG_TAG, "Remove birthday reminder");
                    alarmIntent = PendingIntent.getBroadcast(getActivity(), (int) person.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(alarmIntent);
                    alarmIntent.cancel();
                }
            }
        }
    }

    private boolean checkBirthdayReminder() {
        return (PendingIntent.getBroadcast(getActivity(), (int)personId,
                new Intent(getActivity(), BirthdayReminderReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}
