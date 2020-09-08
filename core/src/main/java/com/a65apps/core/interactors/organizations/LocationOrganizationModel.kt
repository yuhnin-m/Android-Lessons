package com.a65apps.core.interactors.organizations

import com.a65apps.core.entities.Location
import com.a65apps.core.entities.Organization
import kotlinx.coroutines.flow.Flow

class LocationOrganizationModel(private val locationOrganizationRepository: LocationOrganizationRepository) : LocationOrganizationInteractor {

    override fun loadOrganizationsByLocation(location: Location): Flow<List<Organization>?> {
        return locationOrganizationRepository.getOrganizationsByLocation(location)
    }
}

