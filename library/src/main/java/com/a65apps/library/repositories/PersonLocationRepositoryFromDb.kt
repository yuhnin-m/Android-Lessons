package com.a65apps.library.repositories

import android.util.Log
import com.a65apps.core.entities.Location
import com.a65apps.core.interactors.locations.PersonLocationRepository
import com.a65apps.library.database.AppDatabase
import com.a65apps.library.mapper.LocationModelMapper
import com.a65apps.library.models.LocationModel
import kotlinx.coroutines.flow.flow

private const val LOG_TAG = "location_repository"

class PersonLocationRepositoryFromDb(private val database: AppDatabase) : PersonLocationRepository {
    /**
     * Переопределенный метод запроса местополжения определенного контакта
     * @param personId идентификатор пользователя
     * @return Flow с экземпляром [Location]
     */
    override fun getLocationByPerson(personId: String) = flow {
        Log.d(LOG_TAG, "Get location by personId = $personId")
        val personLocation : LocationModel? = database.locationDao().getByPersonId(personId)
        if (personLocation != null) {
            emit(LocationModelMapper().transformModelToEntity(locationModel = personLocation))
        } else {
            emit(null)
        }
    }

    /**
     * Переопределенный метод запроса всех записей местополжения из БД
     * @return Flow со списком [Location]
     */
    override fun getAllPersonLocation() = flow<List<Location>> {
        val locationList = database.locationDao().getAll()
        emit(LocationModelMapper().transformModelListToEntityList(locationList))
    }

    /**
     * Переопределенный метод создания записи местополжения в БД
     * @param location местоположение для записи
     */
    override fun createPersonLocation(location: Location) {
        with(location) {
            Log.d(LOG_TAG, "Write location personId=$personId; lng=$longitude; lat=$latitude")
            val locationModel = LocationModel(personId, address, longitude, latitude)
            database.locationDao().insertLocation(locationModel)
        }
    }
}
