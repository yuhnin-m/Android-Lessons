package com.a65apps.library.repositories

import android.content.Context
import com.a65apps.core.entities.Location
import com.a65apps.core.entities.Organization
import com.a65apps.core.interactors.organizations.LocationOrganizationRepository
import kotlinx.coroutines.flow.flow
import java.util.*

class LocationOrganizationRepositoryRetrofit(private val context: Context) : LocationOrganizationRepository {

    override fun getOrganizationsByLocation(location: Location) = flow {
        val organizations: List<Organization> = ArrayList()
        emit(organizations)
    }
}