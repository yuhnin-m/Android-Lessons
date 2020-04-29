package com.a65apps.yuhnin.lesson1.repository;

import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;

import java.util.List;

public interface ContactRepository {

    List<PersonModel> getAllPersons();

    List<ContactInfoModel> getContactByPerson(PersonModel personModel);

    List<ContactInfoModel> getContactByPerson(long id);

    PersonModel getPersonById(long id);
}
