package com.a65apps.core.interactors.organizations

import com.a65apps.core.entities.Organization
import kotlinx.coroutines.flow.Flow

class LocationOrganizationModel(private val repository: LocationOrganizationRepository) : LocationOrganizationInteractor {

    override fun saveOrganization(organization: Organization) {
        return repository.saveOrganization(organization)
    }

    override fun loadOrganizationByAddress(address: String): Flow<List<Organization>?> {
        return repository.getOrganizationsByAddress(address)
    }

    override fun loadOrganizationsByPersonId(personId: String): Flow<List<Organization>?> {
        return repository.getOrganizationsByPerson(personId)
    }

    override fun loadAllOrganizations(searchString: String): Flow<List<Organization>?> {
        return  repository.getAllOrganizations(searchString)
    }
}
