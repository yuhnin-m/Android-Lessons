package com.a65apps.library.mapper;

import com.a65apps.core.entities.Contact;
import com.a65apps.library.models.ContactModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ContactModelDataMapper {
    @Inject
    public ContactModelDataMapper() {}

    /**
     * Трансформация {@link Contact} into an {@link ContactModel}.
     * @param contact Object to be transformed.
     * @return {@link ContactModel}.
     */
    public ContactModel transform(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final ContactModel contactModel = new ContactModel(
                contact.getId(),
                contact.getPersonId(),
                contact.getContactType(),
                contact.getValue());
        return contactModel;
    }

    /**
     * Метод трансформации коллекции {@link Contact} в коллекция {@link ContactModel}.
     * @param contactList Objects to be transformed.
     * @return Коллекция {@link ContactModel}.
     */
    public List<ContactModel> transform(List<Contact> contactList) {
        List<ContactModel> contactModels;

        if (contactList != null && !contactList.isEmpty()) {
            contactModels = new ArrayList<>();
            for (Contact contact : contactList) {
                contactModels.add(transform(contact));
            }
        } else {
            contactModels = Collections.emptyList();
        }

        return contactModels;
    }
}
