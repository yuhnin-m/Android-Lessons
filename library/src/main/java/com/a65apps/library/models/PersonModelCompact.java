package com.a65apps.library.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PersonModelCompact {
    @NonNull
    private final String id;
    @NonNull
    private final String displayName;
    @Nullable
    private final String description;
    @Nullable
    private final Uri photoPreviewUri;

    public PersonModelCompact(@NonNull String id, @NonNull String displayName, @Nullable String description, @Nullable String photoPreviewUri) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.photoPreviewUri = Uri.parse(photoPreviewUri);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getDisplayName() {
        return this.displayName;
    }
    @Nullable
    public String getDescription() {
        return this.description;
    }
    @Nullable
    public Uri getImageUri() {
        return photoPreviewUri;
    }
}
