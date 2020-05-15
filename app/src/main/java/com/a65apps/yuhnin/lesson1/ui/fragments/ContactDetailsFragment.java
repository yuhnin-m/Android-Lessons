package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFakeImp;
import com.a65apps.yuhnin.lesson1.services.DataFetchService;
import com.a65apps.yuhnin.lesson1.ui.activities.MainActivity;
import com.a65apps.yuhnin.lesson1.ui.adapters.ContactListAdapter;
import com.a65apps.yuhnin.lesson1.ui.listeners.ContactsResultListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventActionBarListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventDataFetchServiceListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.PersonResultListener;

import org.w3c.dom.Text;

import java.util.List;

public class ContactDetailsFragment extends Fragment
        implements ContactsResultListener, PersonResultListener {
    static final String ARG_PARAM_PERSON_ID = "PERSON_ID";

    @NonNull
    PersonModel person;

    long personId = 0;

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
    Button btnRemindBirthday;


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
            this.personId = getArguments().getLong(ARG_PARAM_PERSON_ID);
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
        btnRemindBirthday = view.findViewById(R.id.btn_remind_birthday);
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

    private void setRepeatingAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(context, 0, intent, 0);
        long interval = 60 * 1000;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

    private void setBirthdayReminder() {

    }

    private void updateFields() {
        ivAvatar.setImageResource(person.getImageResource());
        tvFullname.setText(person.getFullName());
        tvDescription.setText(person.getDescription());
        tvBirthday.setText(String.format(getString(R.string.text_birthday), person.getStringBirthday()));
        btnRemindBirthday.setText(R.string.button_text_remind_birthday_on);
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
        updateFields();
    }
}
