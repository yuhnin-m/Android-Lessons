package com.a65apps.core.interactors.locations

import com.a65apps.core.entities.Location
import kotlinx.coroutines.flow.Flow

class PersonLocationModel(private val personLocationRepository: PersonLocationRepository) : PersonLocationInteractor {
    override fun loadLocationByPerson(personId: String): Flow<Location> {
        return personLocationRepository.getLocationByPerson(personId)
    }

    override fun loadAllPersonLocations(): Flow<List<Location>> {
        return personLocationRepository.getAllPersonLocation()
    }

    override fun createPersonLocation(location: Location): Flow<Location> {
        return personLocationRepository.createPersonLocation(location)
    }
}