package com.a65apps.library.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

@Entity
public class LocationModel {
    @PrimaryKey
    @ColumnInfo(name = "person_id")
    private final String personId;

    @ColumnInfo(name = "person_address")
    private final String address;

    @ColumnInfo(name = "longitude")
    private final double longitude ;

    @ColumnInfo(name = "latitude")
    private final double latitude;

    public LocationModel(@NonNull String personId, @Nullable String address,
                         @NonNull double longitude, @NonNull double latitude) {
        this.personId = personId;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @NonNull
    public String getPersonId() {
        return personId;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    @NonNull
    public double getLongitude() {
        return longitude;
    }

    @NonNull
    public double getLatitude() {
        return latitude;
    }
}
