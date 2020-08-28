package com.a65apps.library.models

import android.net.Uri

data class PersonModelCompact (
    val id: String,
    val displayName: String,
    val description: String?,
    val photoPreviewUri: Uri
)