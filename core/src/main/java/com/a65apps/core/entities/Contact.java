package com.a65apps.core.entities;

import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * Класс сущность контактной информации.
 * Может хранить в себе номера телефонов, адреса электронной почты
 */
public class Contact {

    /** Поле - идентификатор контактной информации */
    @NonNull
    private final long id;

    /** Поле идентификатор персоны*/
    @NonNull
    private final String personId;

    /** Тип контакта */
    @NonNull
    private final ContactType contactType;

    /** Значение */
    @NonNull
    private final String value;

    /**
     * Конструктор модели информации о контакте
     * @param id идентификатор
     * @param personId Id контакта
     * @param contactType Тип контактой информации
     * @param value Значение контактной информации
     */
    public Contact(long id, String personId, ContactType contactType, String value) {
        this.id = id;
        this.personId = personId;
        this.contactType = contactType;
        this.value = value;
    }

    /**
     * Метод для получения идентификатора контактной информации
     * @return идентификатор контактной информации
     */
    @NonNull
    public long getId() {
        return id;
    }

    /**
     * Метод возвращающий идентфикатор персоны
     * @return идентификатор персоны, которому относится эта контактная информация
     */
    @NonNull
    public String getPersonId() {
        return personId;
    }

    /**
     * Метод возвращающий тип контакта
     * @return тип контакта
     */
    public ContactType getContactType() {
        return contactType;
    }

    /**
     * Метод возвращающий значение контактной информации: телефон или email
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
