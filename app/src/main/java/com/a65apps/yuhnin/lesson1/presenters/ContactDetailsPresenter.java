package com.a65apps.yuhnin.lesson1.presenters;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.views.ContactDetailsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
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
        compositeDisposable.add(contactRepository.getPersonById(personId)
                .flatMap(person -> contactRepository.getContactByPerson(personId)
                        .map(contactList -> new Pair<PersonModelAdvanced, List<ContactInfoModel>>(person, contactList)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().showProgressBar())
                .doFinally(() -> getViewState().hideProgressBar())
                .subscribe(
                        personModelAdvancedListPair -> {
                            getViewState().fetchContactDetails(personModelAdvancedListPair.first);
                            getViewState().fetchContactsInfo(personModelAdvancedListPair.second);
                        },
                        e -> getViewState().fetchError(e.getMessage())
                ));
    }

    @Override
    public void onDestroy() {
        this.compositeDisposable.dispose();
        super.onDestroy();
    }
}
