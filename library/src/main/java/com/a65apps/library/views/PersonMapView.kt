package com.a65apps.library.views

import com.a65apps.core.entities.Location
import com.a65apps.library.models.LocationModel
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface PersonMapView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun drawMarker(locationModel: Location)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onPersonLocationLoad(locationModel: Location)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onPersonLocationSaved(locationModel: Location)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onError(errorMessage: String)
}