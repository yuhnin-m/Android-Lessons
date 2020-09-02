package com.a65apps.library.views

import com.a65apps.library.models.ContactModel
import com.a65apps.library.models.PersonModelAdvanced
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface PersonDetailsView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun fetchContactDetails(personModel: PersonModelAdvanced)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun fetchContactsInfo(listOfContacts: List<ContactModel>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun fetchError(errorMessage: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgressBar()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgressBar()
}