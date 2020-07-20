package com.a65apps.library.ui.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.a65apps.library.R;
import com.a65apps.library.models.LocationModel;
import com.a65apps.library.ui.listeners.EventActionBarListener;
import com.a65apps.library.ui.listeners.OnPersonSetLocation;
import com.a65apps.library.views.PersonMapView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class PersonMapsFragment extends MvpAppCompatFragment implements PersonMapView, OnMapReadyCallback{

    private final String LOG_TAG = "person_map_fragment";

    @Nullable
    Button btn_save_location;

    @Nullable
    MapView mapView;

    @Nullable
    GoogleMap googleMap;

    @Nullable
    EventActionBarListener eventActionBarListener;

    @Nullable
    OnPersonSetLocation onPersonSetLocation;

    @Override
    public void onAttach(@Nullable Context context) {
        if (context instanceof EventActionBarListener) {
            eventActionBarListener = (EventActionBarListener) context;
        }
        if (context instanceof OnPersonSetLocation) {
            onPersonSetLocation = (OnPersonSetLocation) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btn_save_location = view.findViewById(R.id.btn_open_set_location);
        mapView = view.findViewById(R.id.mapView);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        Objects.requireNonNull(getActivity()).setTitle(R.string.text_header_map_view);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng coordinates = new LatLng( 56.846428, 53.233475 );
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(coordinates)
                .zoom(15)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        this.googleMap.animateCamera(cameraUpdate);

    }

    @Override
    public void drawMarker(LocationModel locationModel) {

        return;
    }

    @Override
    public void onPersonLocationLoad(LocationModel locationModel) {
        return;
    }

    @Override
    public void onPersonLocationSaved(LocationModel locationModel) {
        Log.d(LOG_TAG, "Местоположение контакта сохранено: " + locationModel.toString());
    }

    @Override
    public void onError(String errorMessage) {
        Log.e(LOG_TAG, "Возникла ошибка " + errorMessage);

    }

}