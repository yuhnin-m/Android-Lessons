package com.a65apps.library.mapper

import com.a65apps.core.entities.Location
import com.a65apps.library.models.LocationModel


class LocationModelMapper {

    /**
     * Трансформация класса [LocationModel] в класс [Location]
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
     * Трансформация класса [Location] в класс [LocationModel]
     * @param location Object to be transformed.
     * @return [LocationModel].
     */
    fun transformEntityToModel(location: Location) = with(location) {
        LocationModel(personId, address ?: "", longitude, latitude)
    }


    /**
     * Метод трансформации коллекции [LocationModel] в список [Location]
     * @param locationModels объекты для трансформации
     * @return Коллекция [Location].
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
     * Метод трансформации коллекции [LocationModel] в список [Location]
     * @param locationCollection объекты для трансформации
     * @return Коллекция [Location].
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