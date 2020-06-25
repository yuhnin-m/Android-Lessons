package com.a65apps.yuhnin.lesson1.presenters;

import androidx.annotation.NonNull;


import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.views.ContactListView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> {
    @NonNull
    final ContactRepository contactRepository;
    @NonNull
    CompositeDisposable compositeDisposable;
    @NonNull
    PublishSubject<String> publishSubject;

    public ContactListPresenter(@NonNull ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.publishSubject = PublishSubject.create();
        compositeDisposable.add(
                publishSubject.debounce(400, TimeUnit.MILLISECONDS)
                        .switchMapSingle(query -> contactRepository.getAllPersons(query).subscribeOn(Schedulers.io()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(x -> getViewState().showProgressBar())
                        .subscribe(
                                contactList -> {
                                    getViewState().fetchContactList(contactList);
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
        super.onDestroy();
    }
}
