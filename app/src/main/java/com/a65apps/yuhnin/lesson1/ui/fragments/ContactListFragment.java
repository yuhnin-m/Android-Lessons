package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.a65apps.yuhnin.lesson1.presenters.ContactListPresenter;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFromSystem;
import com.a65apps.yuhnin.lesson1.ui.adapters.PersonListAdapter;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventActionBarListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.OnPersonClickedListener;
import com.a65apps.yuhnin.lesson1.views.ContactListView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

/**
 * Фрагмент списка контактов
 */
public class ContactListFragment extends MvpAppCompatFragment implements ContactListView {

    final String LOG_TAG = "contact_list_fragment";

    ListView listviewPersons;

    @Nullable
    PersonListAdapter personListAdapter;

    @Nullable
    private OnPersonClickedListener onPersonClickedListener;

    @Nullable
    private EventActionBarListener eventActionBarListener;

    @InjectPresenter
    ContactListPresenter contactListPresenter;

    @ProvidePresenter
    ContactListPresenter providerContactListPresenter(){
        return contactListPresenter = new ContactListPresenter(ContactRepositoryFromSystem.getInstance(getContext()));
    }

    @Override
    public void onAttach(@Nullable Context context) {
        Log.d(LOG_TAG, "onAttach");
        if (context instanceof OnPersonClickedListener) {
            onPersonClickedListener = (OnPersonClickedListener) context;
            Log.d(LOG_TAG, "onAttach - OnPersonClickedListener binding");
        }
        if (context instanceof EventActionBarListener) {
            eventActionBarListener = (EventActionBarListener) context;
            Log.d(LOG_TAG, "onAttach - EventActionBarListener binding");
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG, "onDetach");
        onPersonClickedListener = null;
        eventActionBarListener = null;
        super.onDetach();
    }

    public ContactListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        listviewPersons = view.findViewById(R.id.listViewContacts);
        listviewPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (onPersonClickedListener != null) {
                    String personId = ((PersonModelCompact)listviewPersons.getAdapter().getItem(position)).getId();
                    onPersonClickedListener.onItemClick(personId);
                }
            }
        });
        requireActivity().setTitle(getString(R.string.toolbar_header_person_list));
        contactListPresenter.requestContactList();
        return view;
    }


    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if (eventActionBarListener != null) {
            eventActionBarListener.setVisibleToolBarBackButton(false);
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(LOG_TAG, "onDestroyView");
        listviewPersons = null;
        personListAdapter = null;
        super.onDestroyView();
    }

    @Override
    public void getContactList(final List<PersonModelCompact> personList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (personList != null && listviewPersons != null) {
                    Log.d(LOG_TAG, "Создаем список контактов " + personList.size());
                    personListAdapter = new PersonListAdapter(getActivity(), personList);
                    listviewPersons.setAdapter(personListAdapter);
                }
            }
        });
    }

}
