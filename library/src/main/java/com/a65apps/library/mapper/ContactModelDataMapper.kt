package com.a65apps.library.mapper

import com.a65apps.core.entities.Contact
import com.a65apps.library.models.ContactModel

class ContactModelDataMapper {
    /**
     * Трансформация [Contact] into an [ContactModel].
     * @param contact Object to be transformed.
     * @return [ContactModel].
     */
    private fun transform(contact: Contact) = with(contact) {
        ContactModel(id, personId, contactType, value)
    }

    /**
     * Метод трансформации коллекции [Contact] в коллекция [ContactModel].
     * @param contactList Objects to be transformed.
     * @return Коллекция [ContactModel].
     */
    fun transform(contactList: List<Contact>): List<ContactModel> {
        val contactModels = mutableSetOf<ContactModel>()
        if (contactList.isNotEmpty()) {
            for (contact in contactList) {
                contactModels.add(transform(contact))
            }
        }
        return contactModels.toList()
    }
}
