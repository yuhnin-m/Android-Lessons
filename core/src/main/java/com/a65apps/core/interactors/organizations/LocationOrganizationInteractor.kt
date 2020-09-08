package com.a65apps.core.interactors.organizations

import com.a65apps.core.entities.Location
import com.a65apps.core.entities.Organization
import kotlinx.coroutines.flow.Flow

interface LocationOrganizationInteractor {
    fun saveOrganization(organization: Organization)
    fun loadOrganizationByAddress(address: String): Flow<List<Organization>?>
    fun loadOrganizationsByPersonId(personId: String): Flow<List<Organization>?>
    fun loadAllOrganizations(searchString: String): Flow<List<Organization>?>
}