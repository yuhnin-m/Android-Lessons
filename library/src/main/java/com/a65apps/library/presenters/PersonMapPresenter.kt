package com.a65apps.library.presenters

import com.a65apps.core.entities.Location
import com.a65apps.core.interactors.locations.PersonLocationInteractor
import com.a65apps.library.views.PersonMapView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

private const val LOG_TAG = "person_map_presenter"
@InjectViewState
class PersonMapPresenter(private val personLocationInteractor: PersonLocationInteractor) : MvpPresenter<PersonMapView>() {
    fun showMarker(): Location? {
        // TODO: реализовать
        return null
    }

    fun savePersonLocation(): Boolean {
        // TODO: реализовать
        return false
    }
}