package com.a65apps.yuhnin.lesson1.ui.listeners;

import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;

import java.util.List;

public interface PersonListResultListener {
    void onFetchPersonList(List<PersonModelCompact> personModelCompacts);
}
