package com.a65apps.core.interactors.organizations

import com.a65apps.core.entities.Location
import com.a65apps.core.entities.Organization
import kotlinx.coroutines.flow.Flow

interface LocationOrganizationRepository {
    fun getOrganizationsByPerson(personId: String): Flow<List<Organization>?>
    fun getOrganizationsByAddress(address: String): Flow<List<Organization>?>
    fun getAllOrganizations(searchString: String): Flow<List<Organization>?>
    fun saveOrganization(organization: Organization)
}