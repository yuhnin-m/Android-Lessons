package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ContactDetailsFragment extends Fragment {
    static final String ARG_PARAM_PERSON_ID = "PERSON_ID";
    PersonModel person;
    List<ContactInfoModel> contactInfoList;

    ImageView ivAvatar;
    TextView tvFullname;
    ListView lvContacts;
    TextView tvDescription;

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
        tvDescription = view.findViewById(R.id.tv_person_description);
        updateFields();
        return view;
    }


    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).showToolbarbackButton(true);
        ((MainActivity) requireActivity()).setToolbarText(getString(R.string.toolbar_header_person_details));
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



    private PersonModel getPersonModel(long personId) {
        return ContactRepositoryFakeImp.getInstance().getPersonById(personId);
    }

    private List<ContactInfoModel> getContactInfoList(long personId) {
        return ContactRepositoryFakeImp.getInstance().getContactByPerson(personId);
    }

    private void updateFields() {
        ivAvatar.setImageResource(person.getImageResource());
        tvFullname.setText(person.getFullName());
        tvDescription.setText(person.getDescription());

        ContactListAdapter contactListAdapter = new ContactListAdapter(getContext(), contactInfoList);
        lvContacts.setAdapter(contactListAdapter);
    }
}
