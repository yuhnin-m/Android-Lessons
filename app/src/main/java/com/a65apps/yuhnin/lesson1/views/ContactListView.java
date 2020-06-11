package com.a65apps.yuhnin.lesson1.views;

import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface ContactListView extends MvpView {
    void getContactList(List<PersonModelCompact> personList);
}
