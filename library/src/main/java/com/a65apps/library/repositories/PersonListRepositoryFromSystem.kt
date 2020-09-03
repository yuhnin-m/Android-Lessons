package com.a65apps.library.repositories

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.a65apps.core.entities.Person
import com.a65apps.core.interactors.persons.PersonListRepository
import com.a65apps.library.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val LOG_TAG: String = "person_repository"

class PersonListRepositoryFromSystem(val context: Context) : PersonListRepository {

    override fun getAllPersonsFlow(searchString: String) = flow {
        emit(getPersonList(searchString))
    }

    override suspend fun getAllPersons(searchString: String): List<Person> = withContext(Dispatchers.IO) {
        suspendCoroutine {
            try {
                val listOfPersons = getPersonList(searchString)
                it.resume(listOfPersons)
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
        }
    }

    private fun getPersonList(searchString: String): List<Person> {
        val personList = mutableListOf<Person>()
        val contentResolver = context.contentResolver
        val cursor: Cursor?
        cursor = if (searchString.isEmpty()) {
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
                        Log.d(LOG_TAG, "Found contact: id=$id; Name: $displayName Photo=$strPhotoUri")
                        if (id != null && displayName != null) {
                            personList.add(Person(id, displayName, "", strPhotoUri, ""))
                        }
                    } catch (ex: Exception) {
                        Log.e(LOG_TAG, "Error while getting the list of contact details: ${ex.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(LOG_TAG, "Error while getting the list of contact list: ${e.message}")
        } finally {
            cursor?.close()
        }
        Log.d(LOG_TAG, "Found " + personList.size + " contacts")
        return personList.toList()
    }
}