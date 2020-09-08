package com.a65apps.core.interactors.locations

import com.a65apps.core.entities.Location
import kotlinx.coroutines.flow.Flow

interface PersonLocationRepository {
    fun getLocationByPerson(personId: String): Flow<Location?>
    fun getAllPersonLocation(): Flow<List<Location>>?
    fun createPersonLocation(location: Location)
}
