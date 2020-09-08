package com.a65apps.library.database.dao

import androidx.room.*
import com.a65apps.library.models.OrganizationModel

@Dao
interface OrganizationDao {
    @Query("SELECT * FROM OrganizationModel")
    fun getAll(): List<OrganizationModel>

    @Query("SELECT * FROM OrganizationModel WHERE location_id = :id")
    fun getByLocationId(id: String): List<OrganizationModel>

    @Query("SELECT * FROM OrganizationModel WHERE location_id = :id")
    fun getByPersonId(id: String): List<OrganizationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrganization(location: OrganizationModel)

    @Delete
    fun delete(organizationModel: OrganizationModel)
}