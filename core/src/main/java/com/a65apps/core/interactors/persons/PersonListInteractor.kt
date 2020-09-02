package com.a65apps.core.interactors.persons

import com.a65apps.core.entities.Person
import kotlinx.coroutines.flow.Flow

interface PersonListInteractor {
    fun loadAllPersonsFlow(query: String): Flow<List<Person>>
    suspend fun loadAllPersons(query: String): List<Person>
}