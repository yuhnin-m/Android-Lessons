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
import android.widget.ListView;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.a65apps.yuhnin.lesson1.ui.adapters.PersonListAdapter;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventActionBarListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventDataFetchServiceListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.OnPersonClickedListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.PersonListResultListener;

import java.util.List;

/**
 * Фрагмент списка контактов
 */
// parent activity will implement this method to respond to click events



public class ContactListFragment extends Fragment implements PersonListResultListener {

    final String LOG_TAG = "contact_list_fragment";
    ListView listviewPersons;

    PersonListAdapter personListAdapter;

    @Nullable
    private OnPersonClickedListener onPersonClickedListener;

    @Nullable
    private EventActionBarListener eventActionBarListener;

    @Nullable
    EventDataFetchServiceListener eventDataFetchServiceListener;

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
        if (context instanceof EventDataFetchServiceListener) {
            eventDataFetchServiceListener = (EventDataFetchServiceListener) context;
            Log.d(LOG_TAG, "onAttach - EventDataFetchServiceListener binding");
        }
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        Log.d(LOG_TAG, "onDetach");
        onPersonClickedListener = null;
        eventActionBarListener = null;
        eventDataFetchServiceListener = null;
        super.onDetach();
    }

    public ContactListFragment() {

    }

    public void serviceBinded() {
        if (eventDataFetchServiceListener != null) {
            Log.d(LOG_TAG, "onCreateView - запрашиваем getPersonList");
            eventDataFetchServiceListener.getPersonList(this);
        }
    }

    private void createPersonsListView(final List<PersonModelCompact> personList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (personList != null) {
                    Log.d(LOG_TAG, "Создаем список контактов " + personList.size());
                    personListAdapter = new PersonListAdapter(getActivity(), personList);
                    listviewPersons.setAdapter(personListAdapter);
                }
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
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
                    onPersonClickedListener.onItemClick(id);
                }
            }
        });
        requireActivity().setTitle(getString(R.string.toolbar_header_person_list));
        return view;
    }


    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if (eventActionBarListener != null) {
            eventActionBarListener.setVisibleToolBarBackButton(false);
        }
        if (eventDataFetchServiceListener != null) {
            Log.d(LOG_TAG, "onCreateView - запрашиваем getPersonList");
            eventDataFetchServiceListener.getPersonList(this);
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
    public void onFetchPersonList(List<PersonModelCompact> personList) {
        Log.d(LOG_TAG, "ПОЛУЧЕНЫ ДАННЫЕ СПИСКА КОНТАКТОВ");
        createPersonsListView(personList);
    }
}
