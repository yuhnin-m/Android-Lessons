package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.a65apps.yuhnin.lesson1.BirthdayReminderReceiver;
import com.a65apps.yuhnin.lesson1.Constants;
import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.app.AppDelegate;
import com.a65apps.yuhnin.lesson1.di.contactdetails.ContactDetailsComponent;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.presenters.ContactDetailsPresenter;
import com.a65apps.yuhnin.lesson1.ui.adapters.ContactListAdapter;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventActionBarListener;
import com.a65apps.yuhnin.lesson1.views.ContactDetailsView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

public class ContactDetailsFragment extends MvpAppCompatFragment
        implements ContactDetailsView, CompoundButton.OnCheckedChangeListener {
    final String LOG_TAG = "details_fragment";
    @Nullable
    ImageView ivAvatar;
    @Nullable
    TextView tvFullname;
    @Nullable
    ListView lvContacts;
    @Nullable
    TextView tvDescription;
    @Nullable
    TextView tvBirthday;
    @Nullable
    ToggleButton toggleBtnRemindBirthday;
    @Nullable
    ProgressBar progressBar;

    @NonNull
    PersonModelAdvanced person;

    @Nullable
    AlarmManager alarmManager;

    @Nullable
    PendingIntent alarmIntent;

    String personId = "";

    @Nullable
    List<ContactInfoModel> contactInfoList;

    @Nullable
    EventActionBarListener eventActionBarListener;

    @InjectPresenter
    ContactDetailsPresenter contactDetailsPresenter;

    @Inject
    public Provider<ContactDetailsPresenter> detailsPresenterProvider;

    @ProvidePresenter
    ContactDetailsPresenter providerContactDetailsPresenter(){
        return detailsPresenterProvider.get();
    }

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@Nullable Context context) {
        if (context instanceof EventActionBarListener) {
            eventActionBarListener = (EventActionBarListener) context;
        }
        AppDelegate appDelegate = (AppDelegate) getActivity().getApplication();
        ContactDetailsComponent contactDetailsComponent = appDelegate.getAppComponent()
                .plusContactDetailsComponent();
        contactDetailsComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        eventActionBarListener = null;
        super.onDetach();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.personId = getArguments().getString(Constants.KEY_PERSON_ID);
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
        progressBar = view.findViewById(R.id.progressbar_load_details);
        toggleBtnRemindBirthday.setOnCheckedChangeListener(this);
        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        return view;
    }


    @Override
    public void onResume() {
        if (eventActionBarListener != null) {
            eventActionBarListener.setVisibleToolBarBackButton(true);
        }
        contactDetailsPresenter.requestContactsByPerson(personId);
        requireActivity().setTitle(getString(R.string.toolbar_header_person_details));
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        ivAvatar = null;
        tvFullname = null;
        lvContacts = null;
        tvDescription = null;
        progressBar = null;
        super.onDestroyView();
    }

    private void updateFields() {
        if (person == null) {
            Log.e(LOG_TAG, "Невозможно отобразить данные контакта person=null");
            return;
        }
        if (toggleBtnRemindBirthday != null) {
            toggleBtnRemindBirthday.setEnabled(person.getDateBirthday() != null);
            boolean reminderEnabled = checkBirthdayReminder();
            toggleBtnRemindBirthday.setChecked(reminderEnabled);
            toggleBtnRemindBirthday.setText(R.string.button_text_remind_birthday_on);
            Log.d(LOG_TAG,"Напоминание " + (reminderEnabled ? " включено": " выключено"));
        }
        if (ivAvatar != null) {
            ivAvatar.setImageURI(person.getImageUri());
        }
        if (tvFullname != null) {
            tvFullname.setText(person.getFullName());
        }
        if (tvDescription != null) {
            tvDescription.setText(person.getDescription());
        }
        if (tvBirthday != null) {
            String birthday = person.getStringBirthday().isEmpty() ? getString(R.string.text_birthday_notset) : person.getStringBirthday();
            tvBirthday.setText(String.format(getString(R.string.text_birthday_date), birthday));
        }
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

    private long createMillisToRemind(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
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

    private void setBirthdayReminderEnabled(boolean enabled) {
        if (person.getDateBirthday() != null) {
            Intent intent = new Intent(getContext(), BirthdayReminderReceiver.class);
            if (enabled) {
                Log.d(LOG_TAG, "Create birthday reminder");
                intent.putExtra("KEY_ID", person.getId());
                intent.putExtra("KEY_BIRTHDAY", person.getStringBirthday());
                intent.putExtra("KEY_TEXT", String.format(getString(R.string.text_remind_birthday), person.getFullName()));
                alarmIntent = PendingIntent.getBroadcast(getContext(), person.getId().hashCode(),
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                long millisToRemind = createMillisToRemind(person.getDateBirthday());
                alarmManager.set(AlarmManager.RTC_WAKEUP, millisToRemind, alarmIntent);
            } else {
                if (alarmManager != null) {
                    Log.d(LOG_TAG, "Remove birthday reminder");
                    alarmIntent = PendingIntent.getBroadcast(getActivity(), person.getId().hashCode(),
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(alarmIntent);
                    alarmIntent.cancel();
                }
            }
        }
    }

    private boolean checkBirthdayReminder() {
        return (PendingIntent.getBroadcast(getActivity(), person.getId().hashCode(),
                new Intent(getActivity(), BirthdayReminderReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }

    @Override
    public void fetchContactDetails(PersonModelAdvanced personModel) {
        this.person = personModel;
        if (person != null) {
            Log.d(LOG_TAG, "Создаем список контактных данных контакта " + person.getFullName());
            updateFields();
        } else {
            Log.e(LOG_TAG, "onFetchPersonModel - репозиторий вернул personModels=null");
        }
    }

    @Override
    public void fetchContactsInfo(List<ContactInfoModel> listOfContacts) {
        this.contactInfoList = listOfContacts;
        if (lvContacts != null && contactInfoList != null) {
            ContactListAdapter contactListAdapter = new ContactListAdapter(getContext(), contactInfoList);
            lvContacts.setAdapter(contactListAdapter);
        } else {
            Log.e(LOG_TAG, "onFetchContacts - репозиторий вернул contactInfoList=null");
        }
    }

    @Override
    public void fetchError(String errorMessage) {
        Toast.makeText(getActivity().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
    }

    @Override
    public void showProgressBar() {
        Log.d(LOG_TAG, "Progressbar show");
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        Log.d(LOG_TAG, "Progressbar hide");
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }
}
