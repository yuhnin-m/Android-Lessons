package com.a65apps.library.repositories

import android.content.Context
import com.a65apps.core.entities.Location
import com.a65apps.core.entities.Organization
import com.a65apps.core.interactors.organizations.LocationOrganizationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class LocationOrganizationRepositoryRetrofit(private val context: Context) : LocationOrganizationRepository {

    override fun getOrganizationsByPerson(personId: String) = flow {
        //TODO: Необходима реализация
        val organizations: List<Organization> = ArrayList()
        emit(organizations)
    }

    override fun getOrganizationsByAddress(address: String) = flow {
        //TODO: Необходима реализация
        val organizations: List<Organization> = ArrayList()
        emit(organizations)
    }

    override fun getAddressByCoords(lat: Double, lng: Double) = flow {
        //TODO: Необходима реализация
        val emptyAddress = "Нет адреса"
        emit(emptyAddress)
    }

    override fun getAllOrganizations(searchString: String) = flow {
        //TODO: Необходима реализация
        val organizations: List<Organization> = ArrayList()
        emit(organizations)
    }

    override fun saveOrganization(organization: Organization) {
        TODO("Not yet implemented")
    }
}