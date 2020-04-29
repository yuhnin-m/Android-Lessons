package com.a65apps.yuhnin.lesson1.pojo;

public class ContactInfoModel {
    private long id;
    private long personId;
    private ContactType contactType;
    private String value;

    public ContactInfoModel(long id, long personId, ContactType contactType, String value) {
        this.id = id;
        this.personId = personId;
        this.contactType = contactType;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
