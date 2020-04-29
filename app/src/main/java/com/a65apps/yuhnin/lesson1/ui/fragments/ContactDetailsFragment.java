package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFakeImp;
import com.a65apps.yuhnin.lesson1.ui.activities.MainActivity;
import com.a65apps.yuhnin.lesson1.ui.adapters.ContactListAdapter;

import java.util.List;
import java.util.Objects;

public class ContactDetailsFragment extends Fragment {
    static ContactDetailsFragment instance;
    static final String ARG_PARAM_PERSON_ID = "PERSON_ID";
    PersonModel person;
    List<ContactInfoModel> contactInfoList;

    ImageView ivAvatar;
    TextView tvFullname;
    ListView lvContacts;
    EditText etDescription;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long personId = getArguments().getLong(ARG_PARAM_PERSON_ID);
            // TODO: перенести в отдельный поток
            person = getPersonModel(personId);
            if (person != null) {
                contactInfoList = getContactInfoList(personId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvFullname = view.findViewById(R.id.tv_fullname);
        lvContacts = view.findViewById(R.id.lv_contacts);
        etDescription = view.findViewById(R.id.et_person_description);
        updateFields();
        return view;
    }


    @Override
    public void onResume() {
        ((MainActivity) Objects.requireNonNull(getActivity())).showToolbarbackButton(true);
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarText(getString(R.string.toolbar_header_person_details));
        super.onResume();
    }


    private PersonModel getPersonModel(long personId) {
        return ContactRepositoryFakeImp.getInstance().getPersonById(personId);
    }

    private List<ContactInfoModel> getContactInfoList(long personId) {
        return ContactRepositoryFakeImp.getInstance().getContactByPerson(personId);
    }

    private void updateFields() {
        ivAvatar.setImageResource(person.getImageResource());
        tvFullname.setText(person.getFullName());
        etDescription.setText(person.getDescription());

        ContactListAdapter contactListAdapter = new ContactListAdapter(getContext(), contactInfoList);
        lvContacts.setAdapter(contactListAdapter);
    }
}
