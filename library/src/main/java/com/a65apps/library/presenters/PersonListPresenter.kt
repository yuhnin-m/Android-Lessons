package com.a65apps.library.presenters

import android.util.Log
import com.a65apps.core.interactors.persons.PersonListInteractor
import com.a65apps.library.mapper.PersonModelCompactDataMapper
import com.a65apps.library.views.PersonListView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val LOG_TAG = "person_presenter"

@InjectViewState
class PersonListPresenter(
        private val personListInteractor: PersonListInteractor,
        private val dataMapper: PersonModelCompactDataMapper) : MvpPresenter<PersonListView>() {
    private val handler = CoroutineExceptionHandler { _, exception ->
        exception.message?.let {
            viewState.fetchError(it)
        }
        viewState.hideProgressBar()
    }
    private val job = SupervisorJob()
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job + handler)
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
            val listPersons = personListInteractor.loadAllPersons(searchString)
            viewState.fetchContactList(dataMapper.transform(listPersons))
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
