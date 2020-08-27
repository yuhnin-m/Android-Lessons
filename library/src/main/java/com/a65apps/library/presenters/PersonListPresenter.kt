package com.a65apps.library.presenters

import com.a65apps.core.interactors.persons.PersonListInteractor
import com.a65apps.library.mapper.PersonModelCompactDataMapper
import com.a65apps.library.views.PersonListView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

@InjectViewState
class PersonListPresenter(val personListInteractor: PersonListInteractor) : MvpPresenter<PersonListView>() {
    private var dataMapper: PersonModelCompactDataMapper = PersonModelCompactDataMapper()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable();
    private val publishSubject: PublishSubject<String> = PublishSubject.create();

    init {
        compositeDisposable.add(
                publishSubject.debounce(400, TimeUnit.MILLISECONDS, Schedulers.io())
                        .switchMapSingle { personListInteractor.loadAllPersons(it) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { viewState.showProgressBar() }
                        .subscribe(
                                {

                                    viewState.fetchContactList(dataMapper.transform(it))
                                    viewState.hideProgressBar()
                                }
                        )
                        { throwable ->
                            throwable.message?.let {
                                viewState.fetchError(it)
                            }
                            viewState.hideProgressBar()
                        }
        )
    }

    fun requestContactList(searchString: String) {
        publishSubject.onNext(searchString)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}