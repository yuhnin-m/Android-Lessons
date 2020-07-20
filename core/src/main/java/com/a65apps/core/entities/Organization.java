package com.a65apps.core.entities;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class Organization {
    @NonNull
    private final String uid;

    @NonNull
    private final String uidLocation;

    @NonNull
    private final String name;

    @Nullable
    private final String description;

    @Nullable
    private final String availabilities;


    public Organization(@NonNull String uid, @NonNull String uidLocation, @NonNull String name,
                        @Nullable String description, @Nullable String availabilities) {
        this.uid = uid;
        this.uidLocation = uidLocation;
        this.name = name;
        this.description = description;
        this.availabilities = availabilities;
    }

    public String getUid() {
        return uid;
    }

    public String getUidLocation() {
        return uidLocation;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAvailabilities() {
        return availabilities;
    }
}
