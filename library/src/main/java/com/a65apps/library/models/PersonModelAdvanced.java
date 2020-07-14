package com.a65apps.library.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.library.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonModelAdvanced {
    @NonNull
    private final String id;
    @NonNull
    private final String displayName;
    @Nullable
    private final String description;
    @Nullable
    private final Date dateBirthday;
    @Nullable
    private final Uri photoUri;

    public PersonModelAdvanced(@NonNull String id, @NonNull String displayName, @Nullable String description,
                               @Nullable String photoUriString, @Nullable String dateBirthday) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.photoUri = Uri.parse(photoUriString);
        Date tempDateBirthDay = null;
        try {
            tempDateBirthDay = new SimpleDateFormat("dd-MM-yyyy").parse(dateBirthday);
        } catch (Exception e) {
            tempDateBirthDay = null;
        }
        this.dateBirthday = tempDateBirthDay;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getFullName() {
        return this.displayName;
    }

    @Nullable
    public String getDescription() {
        return this.description;
    }

    @Nullable
    public Date getDateBirthday() {
        return this.dateBirthday;
    }

    @Nullable
    public String getStringBirthday() {
        if (dateBirthday != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_STRING_FORMAT);
            return simpleDateFormat.format(dateBirthday);
        } else {
            return "";
        }
    }

    public long getValueBirthday() {
        return dateBirthday == null ? -1 : dateBirthday.getTime();
    }

    @Nullable
    public Uri getImageUri() {
        return this.photoUri;
    }
}
