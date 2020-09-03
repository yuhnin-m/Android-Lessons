package com.a65apps.library.repositories

import android.content.Context
import com.a65apps.core.entities.Location
import com.a65apps.core.interactors.locations.PersonLocationRepository
import com.a65apps.library.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PersonLocationRepositoryFromDb(private val database: AppDatabase, private val context: Context) : PersonLocationRepository {
    override fun getLocationByPerson(personId: String): Flow<Location>? {
        // TODO: реализовать
        return null;
    }

    override fun getAllPersonLocation(): Flow<List<Location>>? {
        // TODO: реализовать
        return null;
    }

    override fun createPersonLocation(location: Location): Flow<Location>? {
        // TODO: реализовать
        return null;
    }
}