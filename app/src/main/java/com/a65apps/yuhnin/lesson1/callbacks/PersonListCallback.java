package com.a65apps.yuhnin.lesson1.callbacks;

import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;

import java.util.List;

public interface PersonListCallback {
    void getPersonList(List<PersonModelCompact> personList);
}
