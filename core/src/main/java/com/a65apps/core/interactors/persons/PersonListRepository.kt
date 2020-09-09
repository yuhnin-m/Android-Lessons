package com.a65apps.core.interactors.persons

import com.a65apps.core.entities.Person
import kotlinx.coroutines.flow.Flow

interface PersonListRepository {
    fun getAllPersonsFlow(searchString: String): Flow<List<Person>>
    suspend fun getAllPersons(searchString: String): List<Person>
}
