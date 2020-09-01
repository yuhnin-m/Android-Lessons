package com.a65apps.library.repositories

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.a65apps.core.entities.Person
import com.a65apps.core.interactors.persons.PersonListRepository
import com.a65apps.library.Constants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val LOG_TAG: String = "person_repository"

class PersonListRepositoryFromSystem(val context: Context) : PersonListRepository {

    override fun getAllPersonsFlow(searchString: String) = flow {
        emit(getPersonList(searchString))
    }

    override suspend fun getAllPersons(searchString: String): List<Person> {
        return suspendCoroutine {
            try {
                val listOfPersons = getPersonList(searchString)
                it.resume(listOfPersons)
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
        }
    }

    private fun getPersonList(searchString: String): List<Person> {
        var personList = mutableListOf<Person>()
        val contentResolver = context.contentResolver
        val cursor: Cursor?
        cursor = if (searchString.isNotEmpty()) {
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null)
        } else {
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE \'%" + searchString + "%\'",
                    null, null)
        }

        try {
            cursor?.let {
                while (cursor.moveToNext()) {
                    try {
                        val id: String? = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                        val displayName: String? = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        var strPhotoUri: String? = it.getString(it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                        strPhotoUri = strPhotoUri ?: Constants.URI_DRAWABLE_AVATAR_NOT_FOUND
                        Log.d(LOG_TAG, "Найден контакт: id=$id; ФИО: $displayName фото=$strPhotoUri")
                        if (id != null && displayName != null) {
                            personList.add(Person(id, displayName, null, strPhotoUri, null))
                        }
                    } catch (ex: Exception) {
                        Log.d(LOG_TAG, "Произошла ошибка получения контакта: ${ex.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(LOG_TAG, "Произошла ошибка получения списка контактов: ${e.message}")
        } finally {
            cursor?.close()
        }
        Log.d(LOG_TAG, "Найдено " + personList.size + " контактов")

        return personList.toList()
    }

}