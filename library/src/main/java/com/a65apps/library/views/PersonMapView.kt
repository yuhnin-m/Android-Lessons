package com.a65apps.library.views

import com.a65apps.library.models.LocationModel
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface PersonMapView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun drawMarker(locationModel: LocationModel)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onPersonLocationLoad(locationModel: LocationModel)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onPersonLocationSaved(locationModel: LocationModel)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onError(errorMessage: String)
}