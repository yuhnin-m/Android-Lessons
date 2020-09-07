package com.a65apps.core.interactors.locations

import com.a65apps.core.entities.Location
import kotlinx.coroutines.flow.Flow

interface PersonLocationInteractor {
    fun loadLocationByPerson(personId: String): Flow<Location?>
    fun loadAllPersonLocations(): Flow<List<Location>>?
    fun createPersonLocation(location: Location)
}