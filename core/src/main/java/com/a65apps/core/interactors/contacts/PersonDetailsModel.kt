package com.a65apps.core.interactors.contacts

import com.a65apps.core.entities.Contact
import com.a65apps.core.entities.Person
import kotlinx.coroutines.flow.Flow

class PersonDetailsModel(private val repository: PersonDetailsRepository) : PersonDetailsInteractor {
    override fun loadContactsByPerson(personId: String): Flow<List<Contact>> {
        return repository.getContactsByPerson(personId)
    }

    override fun loadPersonDetails(personId: String): Flow<Person> {
        return repository.getPersonDetails(personId)
    }
}
