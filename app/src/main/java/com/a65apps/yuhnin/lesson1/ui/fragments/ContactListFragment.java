package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFakeImp;
import com.a65apps.yuhnin.lesson1.ui.activities.MainActivity;
import com.a65apps.yuhnin.lesson1.ui.adapters.PersonListAdapter;

import java.util.List;
import java.util.Objects;

/**
 * Фрагмент списка контактов
 */
public class ContactListFragment extends Fragment {

    ListView listviewPersons;
    List<PersonModel> personList;
    PersonListAdapter personListAdapter;

    public ContactListFragment() {
        getPersons();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarText(getString (R.string.toolbar_header_person_list));

        personListAdapter = new PersonListAdapter(getActivity(), personList);
        listviewPersons = view.findViewById(R.id.listViewContacts);
        listviewPersons.setAdapter(personListAdapter);
        listviewPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ((MainActivity) Objects.requireNonNull(getActivity())).onPersonClicked(id);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        ((MainActivity) Objects.requireNonNull(getActivity())).showToolbarbackButton(false);
        super.onResume();
    }

    /**
     * Метод загрузки данных в список контактов
     */
    private void getPersons() {
        ContactRepository contactRepository = new ContactRepositoryFakeImp();
        personList = contactRepository.getAllPersons();
    }

}
