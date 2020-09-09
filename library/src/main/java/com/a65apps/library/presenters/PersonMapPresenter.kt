package com.a65apps.library.presenters

import android.util.Log
import com.a65apps.core.entities.Location
import com.a65apps.core.interactors.locations.PersonLocationInteractor
import com.a65apps.library.Constants
import com.a65apps.library.mapper.LocationModelMapper
import com.a65apps.library.models.LocationModel
import com.a65apps.library.views.PersonMapView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val LOG_TAG = "person_map_presenter"

@InjectViewState
class PersonMapPresenter(
        private val personLocationInteractor: PersonLocationInteractor,
        private val mapper: LocationModelMapper) : MvpPresenter<PersonMapView>() {
    private val handler = CoroutineExceptionHandler { _, exception ->
        exception.message?.let {
            viewState.onError(it)
        }
    }
    private val job = SupervisorJob()
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job + handler)

    fun requestPersonLocation(personId: String) {
        scope.launch {
            personLocationInteractor.loadLocationByPerson(personId)
                    .flowOn(Dispatchers.IO)
                    .collect() { location ->
                        val locationModel = if (location != null) {
                            mapper.transformEntityToModel(location)
                        } else {
                            LocationModel(personId, "",
                                    Constants.DEFAULT_LONGITUDE, Constants.DEFAULT_LATITUDE)
                        }
                        viewState.onPersonLocationLoad(locationModel = locationModel)
                    }
        }
    }

    fun requestSavePersonLocation(personId: String, address: String, coords: LatLng) {
        Log.d(LOG_TAG, "Request save person=$personId location $coords")
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
                viewState.onPersonLocationSaved(mapper.transformEntityToModel(locationForSave))
            } catch (e: Exception) {
                viewState.onError(e.message ?: "-")
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
