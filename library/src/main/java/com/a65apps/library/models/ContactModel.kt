package com.a65apps.library.models

import com.a65apps.core.entities.ContactType

data class ContactModel(
        val id: Long,
        val personId: String,
        val contactType: ContactType,
        val value: String,
)
