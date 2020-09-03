package com.a65apps.library.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.a65apps.library.Constants
import com.a65apps.library.R
import com.a65apps.library.di.containers.HasAppContainer
import com.a65apps.library.models.PersonModelCompact
import com.a65apps.library.presenters.PersonListPresenter
import com.a65apps.library.ui.adapters.PersonListAdapter
import com.a65apps.library.ui.listeners.EventActionBarListener
import com.a65apps.library.ui.listeners.OnPersonClickedListener
import com.a65apps.library.views.PersonListView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_contact_list.*
import javax.inject.Inject
import javax.inject.Provider

private const val LOG_TAG = "contact_list_fragment"

class PersonListFragment : MvpAppCompatFragment(), PersonListView {
    private var personListAdapter: PersonListAdapter? = null
    private var onPersonClickedListener: OnPersonClickedListener? = null
    private var eventActionBarListener: EventActionBarListener? = null
    private var searchQuery: String? = null

    companion object {
        @JvmStatic
        fun newInstance(): PersonListFragment = PersonListFragment()
    }

    @Inject
    lateinit var personListPresenterProvider: Provider<PersonListPresenter>

    @InjectPresenter
    lateinit var personListPresenter: PersonListPresenter

    @ProvidePresenter
    fun providerPersonListPresenter(): PersonListPresenter {
        return personListPresenterProvider.get()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val app = requireActivity().application
        check(app is HasAppContainer)
        val contactListComponent = (app as HasAppContainer).appContainer().plusPersonListComponent()
        contactListComponent.inject(this)
        if (context is OnPersonClickedListener) {
            onPersonClickedListener = context
            Log.d(LOG_TAG, "onAttach - OnPersonClickedListener binding")
        }
        if (context is EventActionBarListener) {
            eventActionBarListener = context
            Log.d(LOG_TAG, "onAttach - EventActionBarListener binding")
        }
    }

    override fun onDetach() {
        Log.d(LOG_TAG, "onDetach")
        eventActionBarListener = null
        onPersonClickedListener = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        personListAdapter = PersonListAdapter {
            onPersonClickedListener?.onItemClick(it)
        }
        with(recyclerviewPersonList) {
            adapter = personListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        requireActivity().title = getString(R.string.toolbar_header_person_list)
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(Constants.KEY_SEARCH_QUERY_TEXT, "")
        }
        personListPresenter.requestContactList(searchQuery ?: "")
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val searchMenuItem = menu.findItem(R.id.appSearchBar)
        val searchView = searchMenuItem.actionView as SearchView
        with(searchView) {
            queryHint = resources.getString(R.string.action_search)
            setQuery(searchQuery, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchQuery = query
                    personListPresenter.requestContactList(query ?: "")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    personListPresenter.requestContactList(newText ?: "")
                    searchQuery = newText
                    return false
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(Constants.KEY_SEARCH_QUERY_TEXT, searchQuery)
        super.onSaveInstanceState(outState)
    }

    override fun fetchContactList(personList: List<PersonModelCompact>) {
        personListAdapter?.setItems(personList)
        Log.d(LOG_TAG, "Создаем список контактов " + personList.size)
    }

    override fun fetchError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun hideProgressBar() {
        progressbarLoadPersons.isVisible = false
    }

    override fun showProgressBar() {
        progressbarLoadPersons.isVisible = true
    }
}