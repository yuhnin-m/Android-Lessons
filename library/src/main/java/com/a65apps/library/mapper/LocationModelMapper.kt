package com.a65apps.library.mapper

import com.a65apps.core.entities.Location
import com.a65apps.library.models.LocationModel


class LocationModelMapper {

    /**
     * Трансформация класса [LocationModel] в класс [Location]
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
     * Метод трансформации коллекции [LocationModel] в список [Location]
     * @param locationModels объекты для трансформации
     * @return Коллекция [Location].
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