package com.a65apps.yuhnin.lesson1.presenters;

import androidx.annotation.NonNull;

import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.views.ContactListView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> {
    @NonNull
    final ContactRepository contactRepository;
    @NonNull
    CompositeDisposable compositeDisposable;

    public ContactListPresenter(@NonNull ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void requestContactList(String searchString) {
        compositeDisposable.add(contactRepository.getAllPersons(searchString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(x -> getViewState().showProgressBar())
                .subscribeWith(new DisposableSingleObserver<List<PersonModelCompact>>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<PersonModelCompact> personList) {
                        getViewState().fetchContactList(personList);
                        getViewState().hideProgressBar();
                    }
                    @Override
                    public void onError(Throwable e) {
                        getViewState().fetchError(e.getMessage());
                    }
                })
        );
    }

    @Override
    public void onDestroy() {
        this.compositeDisposable.dispose();
        super.onDestroy();
    }
}
