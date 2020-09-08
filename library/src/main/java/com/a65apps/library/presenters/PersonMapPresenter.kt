package com.a65apps.library.presenters

import android.util.Log
import com.a65apps.core.entities.Location
import com.a65apps.core.interactors.locations.PersonLocationInteractor
import com.a65apps.core.interactors.organizations.LocationOrganizationInteractor
import com.a65apps.library.Constants
import com.a65apps.library.mapper.LocationModelMapper
import com.a65apps.library.models.LocationModel
import com.a65apps.library.views.PersonMapView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

private const val LOG_TAG = "person_map_presenter"

@InjectViewState
class PersonMapPresenter(
        private val personLocationInteractor: PersonLocationInteractor,
        private val locationOrganizationInteractor: LocationOrganizationInteractor) : MvpPresenter<PersonMapView>() {
    private val job = SupervisorJob()
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)


    fun requestPersonLocation(personId: String) {
        try {
            scope.launch {
                personLocationInteractor.loadLocationByPerson(personId).flowOn(Dispatchers.IO)
                        .collect() { location ->
                            val locationModel = if (location != null) {
                                LocationModelMapper().transformEntityToModel(location)
                            } else {
                                LocationModel(personId, "",
                                        Constants.DEFAULT_LONGITUDE, Constants.DEFAULT_LATITUDE)
                            }
                            viewState.onPersonLocationLoad(locationModel = locationModel)
                        }
            }
        } catch (e: Exception) {
            e.message?.let {
                viewState.onError(it)
            }
        }
    }

    fun requestSavePersonLocation(personId: String, address:String, coords: LatLng) {
        Log.d(LOG_TAG, "Request save person=$personId location $coords")
        try {
            scope.launch {
                try {
                    val locationForSave = Location(
                            personId = personId,
                            address = address,
                            longitude = coords.longitude,
                            latitude = coords.latitude)
                    withContext(Dispatchers.IO) {
                        personLocationInteractor.createPersonLocation(locationForSave)
                    }
                    viewState.onPersonLocationSaved(LocationModelMapper().transformEntityToModel(locationForSave))
                } catch (e: Exception) {
                    viewState.onError(e.message?:"-")
                }
            }
        } catch (e: Exception) {

        }


    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}