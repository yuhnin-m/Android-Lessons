package com.a65apps.library.repositories

import android.content.Context
import android.util.Log
import com.a65apps.core.entities.Location
import com.a65apps.core.interactors.locations.PersonLocationRepository
import com.a65apps.library.database.AppDatabase
import com.a65apps.library.mapper.LocationModelMapper
import com.a65apps.library.models.LocationModel
import kotlinx.coroutines.flow.flow

private const val LOG_TAG = "location_repository"
class PersonLocationRepositoryFromDb(private val database: AppDatabase, private val context: Context) : PersonLocationRepository {
    override fun getLocationByPerson(personId: String) = flow {
        Log.d(LOG_TAG, "get location by personId = $personId")
        val personLocation = database.locationDao().getByPersonId(personId)
        emit(LocationModelMapper().transform(personLocation))
    }

    override fun getAllPersonLocation() = flow<List<Location>> {
        val locationList = database.locationDao().getAll()
        emit(LocationModelMapper().transform(locationList))
    }

    override fun createPersonLocation(location: Location) {
        location.let {
            val locationModel = LocationModel(it.personId, it.address, it.longitude, it.latitude)
            database.locationDao().insertLocation(locationModel)
        }
    }
}
