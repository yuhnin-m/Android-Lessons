package com.a65apps.yuhnin.lesson1.pojo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PersonModelCompact {
    private final String id;
    private final String displayName;
    private final Uri photoPreviewUri;

    public PersonModelCompact(@NonNull String id, @NonNull String displayName, @Nullable Uri photoPreviewUri) {
        this.id = id;
        this.displayName = displayName;
        this.photoPreviewUri = photoPreviewUri;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Uri getImageUri() {
        return photoPreviewUri;
    }

}
