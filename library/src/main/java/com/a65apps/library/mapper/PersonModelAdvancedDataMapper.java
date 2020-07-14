package com.a65apps.library.mapper;

import com.a65apps.core.entities.Person;
import com.a65apps.library.models.PersonModelAdvanced;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class PersonModelAdvancedDataMapper {

    public PersonModelAdvancedDataMapper() {}

    /**
     * Трансформация {@link Person} into an {@link com.a65apps.library.models.PersonModelAdvanced}.
     * @param person Object to be transformed.
     * @return {@link PersonModelAdvanced}.
     */
    public PersonModelAdvanced transform(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final PersonModelAdvanced personModel = new PersonModelAdvanced(
                person.getId(),
                person.getFullName(),
                person.getDescription(),
                person.getImageUriString(),
                person.getBirthdayString());
        return personModel;
    }

    /**
     * Метод трансформации коллекции {@link Person} в коллекция {@link PersonModelAdvanced}.
     * @param personCollection Objects to be transformed.
     * @return Коллекция {@link PersonModelAdvanced}.
     */
    public Collection<PersonModelAdvanced> transform(Collection<Person> personCollection) {
        Collection<PersonModelAdvanced> personModelsCollection;

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