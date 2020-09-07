package com.a65apps.library.presenters

import android.util.Log
import com.a65apps.core.interactors.persons.PersonListInteractor
import com.a65apps.library.mapper.PersonModelCompactDataMapper
import com.a65apps.library.views.PersonListView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

private const val LOG_TAG = "person_presenter"

@InjectViewState
class PersonListPresenter(private val personListInteractor: PersonListInteractor) : MvpPresenter<PersonListView>() {
    private val job = SupervisorJob()
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)
    private var dataMapper: PersonModelCompactDataMapper = PersonModelCompactDataMapper()
    private val broadcastChannel = BroadcastChannel<String>(Channel.CONFLATED)

    init {
        scope.launch {
            broadcastChannel.asFlow()
                    .debounce(400)
                    .onEach {
                        withContext(Dispatchers.Main) { viewState.showProgressBar() }
                    }
                    .flatMapLatest {
                        Log.d(LOG_TAG, "Запрашиваем данные из репозитория $it")
                        personListInteractor.loadAllPersonsFlow(it)
                    }.flowOn(Dispatchers.IO)
                    .distinctUntilChanged()
                    .collect { list ->
                        Log.d(LOG_TAG, "Обновление списка ${list.size}")
                        viewState.fetchContactList(dataMapper.transform(list))
                        viewState.hideProgressBar()
                        Log.d(LOG_TAG, "Скрыли прогрессбар")
                    }
        }
    }

    private fun loadPersonList(searchString: String) {
        scope.launch {
            viewState.showProgressBar()
            try {
                val listPersons = personListInteractor.loadAllPersons(searchString)
                viewState.fetchContactList(dataMapper.transform(listPersons))
            } catch (e: Exception) {
                Log.d(LOG_TAG, "Error retrieve list of person: " + e.message)
                e.message?.let { viewState.fetchError(it) }
            } finally {
                viewState.hideProgressBar()
            }
        }
    }

    fun requestContactList(searchString: String) {
        Log.d(LOG_TAG, "Выполняем поиск по $searchString")
        broadcastChannel.offer(searchString)
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "Flow завершен")
        broadcastChannel.close()
        scope.cancel()
        super.onDestroy()
    }
}