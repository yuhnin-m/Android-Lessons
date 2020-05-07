package com.a65apps.yuhnin.lesson1.ui.listeners;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import java.util.List;

public interface ContactsResultListener {
    void onFetchContacts(List<ContactInfoModel> contactInfoModels);
}
