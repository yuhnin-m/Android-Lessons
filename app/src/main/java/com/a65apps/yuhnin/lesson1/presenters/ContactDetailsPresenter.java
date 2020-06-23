package com.a65apps.yuhnin.lesson1.presenters;

import androidx.annotation.NonNull;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.views.ContactDetailsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@InjectViewState
public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    @NonNull
    final ContactRepository contactRepository;
    @NonNull
    CompositeDisposable compositeDisposable;


    public ContactDetailsPresenter(@NonNull ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void requestContactsByPerson(@NonNull String personId) {
        compositeDisposable.add(contactRepository.getContactByPerson(personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(x -> getViewState().showProgressBar())
                .subscribeWith(new DisposableSingleObserver<List<ContactInfoModel>>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<ContactInfoModel> contactInfoModels) {
                        getViewState().fetchContactsInfo(contactInfoModels);
                        getViewState().hideProgressBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().fetchError(e.getMessage());
                    }
                })
        );
    }

    public void requestPersonDetails(@NonNull String personId) {
        compositeDisposable.add(contactRepository.getPersonById(personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(x -> getViewState().showProgressBar())
                .subscribeWith(new DisposableSingleObserver<PersonModelAdvanced>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull PersonModelAdvanced personModel) {
                        getViewState().fetchContactDetails(personModel);
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
