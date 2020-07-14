package com.a65apps.core.entities;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class Person {
    @NonNull
    private final String id;
    @NonNull
    private final String displayName;
    @Nullable
    private final String description;
    @Nullable
    private final String dateBirthday;
    @Nullable
    private final String photoStringUri;

    public Person(@NonNull String id, @NonNull String displayName, @Nullable String description,
                               @Nullable String photoStringUri, @Nullable String dateBirthday) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.photoStringUri = photoStringUri;
        this.dateBirthday = dateBirthday;
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
    public String getBirthdayString() {
        return this.dateBirthday;
    }

    @Nullable
    public String getImageUriString() {
        return this.photoStringUri;
    }
}
