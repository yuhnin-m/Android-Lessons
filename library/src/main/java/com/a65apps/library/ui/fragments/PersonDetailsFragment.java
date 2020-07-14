package com.a65apps.library.ui.fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.library.Constants;
import com.a65apps.library.R;
import com.a65apps.library.di.containers.PersonDetailsContainer;
import com.a65apps.library.di.containers.HasAppContainer;
import com.a65apps.library.ui.adapters.ContactListAdapter;
import com.a65apps.library.ui.listeners.EventActionBarListener;
import com.a65apps.library.models.ContactModel;
import com.a65apps.library.models.PersonModelAdvanced;
import com.a65apps.library.presenters.PersonDetailsPresenter;
import com.a65apps.library.views.PersonDetailsView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

public class PersonDetailsFragment extends MvpAppCompatFragment
        implements PersonDetailsView, CompoundButton.OnCheckedChangeListener {
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

    String personId = "";

    @Nullable
    List<ContactModel> contactInfoList;

    @Nullable
    EventActionBarListener eventActionBarListener;

    @InjectPresenter
    PersonDetailsPresenter personDetailsPresenter;

    @Inject
    public Provider<PersonDetailsPresenter> detailsPresenterProvider;

    @ProvidePresenter
    PersonDetailsPresenter providerContactDetailsPresenter(){
        return detailsPresenterProvider.get();
    }

    public PersonDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@Nullable Context context) {
        if (context instanceof EventActionBarListener) {
            eventActionBarListener = (EventActionBarListener) context;
        }
        Application app = requireActivity().getApplication();
        if (!(app instanceof HasAppContainer)){
            throw new IllegalStateException();
        }
        PersonDetailsContainer contactsContainer = ((HasAppContainer)app)
                .appContainer().plusPersonDetailsComponent();
        contactsContainer.inject(this);
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
        return view;
    }


    @Override
    public void onResume() {
        if (eventActionBarListener != null) {
            eventActionBarListener.setVisibleToolBarBackButton(true);
        }
        personDetailsPresenter.requestContactsByPerson(personId);
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
            boolean reminderEnabled = personDetailsPresenter.checkBirthdayReminderEnabled(person.getId());
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
            Log.d(LOG_TAG, "Напоминания включены");
            personDetailsPresenter.birthdayReminderEnable(person);
        } else {
            Log.d(LOG_TAG, "Напоминания выключены");
            personDetailsPresenter.birthdayReminderDisable(personId);
        }
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
    public void fetchContactsInfo(List<ContactModel> listOfContacts) {
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
