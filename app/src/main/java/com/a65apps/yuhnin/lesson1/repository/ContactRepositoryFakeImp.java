package com.a65apps.yuhnin.lesson1.repository;

import android.util.Log;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.ContactType;
import com.a65apps.yuhnin.lesson1.pojo.PersonModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ContactRepositoryFakeImp  implements ContactRepository{
    private final String LOG_TAG = "contact_repository_fake";
    private static ContactRepositoryFakeImp instance;

    public static synchronized ContactRepositoryFakeImp getInstance() {
        if (instance == null) {
            instance = new ContactRepositoryFakeImp();
        }
        return instance;
    }


    private List<PersonModel> personModels = new ArrayList<PersonModel>();
    private List<ContactInfoModel> contactInfoModels = new ArrayList<ContactInfoModel>();


    public ContactRepositoryFakeImp() {
        try {
            createPersons();
            createContacts();
        }catch (ParseException ex) {
            Log.e(LOG_TAG, "Ошибка формата даты рождения при создании списка контактов. " +
                    ex.getMessage());
        }
    }

    private void createPersons() throws ParseException{
        personModels.add(new PersonModel(
                1,
                "Юрий","Гагарин","Алексеевич",
                "Восток-1", R.drawable.avatar1, "09-03-1934"));

        personModels.add(new PersonModel(
                2,
                "Алексей","Леонов","Архипович",
                "Восход-2", R.drawable.avatar2, "30-05-1934"));

        personModels.add(new PersonModel(3,
                "Герман","Титов","Степанович",
                "Восток-2", R.drawable.avatar3, "11-09-1935"));
    }


    private void createContacts() {
        contactInfoModels.add(new ContactInfoModel(1, 1,
                ContactType.PHONE_NUMBER, "+71111111111"));
        contactInfoModels.add(new ContactInfoModel(2, 1,
                ContactType.EMAIL, "gagarin@cosmonauts.su"));
        contactInfoModels.add(new ContactInfoModel(3, 1,
                ContactType.PHONE_NUMBER, "+71111111112"));
        contactInfoModels.add(new ContactInfoModel(4, 1,
                ContactType.EMAIL, "y.gagarin@vvs.su"));

        contactInfoModels.add(new ContactInfoModel(5, 2,
                ContactType.PHONE_NUMBER, "+72222222222"));
        contactInfoModels.add(new ContactInfoModel(6, 2,
                ContactType.EMAIL, "leonov@cosmonauts.su"));
        contactInfoModels.add(new ContactInfoModel(7, 2,
                ContactType.PHONE_NUMBER, "+72222222223"));
        contactInfoModels.add(new ContactInfoModel(8, 2,
                ContactType.EMAIL, "laa@vvs.su"));

        contactInfoModels.add(new ContactInfoModel(9, 3,
                ContactType.PHONE_NUMBER, "+73333333333"));
        contactInfoModels.add(new ContactInfoModel(10, 3,
                ContactType.EMAIL, "leonov@cosmonauts.su"));
        contactInfoModels.add(new ContactInfoModel(11, 3,
                ContactType.PHONE_NUMBER, "+73333333334"));
    }

    @Override
    public List<PersonModel> getAllPersons() {
        return personModels;
    }

    @Override
    public List<ContactInfoModel> getContactByPerson(PersonModel personModel) {
        List<ContactInfoModel> foundContacts = new ArrayList<ContactInfoModel>();
        for (ContactInfoModel contact : contactInfoModels) {
            if (contact.getPersonId() == personModel.getId()) {
                foundContacts.add(contact);
            }
        }
        return foundContacts;
    }

    @Override
    public List<ContactInfoModel> getContactByPerson(long id) {
        List<ContactInfoModel> foundContacts = new ArrayList<ContactInfoModel>();
        for (ContactInfoModel contact : contactInfoModels) {
            if (contact.getPersonId() == id) {
                foundContacts.add(contact);
            }
        }
        return foundContacts;
    }

    @Override
    public PersonModel getPersonById(long id) {
        for (PersonModel personModel : personModels) {
            if (personModel.getId() == id) {
                return personModel;
            }
        }
        return null;
    }


}
