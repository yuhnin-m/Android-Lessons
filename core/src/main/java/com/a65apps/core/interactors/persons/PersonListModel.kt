package com.a65apps.core.interactors.persons

import com.a65apps.core.entities.Person
import kotlinx.coroutines.flow.Flow

class PersonListModel(private val repository: PersonListRepository) : PersonListInteractor {
    override fun loadAllPersonsFlow(searchString: String): Flow<List<Person>> {
        return repository.getAllPersonsFlow(searchString)
    }

    override suspend fun loadAllPersons(searchString: String): List<Person> {
        return repository.getAllPersons(searchString)
    }

}