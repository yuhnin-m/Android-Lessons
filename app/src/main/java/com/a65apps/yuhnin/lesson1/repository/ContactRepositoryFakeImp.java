package com.a65apps.yuhnin.lesson1.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import com.a65apps.yuhnin.lesson1.pojo.ContactType;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelAdvanced;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;


public class ContactRepositoryFakeImp implements ContactRepository{
    private final String LOG_TAG = "contact_repository_fake";
    private static ContactRepositoryFakeImp instance;

    private Context context;

    private List<PersonModelAdvanced> personModelAdvanceds = new ArrayList<PersonModelAdvanced>();
    private List<PersonModelCompact> personModelCompacts = new ArrayList<PersonModelCompact>();
    private List<ContactInfoModel> contactInfoModels = new ArrayList<ContactInfoModel>();

    private void setContext(Context context) {
        this.context = context;
    }



    public ContactRepositoryFakeImp(Context context) {
        setContext(context);
        createPersons();
        createContacts();
    }

    private void createPersons() {
        personModelAdvanceds.add(new PersonModelAdvanced(
                "1",
                "Гагарин Юрий Алексеевич",
                "Восток-1", resourceToUri(R.drawable.avatar1), "09-03-1934"));
        personModelAdvanceds.add(new PersonModelAdvanced(
                "2",
                "Леонов Алексей Архипович",
                "Восход-2", resourceToUri(R.drawable.avatar2), "30-05-1934"));
        personModelAdvanceds.add(new PersonModelAdvanced(
                "3",
                "Титов Герман Степанович",
                "Восток-2", resourceToUri(R.drawable.avatar3), "11-09-1935"));
        personModelCompacts.add(new PersonModelCompact("1",
                "Гагарин Юрий Алексеевич",
                "Позывной: Кедр", resourceToUri(R.drawable.avatar1)));
        personModelCompacts.add(new PersonModelCompact("2",
                "Леонов Алексей Архипович",
                "Позывной: Алмаз-2, Союз-1", resourceToUri(R.drawable.avatar2)));
        personModelCompacts.add(new PersonModelCompact("3",
                "Титов Герман Степанович",
                "Позывной: Орёл", resourceToUri(R.drawable.avatar3)));
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

    @NonNull
    private List<ContactInfoModel> getContacts(@NonNull final String personId) {
        List<ContactInfoModel> foundContacts = new ArrayList<ContactInfoModel>();
        for (ContactInfoModel contact : contactInfoModels) {
            if (contact.getPersonId().equals(personId)) {
                foundContacts.add(contact);
            }
        }
        return foundContacts;
    }

    @Nullable
    private PersonModelAdvanced getPerson(@NonNull final String personId) {
        for (PersonModelAdvanced personModelAdvanced : personModelAdvanceds) {
            if (personModelAdvanced.getId().equals(personId)) {
                return personModelAdvanced;
            }
        }
        return null;
    }

    @Nullable
    private List<PersonModelCompact> getPersonList() {
        return personModelCompacts;
    }

    @Override
    public Single<List<PersonModelCompact>> getAllPersons(@Nullable String searchString) {
        return Single.fromCallable(() -> getPersonList());
    }

    @Override
    public Single<List<ContactInfoModel>> getContactByPerson(@NonNull String id) {
        return Single.fromCallable(() -> getContacts(id));
    }

    @Override
    public Single<PersonModelAdvanced> getPersonById(@NonNull String id) {
        return Single.fromCallable(() -> getPerson(id));
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
