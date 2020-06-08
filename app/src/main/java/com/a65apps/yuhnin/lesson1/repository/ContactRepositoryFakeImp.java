package com.a65apps.yuhnin.lesson1.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.ContactType;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ContactRepositoryFakeImp  implements ContactRepository{
    private final String LOG_TAG = "contact_repository_fake";
    private static ContactRepositoryFakeImp instance;

    private Context context;

    public static synchronized ContactRepositoryFakeImp getInstance(Context context) {
        if (instance == null) {
            instance = new ContactRepositoryFakeImp();
        }
        instance.setContext(context);
        return instance;
    }


    private List<PersonModelAdvanced> personModelAdvanceds = new ArrayList<PersonModelAdvanced>();
    private List<PersonModelCompact> personModelCompacts = new ArrayList<PersonModelCompact>();
    private List<ContactInfoModel> contactInfoModels = new ArrayList<ContactInfoModel>();

    private void setContext(Context context) {
        this.context = context;
    }


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
        personModelAdvanceds.add(new PersonModelAdvanced(
                "1",
                "Гагарин Юрий Алексеевич",
                "Восток-1", resourceToUri(R.drawable.avatar1), "09-03-1934"));

        personModelAdvanceds.add(new PersonModelAdvanced(
                "2",
                "Леонов Алексей Архипович",
                "Восход-2", resourceToUri(R.drawable.avatar2), "30-05-1934"));

        personModelAdvanceds.add(new PersonModelAdvanced("3","Титов Герман Степанович",
                "Восток-2", resourceToUri(R.drawable.avatar3), "11-09-1935"));

        personModelCompacts.add(new PersonModelCompact("1","Гагарин Юрий Алексеевич", resourceToUri(R.drawable.avatar1)));
        personModelCompacts.add(new PersonModelCompact("2", "Леонов Алексей Архипович", resourceToUri(R.drawable.avatar2)));
        personModelCompacts.add(new PersonModelCompact("3","Титов Герман Степанович", resourceToUri(R.drawable.avatar3)));
    }


    private void createContacts() {
        contactInfoModels.add(new ContactInfoModel(1, "1",
                ContactType.PHONE_NUMBER, "+71111111111"));
        contactInfoModels.add(new ContactInfoModel(2, "1",
                ContactType.EMAIL, "gagarin@cosmonauts.su"));
        contactInfoModels.add(new ContactInfoModel(3, "1",
                ContactType.PHONE_NUMBER, "+71111111112"));
        contactInfoModels.add(new ContactInfoModel(4, "1",
                ContactType.EMAIL, "y.gagarin@vvs.su"));

        contactInfoModels.add(new ContactInfoModel(5, "2",
                ContactType.PHONE_NUMBER, "+72222222222"));
        contactInfoModels.add(new ContactInfoModel(6, "2",
                ContactType.EMAIL, "leonov@cosmonauts.su"));
        contactInfoModels.add(new ContactInfoModel(7, "2",
                ContactType.PHONE_NUMBER, "+72222222223"));
        contactInfoModels.add(new ContactInfoModel(8, "2",
                ContactType.EMAIL, "laa@vvs.su"));

        contactInfoModels.add(new ContactInfoModel(9, "3",
                ContactType.PHONE_NUMBER, "+73333333333"));
        contactInfoModels.add(new ContactInfoModel(10, "3",
                ContactType.EMAIL, "leonov@cosmonauts.su"));
        contactInfoModels.add(new ContactInfoModel(11, "3",
                ContactType.PHONE_NUMBER, "+73333333334"));
    }

    @Override
    public List<PersonModelCompact> getAllPersons() {
        return personModelCompacts;
    }


    @Override
    public List<ContactInfoModel> getContactByPerson(String id) {
        List<ContactInfoModel> foundContacts = new ArrayList<ContactInfoModel>();
        for (ContactInfoModel contact : contactInfoModels) {
            if (contact.getPersonId().equals(id)) {
                foundContacts.add(contact);
            }
        }
        return foundContacts;
    }

    @Override
    public PersonModelAdvanced getPersonById(String id) {
        for (PersonModelAdvanced personModelAdvanced : personModelAdvanceds) {
            if (personModelAdvanced.getId().equals(id)) {
                return personModelAdvanced;
            }
        }
        return null;
    }

    public Uri resourceToUri(int resID) {
        if (context != null) {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    this.context.getResources().getResourcePackageName(resID) + '/' +
                    this.context.getResources().getResourceTypeName(resID) + '/' +
                    this.context.getResources().getResourceEntryName(resID) );
        } else
            return null;
    }
}
