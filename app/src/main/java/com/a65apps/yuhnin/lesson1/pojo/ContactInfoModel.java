package com.a65apps.yuhnin.lesson1.pojo;

public class ContactInfoModel {
    private final long id;
    private final long personId;
    private final ContactType contactType;
    private final String value;

    public ContactInfoModel(long id, long personId, ContactType contactType, String value) {
        this.id = id;
        this.personId = personId;
        this.contactType = contactType;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public long getPersonId() {
        return personId;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public String getValue() {
        return value;
    }
}
