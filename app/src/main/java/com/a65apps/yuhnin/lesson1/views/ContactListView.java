package com.a65apps.yuhnin.lesson1.views;

import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

public interface ContactListView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void fetchContactList(List<PersonModelCompact> personList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void fetchError(String message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showProgressBar();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideProgressBar();

}
