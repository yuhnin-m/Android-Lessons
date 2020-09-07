package com.a65apps.library.mapper

import com.a65apps.core.entities.Location
import com.a65apps.library.models.LocationModel


class LocationModelMapper {

    /**
     * ������������� ������ [LocationModel] � ����� [Location]
     * @param locationModel Object to be transformed.
     * @return [Location].
     */
    fun transform(locationModel: LocationModel) = with(locationModel) {
        Location(
                personId = personId,
                address = address ?: "",
                longitude = longitude,
                latitude = latitude)
    }

    /**
     * ����� ������������� ��������� [LocationModel] � ������ [Location]
     * @param locationModels ������� ��� �������������
     * @return ��������� [Location].
     */
    fun transform(locationModels: List<LocationModel>): List<Location> {
        val locationCollection = mutableSetOf<Location>()
        if (locationModels.isNotEmpty()) {
            for (locationModel in locationModels) {
                locationCollection.add(transform(locationModel))
            }
        }
        return locationCollection.toList()
    }
}