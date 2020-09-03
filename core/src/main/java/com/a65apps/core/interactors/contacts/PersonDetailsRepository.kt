package com.a65apps.core.interactors.contacts

import com.a65apps.core.entities.Contact
import com.a65apps.core.entities.Person
import kotlinx.coroutines.flow.Flow

interface PersonDetailsRepository {
    fun getContactsByPerson(personId: String): Flow<List<Contact>>
    fun getPersonDetails(personId: String): Flow<Person>
}