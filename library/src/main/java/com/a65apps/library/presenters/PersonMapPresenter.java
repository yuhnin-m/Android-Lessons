package com.a65apps.library.presenters;

import com.a65apps.core.entities.Location;
import com.a65apps.core.interactors.locations.PersonLocationInteractor;
import com.a65apps.library.views.PersonMapView;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PersonMapPresenter extends MvpPresenter<PersonMapView> {
    private final String LOG_TAG = "person_map_presenter";

    @NonNull
    private final PersonLocationInteractor personLocationInteractor;

    @NonNull
    private final CompositeDisposable compositeDisposable;


    public PersonMapPresenter(@NonNull PersonLocationInteractor personLocationInteractor) {
        this.personLocationInteractor = personLocationInteractor;
        compositeDisposable = new CompositeDisposable();
    }

    public Location showMarker() {
        // TODO: реализовать
        return null;
    }

    public boolean savePersonLocation() {
        // TODO: реализовать
        return false;
    }
}
