package com.a65apps.library.mapper

import com.a65apps.core.entities.Location
import com.a65apps.library.models.LocationModel


class LocationModelMapper {

    /**
     * ������������� ������ [LocationModel] � ����� [Location]
     * @param locationModel Object to be transformed.
     * @return [Location].
     */
    fun transformModelToEntity(locationModel: LocationModel) = with(locationModel) {
        Location(
                personId = personId,
                address = address ?: "",
                longitude = longitude,
                latitude = latitude)
    }

    /**
     * ������������� ������ [Location] � ����� [LocationModel]
     * @param location Object to be transformed.
     * @return [LocationModel].
     */
    fun transformEntityToModel(location: Location) = with(location) {
        LocationModel(personId, address ?: "", longitude, latitude)
    }


    /**
     * ����� ������������� ��������� [LocationModel] � ������ [Location]
     * @param locationModels ������� ��� �������������
     * @return ��������� [Location].
     */
    fun transformModelListToEntityList(locationModels: List<LocationModel>): List<Location> {
        val locationCollection = mutableSetOf<Location>()
        if (locationModels.isNotEmpty()) {
            for (locationModel in locationModels) {
                locationCollection.add(transformModelToEntity(locationModel))
            }
        }
        return locationCollection.toList()
    }

    /**
     * ����� ������������� ��������� [LocationModel] � ������ [Location]
     * @param locationCollection ������� ��� �������������
     * @return ��������� [Location].
     */
    fun transformEntityListToModelList(locationCollection: List<Location>): List<LocationModel> {
        val locationModels = mutableSetOf<LocationModel>()
        if (locationModels.isNotEmpty()) {
            for (location in locationCollection) {
                locationModels.add(transformEntityToModel(location))
            }
        }
        return locationModels.toList()
    }
}