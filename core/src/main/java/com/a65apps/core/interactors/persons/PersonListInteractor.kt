package com.a65apps.core.interactors.persons

import com.a65apps.core.entities.Person
import kotlinx.coroutines.flow.Flow

interface PersonListInteractor {
    fun loadAllPersonsFlow(searchString: String): Flow<List<Person>>
    suspend fun loadAllPersons(searchString: String): List<Person>
}