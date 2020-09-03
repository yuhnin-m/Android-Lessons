package com.a65apps.library.mapper

import com.a65apps.core.entities.Person
import com.a65apps.library.models.PersonModelAdvanced

class PersonModelAdvancedDataMapper {

    /**
     * Трансформация [Person] into an [com.a65apps.library.models.PersonModelAdvanced].
     * @param person Object to be transformed.
     * @return [PersonModelAdvanced].
     */
    fun transform(person: Person) = with(person) {
        PersonModelAdvanced(
                id = id,
                displayName = displayName,
                description = description,
                photoUriString = photoStringUri,
                dateBirthday = dateBirthday)
    }

    /**
     * Метод трансформации коллекции [Person] в коллекция [PersonModelAdvanced].
     * @param personCollection Objects to be transformed.
     * @return Коллекция [PersonModelAdvanced].
     */
    fun transform(personCollection: List<Person>): List<PersonModelAdvanced> {
        val personModelsCollection = mutableSetOf<PersonModelAdvanced>()
        if (personCollection.isNotEmpty()) {
            for (person in personCollection) {
                personModelsCollection.add(transform(person))
            }
        }
        return personModelsCollection.toList()
    }
}