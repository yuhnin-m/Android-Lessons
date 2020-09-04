package com.a65apps.library.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import javax.inject.Inject
import javax.inject.Provider

private const val LOG_TAG = "person_map_fragment";
class PersonMapsFragment() : MvpAppCompatFragment(), PersonMapView, OnMapReadyCallback {
    var eventActionBarListener: EventActionBarListener? = null
    var onPersonSetLocation: OnPersonSetLocation? = null
    private lateinit var personId: String;
    private lateinit var googleMap: GoogleMap

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
        Log.d(LOG_TAG, "Received from bundle: personId=$personId")
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.let {
            Log.d(LOG_TAG, "Request async map")
            it.getMapAsync(this)
        }
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDetach() {
        eventActionBarListener = null
        onPersonSetLocation = null
        super.onDetach()
    }

    override fun drawMarker(locationModel: LocationModel) {
        TODO("Not yet implemented")
    }

    override fun onPersonLocationLoad(locationModel: LocationModel) {
        TODO("Not yet implemented")
    }

    override fun onPersonLocationSaved(locationModel: LocationModel) {
        TODO("Not yet implemented")
    }

    override fun onError(errorMessage: String) {
        TODO("Not yet implemented")
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            Log.d(LOG_TAG, "Map retrieved")
            this.googleMap = googleMap
        }
    }
}