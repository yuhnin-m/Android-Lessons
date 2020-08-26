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

private val LOG_TAG = "contact_list_fragment"

class PersonListFragment : MvpAppCompatFragment(), PersonListView {
    var personListAdapter: PersonListAdapter? = null
    var onPersonClickedListener: OnPersonClickedListener? = null
    var eventActionBarListener: EventActionBarListener? = null
    var searchQuery: String? = null

    companion object{
        fun  newInstance():PersonListFragment = PersonListFragment()
    }
    @Inject
    lateinit var contactListPresenterProvider: Provider<PersonListPresenter>

    @InjectPresenter
    lateinit var contactListPresenter: PersonListPresenter

    @ProvidePresenter
    fun providerContactListPresenter(): PersonListPresenter? {
        return contactListPresenterProvider.get()
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
        onPersonClickedListener = null
        eventActionBarListener = null
        super.onDetach()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        personListAdapter = PersonListAdapter(onPersonClickedListener)
        with(recyclerviewPersonList){
            adapter = personListAdapter
            setLayoutManager(LinearLayoutManager(activity))
        }
        requireActivity().title = getString(R.string.toolbar_header_person_list)
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(Constants.KEY_SEARCH_QUERY_TEXT, "")
        }
        if (searchQuery == null) "" else searchQuery?.let { contactListPresenter?.requestContactList(it) }

        contactListPresenter.requestContactList(searchQuery?:"")
        setHasOptionsMenu(true)

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val searchMenuItem = menu.findItem(R.id.appSearchBar)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.action_search)
        searchView.setQuery(searchQuery, false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query
                contactListPresenter!!.requestContactList(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                contactListPresenter!!.requestContactList(newText ?: "")
                searchQuery = newText
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(Constants.KEY_SEARCH_QUERY_TEXT, searchQuery)
        super.onSaveInstanceState(outState)
    }

    override fun fetchContactList(personList: MutableList<PersonModelCompact>?) {
        if (personList != null && personListAdapter != null) {
            personListAdapter?.let {

            }
            Log.d(LOG_TAG, "Создаем список контактов " + personList.size)
            personListAdapter!!.setItems(personList)
        }
    }

    override fun hideProgressBar() {
        progressbarLoadPersons.setVisibility(View.GONE)
    }

    override fun fetchError(errorMessage: String?) {
        Toast.makeText(activity!!.applicationContext, errorMessage, Toast.LENGTH_SHORT)
    }

    override fun showProgressBar() {
        progressbarLoadPersons.setVisibility(View.VISIBLE)
    }

    /**
     * Метод необходимый для перевода из dp в пиксели
     * @param dp величина density-independent pixel
     * @return величина в пикселях
     */
    private fun convertDpToPixels(dp: Int): Int {
        return (dp * requireContext().resources.displayMetrics.density).toInt()
    }
}