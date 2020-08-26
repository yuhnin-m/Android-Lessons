package com.a65apps.core.entities;

import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * ����� �������� ���������� ����������.
 * ����� ������� � ���� ������ ���������, ������ ����������� �����
 */
public class Contact {

    /** ���� - ������������� ���������� ���������� */
    @NonNull
    private final long id;

    /** ���� ������������� �������*/
    @NonNull
    private final String personId;

    /** ��� �������� */
    @NonNull
    private final ContactType contactType;

    /** �������� */
    @NonNull
    private final String value;

    /**
     * ����������� ������ ���������� � ��������
     * @param id �������������
     * @param personId Id ��������
     * @param contactType ��� ��������� ����������
     * @param value �������� ���������� ����������
     */
    public Contact(long id, String personId, ContactType contactType, String value) {
        this.id = id;
        this.personId = personId;
        this.contactType = contactType;
        this.value = value;
    }

    /**
     * ����� ��� ��������� �������������� ���������� ����������
     * @return ������������� ���������� ����������
     */
    @NonNull
    public long getId() {
        return id;
    }

    /**
     * ����� ������������ ������������ �������
     * @return ������������� �������, �������� ��������� ��� ���������� ����������
     */
    @NonNull
    public String getPersonId() {
        return personId;
    }

    /**
     * ����� ������������ ��� ��������
     * @return ��� ��������
     */
    public ContactType getContactType() {
        return contactType;
    }

    /**
     * ����� ������������ �������� ���������� ����������: ������� ��� email
     * @return
     */
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personId, contactType, value);
    }
}
