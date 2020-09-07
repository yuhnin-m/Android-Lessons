package com.a65apps.library.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.a65apps.library.Constants
import com.a65apps.library.R
import com.a65apps.library.di.containers.HasAppContainer
import com.a65apps.library.models.LocationModel
import com.a65apps.library.presenters.PersonMapPresenter
import com.a65apps.library.ui.listeners.EventActionBarListener
import com.a65apps.library.ui.listeners.OnPersonSetLocation
import com.a65apps.library.views.PersonMapView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_person_maps.*
import javax.inject.Inject
import javax.inject.Provider


private const val LOG_TAG = "person_map_fragment";

class PersonMapsFragment() : MvpAppCompatFragment(), PersonMapView, OnMapReadyCallback {
    var eventActionBarListener: EventActionBarListener? = null
    var onPersonSetLocation: OnPersonSetLocation? = null
    private lateinit var personId: String;
    private lateinit var personName: String;
    private lateinit var googleMap: GoogleMap
    private lateinit var currentMarker: Marker

    companion object {
        @JvmStatic
        fun newInstance(personId: String): PersonMapsFragment {
            val fragment = PersonMapsFragment()
            val args = Bundle()
            args.putString(Constants.KEY_PERSON_ID, personId)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var personMapPresenterProvider: Provider<PersonMapPresenter>

    @InjectPresenter
    lateinit var personMapPresenter: PersonMapPresenter

    @ProvidePresenter
    fun providerPersonMapPresenter(): PersonMapPresenter {
        return personMapPresenterProvider.get()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val app = requireActivity().application
        check(app is HasAppContainer)
        val contactMapComponent = (app as HasAppContainer)
                .appContainer().plusPersonLocationComponent()
        contactMapComponent.inject(this)
        if (context is EventActionBarListener) {
            eventActionBarListener = context
        }
        if (context is OnPersonSetLocation) {
            onPersonSetLocation = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_person_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        personId = arguments?.getString(Constants.KEY_PERSON_ID) ?: ""
        personName = arguments?.getString(Constants.KEY_PERSON_NAME) ?: ""
        personMapPresenter.requestPersonLocation(personId)

        Log.d(LOG_TAG, "Received from bundle: personId=$personId")
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.let {
            Log.d(LOG_TAG, "Request async map")
            it.getMapAsync(this)
        }
        buttonSaveLocation.setOnClickListener {
            if (currentMarker != null) {
                personMapPresenter.requestSavePersonLocation(personId, "", currentMarker.position)
            }
            Toast.makeText(requireContext(), "You clicked me.", Toast.LENGTH_SHORT).show()
        }

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDetach() {
        eventActionBarListener = null
        onPersonSetLocation = null
        super.onDetach()
    }

    override fun onPersonLocationLoad(locationModel: LocationModel) {
        createMarker(locationModel.latLng, personName)
        setCameraPosition(locationModel.latLng)
    }

    override fun onPersonLocationSaved(locationModel: LocationModel) {
        Toast.makeText(requireContext(),
                "${getText(R.string.text_success_save_location)} : $locationModel",
                Toast.LENGTH_SHORT).show()
    }

    override fun onError(errorMessage: String) {
        Toast.makeText(
                requireContext(),
                "${getText(R.string.text_error_fetch_person_location)}: $errorMessage",
                Toast.LENGTH_SHORT).show()
    }

    private fun createMarker(coordinate: LatLng, text: String) {
        googleMap.clear()
        currentMarker = googleMap.addMarker(MarkerOptions().position(coordinate).title(text))
    }

    private fun setCameraPosition(coordinate: LatLng) {
        val zoomValue: Float = if (googleMap.cameraPosition.zoom < Constants.DEFAULT_MAP_ZOOM)
            Constants.DEFAULT_MAP_ZOOM else googleMap.cameraPosition.zoom
        val cameraPosition = CameraPosition.Builder()
                .target(coordinate)
                .zoom(zoomValue)
                .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        googleMap.animateCamera(cameraUpdate)
    }

    private fun onMapClickListener(point: LatLng) {
        Log.d(LOG_TAG, "Выбрана координата $point")
        createMarker(point, personName)
        setCameraPosition(point)
    }

    override fun onMapReady(gMap: GoogleMap?) {
        gMap?.let {
            this.googleMap = gMap
            Log.d(LOG_TAG, "Map retrieved")
            gMap.setOnMapClickListener(OnMapClickListener { point: LatLng? ->
                gMap.clear()
                point?.let { onMapClickListener(it) }

            })
        }
    }


}