package com.a65apps.core.entities;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class Location {

    private final String uid;

    @NonNull
    private final String personId;

    @Nullable
    private final String address;

    @NonNull
    private final double longitude;

    @NonNull
    private final double latitude;

    public Location(@NonNull String uid, @NonNull String personId, @Nullable String address,
                    @NonNull double longitude, @NonNull double latitude) {
        this.uid = uid;
        this.personId = personId;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getPersonId() {
        return personId;
    }

    public String getAddress() {
        return address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
