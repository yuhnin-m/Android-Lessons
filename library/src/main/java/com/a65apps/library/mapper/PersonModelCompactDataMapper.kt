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
    private fun transform(person: Person) = with(person) {
        PersonModelCompact(
                id = id,
                displayName = displayName,
                description = description,
                photoPreviewUri = Uri.parse(photoStringUri))
    }

    /**
     * Трансформация списка [Person] в список [PersonModelCompact].
     * @param personCollection список сущностей [Person].
     * @return Список [PersonModelCompact].
     */
    fun transform(personCollection: List<Person>): List<PersonModelCompact> {
        val personModelsCollection = mutableSetOf<PersonModelCompact>()
        if (personCollection.isNotEmpty()) {
            for (person in personCollection) {
                personModelsCollection.add(transform(person))
            }
        }
        return personModelsCollection.toList()
    }
}
