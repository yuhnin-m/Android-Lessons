package com.a65apps.library.repositories

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.a65apps.core.entities.Contact
import com.a65apps.core.entities.ContactType
import com.a65apps.core.entities.Person
import com.a65apps.core.interactors.contacts.PersonDetailsRepository
import com.a65apps.library.Constants
import kotlinx.coroutines.flow.flow

private const val LOG_TAG = "contact_list_repository"
private val KEY_CONTENT_PHONE = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
private val KEY_CONTENT_EMAIL = ContactsContract.CommonDataKinds.Email.CONTENT_URI
private const val KEY_CONTENT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID

class PersonDetailsRepositoryFromSystem(private val context: Context) : PersonDetailsRepository {
    /**
     * Переопределенный метод запроса контактной информации
     * @param personId идентификатор контакта
     * @return Flow со списком контактной информации
     */
    override fun getContactsByPerson(personId: String) = flow {
        emit(getContacts(personId))
    }

    /**
     * Переопределенный метод запроса деталей о контакте
     * @param personId идентификатор контакта
     * @return Flow с [Person]
     */
    override fun getPersonDetails(personId: String) = flow {
        getPerson(personId)?.let {
            emit(it)
        }
    }

    /**
     * Метод получения информации о контакте
     * @param personId идентификатор контакта
     * @return возвращает экземпляр PersonModelAdvanced
     */
    private fun getPerson(personId: String): Person? {
        val cursor = context.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                "${ContactsContract.Contacts._ID} = $personId", null, null)
        cursor?.use {
            try {
                it.moveToNext()
                val displayName = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                val strPhotoUri = it.getString(it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                val dateBirthDay: String = getDateBirthday(id, context.contentResolver)
                val description: String = getCompanyName(id, context.contentResolver)
                return Person(
                        personId,
                        displayName,
                        description,
                        strPhotoUri ?: Constants.URI_DRAWABLE_AVATAR_NOT_FOUND,
                        dateBirthDay)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Ошибка получения информации о контакте" + e.message)
            }
        }
        return null
    }

    /**
     * Метод возвращающий название организации контакта с определенным ID
     * FIXME: Не работает, выдает все подряд
     * @param personId Идентификатор контакта
     * @param contentResolver Экземпляр ContentResolver
     * @return Имя компании и сопутствующие данные
     */
    private fun getCompanyName(personId: String, contentResolver: ContentResolver): String {
        Log.d(LOG_TAG, "Читаем место работы контакта id=$personId")
        var personJobDescription = ""
        val cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null,
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = $personId",
                null, null)
        cursor?.use {
            while (it.moveToNext()) {
                personJobDescription += it.getString(it.getColumnIndex(
                        ContactsContract.CommonDataKinds.Organization.COMPANY)) + " "
            }
        }
        return personJobDescription
    }

    /**
     * Метод для получения списка контактной информации у контакта
     * @param personId идентификатор контакта
     * @return список с контактной информацией
     */
    private fun getContacts(personId: String): List<Contact> {
        val contactInfoModels: MutableList<Contact> = ArrayList()
        try {
            val phoneNumbers: List<Contact> = getContactsByType(ContactType.PHONE_NUMBER, personId, context.contentResolver)
            contactInfoModels.addAll(phoneNumbers)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Ошибка чтения телефонов контакта id=$personId: ${e.message}")
        }
        try {
            val emails: List<Contact> = getContactsByType(ContactType.EMAIL, personId, context.contentResolver)
            contactInfoModels.addAll(emails)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Ошибка чтения списка эл.почт контакта $personId : ${e.message}")
        }
        return contactInfoModels
    }

    /**
     * Метод возвращающий дату рождения для определенного контакта с заданным ID
     * FIXME: Не работает абсолютно. На Xiaomi выдает все что угодно, кроме даты рождения
     * @param id Идентификатор контакта
     * @param contentResolver Экземпляр ContentResolver
     * @return строка - дата рождения
     */
    private fun getDateBirthday(id: String, contentResolver: ContentResolver): String {
        val uri = ContactsContract.Data.CONTENT_URI
        var birthDay = ""
        val projection = arrayOf(
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
        )
        val where = ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + " AND " +
                ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = " + id
        val cursorBirthday = contentResolver.query(
                uri, projection, where, null, null, null)
        cursorBirthday?.use {
            while (it.moveToNext()) {
                birthDay = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
                Log.d(LOG_TAG, "Найден день рождения контакта $id: $birthDay")
            }
        }
        return birthDay
    }

    /**
     * Метод, в зависимости от параметра, возвращающий номера телефонов или адреса
     * электронной почты для определенного контакта с заданным ID
     * @param contactType тип контакта из [ContactType]
     * @param personId идентификатор контакта
     * @param contentResolver экземпляр ContentResolver
     * @return Список номеров телефонов, либо адресов email
     */
    private fun getContactsByType(contactType: ContactType, personId: String, contentResolver: ContentResolver):
            List<Contact> {
        Log.d(LOG_TAG, "Читаем номера телефонов по id=$personId")
        val contactList: MutableList<Contact> = ArrayList()
        val uri = when (contactType) {
            ContactType.EMAIL -> KEY_CONTENT_EMAIL
            ContactType.PHONE_NUMBER -> KEY_CONTENT_PHONE
            else -> {
                throw Exception("Невозможно запросить тип контактных данных $contactType")
            }
        }
        val cursor = contentResolver.query(uri, null,
                "$KEY_CONTENT_ID = $personId",
                null, null)
        cursor?.use {
            try {
                while (it.moveToNext()) {
                    val value = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val valueId = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                    if (!value.isNullOrEmpty() && !valueId.isNullOrEmpty()) {
                        try {
                            contactList.add(Contact(
                                    id = valueId.toLong(),
                                    personId = personId,
                                    contactType = contactType,
                                    value = value))
                        } catch (e1: NumberFormatException) {
                            Log.d(LOG_TAG, "Ошибка при обработке контакта $personId" +
                                    "Невозможно распарсить ID $valueId")
                            continue
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Ошибка чтения номеров контакта $personId: ${e.message}")
            }
        }
        return contactList
    }
}
