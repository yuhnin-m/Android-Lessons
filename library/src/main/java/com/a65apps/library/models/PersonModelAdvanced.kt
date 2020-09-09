package com.a65apps.library.models

data class PersonModelAdvanced(
        val id: String,
        val displayName: String,
        val description: String?,
        val photoUriString: String,
        val dateBirthday: String?
)
