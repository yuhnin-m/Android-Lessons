package com.a65apps.library.presenters

import android.util.Log
import com.a65apps.core.entities.Person
import com.a65apps.core.interactors.persons.PersonListInteractor
import com.a65apps.library.mapper.PersonModelCompactDataMapper
import com.a65apps.library.views.PersonListView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*

private const val LOG_TAG = "person_presenter"
@InjectViewState
class PersonListPresenter(val personListInteractor: PersonListInteractor) : MvpPresenter<PersonListView>() {
    private val job = SupervisorJob()
    private var scope: CoroutineScope? = CoroutineScope(Dispatchers.Main + job)

    private var dataMapper: PersonModelCompactDataMapper = PersonModelCompactDataMapper()

    private fun loadPersonList(searchString: String) {
        scope?.launch {
            viewState.showProgressBar()
            try {
                var listPersons: List<Person>? = null
                withContext(Dispatchers.IO) {
                    listPersons = personListInteractor.loadAllPersons(searchString)
                }
                listPersons?.let {
                    viewState.fetchContactList(dataMapper.transform(it))
                }
            } catch (e: Exception) {
                Log.d(LOG_TAG, "Error retrieve list of person: " + e.message)
                e.message?.let { viewState.fetchError(it) }
            } finally {
                viewState.hideProgressBar()
            }
        }
    }


    fun requestContactList(searchString: String) {
        loadPersonList(searchString)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}