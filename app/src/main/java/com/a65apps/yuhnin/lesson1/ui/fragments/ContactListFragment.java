package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.a65apps.yuhnin.lesson1.ui.listeners.EventActionBarListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.OnPersonClickedListener;

import java.util.List;
import java.util.Objects;

/**
 * Фрагмент списка контактов
 */
// parent activity will implement this method to respond to click events



public class ContactListFragment extends Fragment {

    ListView listviewPersons;
    List<PersonModel> personList;
    PersonListAdapter personListAdapter;

    @Nullable
    private OnPersonClickedListener onPersonClickedListener;

    @Nullable
    private EventActionBarListener eventActionBarListener;

    @Override
    public void onAttach(@Nullable Context context) {
        if (context instanceof OnPersonClickedListener) {
            onPersonClickedListener = (OnPersonClickedListener) context;
        }
        if (context instanceof EventActionBarListener) {
            eventActionBarListener = (EventActionBarListener) context;
        }
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        onPersonClickedListener = null;
        eventActionBarListener = null;
        super.onDetach();
    }


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
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        requireActivity().setTitle(getString(R.string.toolbar_header_person_list));
        personListAdapter = new PersonListAdapter(getActivity(), personList);
        listviewPersons = view.findViewById(R.id.listViewContacts);
        listviewPersons.setAdapter(personListAdapter);
        listviewPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (onPersonClickedListener != null) {
                    onPersonClickedListener.onItemClick(id);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        if (eventActionBarListener != null) {
            eventActionBarListener.setVisibleToolBarBackButton(false);
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        listviewPersons = null;
        personListAdapter = null;
        super.onDestroyView();
    }

    /**
     * Метод загрузки данных в список контактов
     */
    private void getPersons() {
        ContactRepository contactRepository = new ContactRepositoryFakeImp();
        personList = contactRepository.getAllPersons();
    }

}
