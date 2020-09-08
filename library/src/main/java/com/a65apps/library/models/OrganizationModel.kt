package com.a65apps.library.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
        entity = LocationModel::class,
        parentColumns = arrayOf("person_id"),
        childColumns = arrayOf("location_id"),
        onDelete = ForeignKey.CASCADE)])
data class OrganizationModel(
        @PrimaryKey(autoGenerate = true)
        var uid: Int,
        @ColumnInfo(name = "location_id") val locationId: String,
        val address: String,
        val name: String
)