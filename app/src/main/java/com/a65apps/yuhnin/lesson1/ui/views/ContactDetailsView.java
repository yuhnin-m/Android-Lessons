package com.a65apps.yuhnin.lesson1.ui.views;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

public interface ContactDetailsView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void getContactDetails(PersonModelAdvanced personModel);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void getContactsInfo(List<ContactInfoModel> listOfContacts);
}
