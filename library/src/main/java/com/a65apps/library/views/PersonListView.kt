package com.a65apps.library.views

import com.a65apps.library.models.PersonModelCompact
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface PersonListView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun fetchContactList(personList: List<PersonModelCompact>?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun fetchError(message: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgressBar()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgressBar()

}