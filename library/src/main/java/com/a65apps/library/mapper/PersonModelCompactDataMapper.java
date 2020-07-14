package com.a65apps.library.mapper;

import com.a65apps.core.entities.Person;
import com.a65apps.library.models.PersonModelCompact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonModelCompactDataMapper {

    /**
     * Трансформация {@link Person} into an {@link PersonModelCompact}.
     * @param person Object to be transformed.
     * @return {@link PersonModelCompact}.
     */
    public PersonModelCompact transform(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final PersonModelCompact personModel = new PersonModelCompact(
                person.getId(),
                person.getFullName(),
                person.getDescription(),
                person.getImageUriString());
        return personModel;
    }

    /**
     * Метод трансформации коллекции {@link Person} в коллекция {@link PersonModelCompact}.
     * @param personCollection Objects to be transformed.
     * @return Коллекция {@link PersonModelCompact}.
     */
    public List<PersonModelCompact> transform(List<Person> personCollection) {
        List<PersonModelCompact> personModelsCollection;

        if (personCollection != null && !personCollection.isEmpty()) {
            personModelsCollection = new ArrayList<>();
            for (Person person : personCollection) {
                personModelsCollection.add(transform(person));
            }
        } else {
            personModelsCollection = Collections.emptyList();
        }

        return personModelsCollection;
    }
}