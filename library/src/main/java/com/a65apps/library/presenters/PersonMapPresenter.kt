package com.a65apps.library.presenters

import com.a65apps.core.entities.Location
import com.a65apps.core.interactors.locations.PersonLocationInteractor
import com.a65apps.library.views.PersonMapView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

private const val LOG_TAG = "person_map_presenter"

@InjectViewState
class PersonMapPresenter(private val personLocationInteractor: PersonLocationInteractor) : MvpPresenter<PersonMapView>() {
    fun requestPersonLocation(personId: String) {

        /*
        personLocationInteractor.loadLocationByPerson(personId)
        .flowOn(Dispatchers.IO).collect {
            viewState.onPersonLocationLoad(it)
        }
        */
    }

    fun requestSavePersonLocation(coords: LatLng) {

    }
}