package com.a65apps.core.interactors.contacts

import com.a65apps.core.entities.Contact
import com.a65apps.core.entities.Person
import kotlinx.coroutines.flow.Flow

interface PersonDetailsInteractor {
    fun loadContactsByPerson(personId: String): Flow<List<Contact>>
    fun loadPersonDetails(personId: String): Flow<Person>
}
