package com.a65apps.library.ui.fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a65apps.library.Constants;
import com.a65apps.library.R;
import com.a65apps.library.di.containers.HasAppContainer;
import com.a65apps.library.di.containers.PersonListContainer;
import com.a65apps.library.models.PersonModelCompact;
import com.a65apps.library.presenters.PersonListPresenter;
import com.a65apps.library.ui.adapters.PersonListAdapter;
import com.a65apps.library.ui.decorations.PersonDecoration;
import com.a65apps.library.ui.listeners.EventActionBarListener;
import com.a65apps.library.ui.listeners.OnPersonClickedListener;
import com.a65apps.library.views.PersonListView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

public class PersonListFragment extends MvpAppCompatFragment implements PersonListView {

    final String LOG_TAG = "contact_list_fragment";

    String searchQuery;

    @Nullable
    RecyclerView recyclerViewPersonList;

    @Nullable
    ProgressBar progressBar;

    @Nullable
    PersonListAdapter personListAdapter;

    @Nullable
    OnPersonClickedListener onPersonClickedListener;

    @Nullable
    EventActionBarListener eventActionBarListener;

    @Inject
    public Provider<PersonListPresenter> contactListPresenterProvider;

    @InjectPresenter
    PersonListPresenter contactListPresenter;

    @ProvidePresenter
    PersonListPresenter providerContactListPresenter() {
        return contactListPresenterProvider.get();
    }

    @Override
    public void onAttach(@Nullable Context context) {
        Log.d(LOG_TAG, "onAttach");
        super.onAttach(context);
        Application app = requireActivity().getApplication();
        if (!(app instanceof HasAppContainer)){
            throw new IllegalStateException();
        }
        PersonListContainer contactListComponent = ((HasAppContainer)app).appContainer()
                .plusPersonListComponent();
        contactListComponent.inject(this);
        if (context instanceof OnPersonClickedListener) {
            onPersonClickedListener = (OnPersonClickedListener) context;
            Log.d(LOG_TAG, "onAttach - OnPersonClickedListener binding");
        }
        if (context instanceof EventActionBarListener) {
            eventActionBarListener = (EventActionBarListener) context;
            Log.d(LOG_TAG, "onAttach - EventActionBarListener binding");
        }
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
        progressBar = view.findViewById(R.id.progressbar_load_persons);
        recyclerViewPersonList = view.findViewById(R.id.rv_person_list);
        recyclerViewPersonList.addItemDecoration(new PersonDecoration(convertDpToPixels(Constants.PERSON_LIST_DECORATION_PADDING_DP)));
        personListAdapter = new PersonListAdapter(onPersonClickedListener);
        recyclerViewPersonList.setAdapter(personListAdapter);
        recyclerViewPersonList.setLayoutManager(new LinearLayoutManager(getActivity()));
        requireActivity().setTitle(getString(R.string.toolbar_header_person_list));
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(Constants.KEY_SEARCH_QUERY_TEXT, "");
        }
        contactListPresenter.requestContactList(searchQuery == null ? "" : searchQuery);
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
                contactListPresenter.requestContactList(query == null ? "" : query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactListPresenter.requestContactList(newText == null ? "" : newText);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.KEY_SEARCH_QUERY_TEXT, searchQuery);
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onDestroyView() {
        Log.d(LOG_TAG, "onDestroyView");
        recyclerViewPersonList = null;
        progressBar = null;
        personListAdapter = null;
        searchQuery = null;
        super.onDestroyView();
    }

    @Override
    public void fetchContactList(List<PersonModelCompact> personList) {
        if (personList != null && personListAdapter != null) {
            Log.d(LOG_TAG, "Создаем список контактов " + personList.size());
            personListAdapter.setItems(personList);
        }
    }

    @Override
    public void fetchError(String errorMessage) {
        Toast.makeText(getActivity().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
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


