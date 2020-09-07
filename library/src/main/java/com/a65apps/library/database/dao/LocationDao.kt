package com.a65apps.library.database.dao

import androidx.room.*
import com.a65apps.library.models.LocationModel
@Dao
interface LocationDao {
    @Query("SELECT * FROM LocationModel")
    fun getAll(): List<LocationModel>

    @Query("SELECT * FROM LocationModel WHERE person_id = :id")
    fun getByPersonId(id: String): LocationModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationModel)

    @Query("SELECT EXISTS(SELECT * FROM LocationModel WHERE person_id = :id)")
    fun isExist(id: String): Int

    @Delete
    fun delete(locationModel: LocationModel?)
}