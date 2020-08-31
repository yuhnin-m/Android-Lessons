package com.a65apps.core.interactors.persons

import com.a65apps.core.entities.Person
import io.reactivex.rxjava3.annotations.Nullable
import kotlinx.coroutines.flow.Flow

interface PersonListRepository {
    fun getAllPersonsFlow(searchString: String): Flow<List<Person>>
    suspend fun getAllPersons(searchString: String): List<Person>

}