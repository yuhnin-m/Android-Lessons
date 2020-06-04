package com.a65apps.yuhnin.lesson1.pojo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonModelCompact {
    private int id;
    private final String displayName;
    private Uri photoPreviewUri;


    public Uri getImageUri() {
        return photoPreviewUri;
    }



    public PersonModelCompact(int id, @NonNull String displayName, @Nullable Uri photoPreviewUri)
            throws ParseException {
        this.id = id;
        this.displayName = displayName;
        this.photoPreviewUri = photoPreviewUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
