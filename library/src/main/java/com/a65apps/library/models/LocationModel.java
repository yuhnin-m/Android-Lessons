package com.a65apps.library.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;


@Entity
public class LocationModel {
    @PrimaryKey
    @ColumnInfo(name = "person_id")
    @NonNull
    private final String personId;

    @Nullable
    @ColumnInfo(name = "person_address")
    private final String address;

    @NonNull
    @ColumnInfo(name = "longitude")
    private final double longitude;

    @NonNull
    @ColumnInfo(name = "latitude")
    private final double latitude;

    public LocationModel(@NonNull String personId, @Nullable String address,
                         @NonNull double longitude, @NonNull double latitude) {
        this.personId = personId;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public LocationModel(@NonNull String personId, @Nullable String address,
                         @NonNull LatLng coord) {
        this.personId = personId;
        this.address = address;
        this.longitude = coord.longitude;
        this.latitude = coord.latitude;
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

    @NonNull
    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "personId='" + personId + '\'' +
                ", address='" + address + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
