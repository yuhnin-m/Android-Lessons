package com.a65apps.library.views;

import com.a65apps.core.entities.Contact;
import com.a65apps.library.models.ContactModel;
import com.a65apps.library.models.PersonModelAdvanced;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

public interface PersonDetailsView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void fetchContactDetails(PersonModelAdvanced personModel);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void fetchContactsInfo(List<ContactModel> listOfContacts);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void fetchError(String errorMessage);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showProgressBar();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideProgressBar();
}
