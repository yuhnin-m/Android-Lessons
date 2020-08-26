package com.a65apps.library.mapper

import android.net.Uri
import com.a65apps.core.entities.Person
import com.a65apps.library.models.PersonModelCompact

class PersonModelCompactDataMapper {
    /**
     * Трансформация [Person] в [PersonModelCompact].
     * @param person Экземпляр сущности [Person].
     * @return [PersonModelCompact].
     */
    fun transform(person: Person): PersonModelCompact {
        return PersonModelCompact(
                person.id,
                person.fullName,
                person.description,
                Uri.parse(person.imageUriString))
    }

    /**
     * Трансформация списка [Person] в список [PersonModelCompact].
     * @param person список сущностей [Person].
     * @return Список [PersonModelCompact].
     */
    fun transform(personCollection: List<Person>?): List<PersonModelCompact>?{
        val personModelsCollection = mutableSetOf<PersonModelCompact>()
        if (personCollection != null && !personCollection.isEmpty()) {
            for (person in personCollection) {
                personModelsCollection.add(transform(person))
            }
        }
        return personModelsCollection.toList()
    }

}