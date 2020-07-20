package com.a65apps.library.views;

import com.a65apps.library.models.LocationModel;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface PersonMapView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void drawMarker(LocationModel locationModel);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void onPersonLocationLoad(LocationModel locationModel);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void onPersonLocationSaved(LocationModel locationModel);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void onError(String errorMessage);
}
