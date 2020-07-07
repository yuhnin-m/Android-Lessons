package com.a65apps.library.presenters;

import androidx.annotation.NonNull;


import com.a65apps.core.interactors.persons.PersonListInteractor;
import com.a65apps.library.mapper.PersonModelCompactDataMapper;
import com.a65apps.library.views.PersonListView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

@InjectViewState
public class PersonListPresenter extends MvpPresenter<PersonListView> {
    @NonNull
    private final PersonListInteractor personListInteractor;

    @NonNull
    private PersonModelCompactDataMapper dataMapper;

    @NonNull
    private CompositeDisposable compositeDisposable;

    @NonNull
    private PublishSubject<String> publishSubject;

    public PersonListPresenter(@NonNull PersonListInteractor personListInteractor) {
        this.personListInteractor = personListInteractor;
        this.dataMapper = new PersonModelCompactDataMapper();
        this.compositeDisposable = new CompositeDisposable();
        this.publishSubject = PublishSubject.create();
        compositeDisposable.add(
                publishSubject.debounce(400, TimeUnit.MILLISECONDS, Schedulers.io())
                        .switchMapSingle(searchString -> personListInteractor.loadAllPersons(searchString))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(x -> getViewState().showProgressBar())
                        .subscribe(
                                contactList -> {

                                    getViewState().fetchContactList(dataMapper.transform(contactList));
                                    getViewState().hideProgressBar();
                                },
                                e -> {
                                    getViewState().fetchError(e.getMessage());
                                    getViewState().hideProgressBar();
                                }
                        )
        );
    }

    public void requestContactList(@NonNull String searchString) {
        publishSubject.onNext(searchString);
    }

    @Override
    public void onDestroy() {
        this.compositeDisposable.dispose();
        this.dataMapper = null;
        super.onDestroy();
    }
}
