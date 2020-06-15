package com.a65apps.yuhnin.lesson1.callbacks;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;

import java.util.List;

public interface PersonDetailsCallback {
    void onFetchPersonDetails(PersonModelAdvanced personModel);
    void onFetchPersonContacts(List<ContactInfoModel> contactList);
}
