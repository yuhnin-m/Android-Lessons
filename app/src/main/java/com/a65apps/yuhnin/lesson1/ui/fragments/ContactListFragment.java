package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.a65apps.yuhnin.lesson1.Constants;
import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.a65apps.yuhnin.lesson1.presenters.ContactListPresenter;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFromSystem;
import com.a65apps.yuhnin.lesson1.ui.PersonDecoration;
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
    String searchQuery;
    @Nullable
    RecyclerView recyclerViewPersonList;

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
        return contactListPresenter = new ContactListPresenter(ContactRepositoryFromSystem.getInstance(getActivity().getApplicationContext()));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        recyclerViewPersonList = view.findViewById(R.id.rv_person_list);
        recyclerViewPersonList.addItemDecoration(new PersonDecoration(convertDpToPixels(Constants.PERSON_LIST_DECORATION_PADDING_DP)));
        personListAdapter = new PersonListAdapter(onPersonClickedListener);
        recyclerViewPersonList.setAdapter(personListAdapter);
        recyclerViewPersonList.setLayoutManager(new LinearLayoutManager(getActivity()));
        requireActivity().setTitle(getString(R.string.toolbar_header_person_list));
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(Constants.KEY_SEARCH_QUERY_TEXT, "");
        }
        contactListPresenter.requestContactList(searchQuery);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.appSearchBar);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.action_search));
        searchView.setQuery(searchQuery, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                contactListPresenter.requestContactList(query.isEmpty() ? "" : query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactListPresenter.requestContactList(newText.isEmpty() ? "" : newText);
                searchQuery = newText;
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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
        recyclerViewPersonList = null;
        personListAdapter = null;
        searchQuery = null;
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.KEY_SEARCH_QUERY_TEXT, searchQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void getContactList(final List<PersonModelCompact> personList) {
        if (personList != null && personListAdapter != null) {
            Log.d(LOG_TAG, "Создаем список контактов " + personList.size());
            personListAdapter.setItems(personList);
        }
    }

    /**
     * Метод необходимый для перевода из dp в пиксели
     * @param dp величина density-independent pixel
     * @return величина в пикселях
     */
    private int convertDpToPixels(int dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density);
    }
}
