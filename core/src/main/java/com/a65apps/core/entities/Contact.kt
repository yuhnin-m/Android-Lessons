package com.a65apps.core.entities

data class Contact(
        val id: Long,
        val personId: String,
        val contactType: ContactType,
        val value: String
)
