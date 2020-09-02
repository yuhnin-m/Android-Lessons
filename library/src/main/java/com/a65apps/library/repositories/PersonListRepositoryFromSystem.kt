package com.a65apps.library.repositories

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.a65apps.core.entities.Person
import com.a65apps.core.interactors.persons.PersonListRepository
import com.a65apps.library.Constants
import io.reactivex.rxjava3.core.Single

private const val LOG_TAG: String = "person_repository"

class PersonListRepositoryFromSystem(val context: Context) : PersonListRepository {

    override fun getAllPersons(searchString: String?): Single<List<Person>> {
        return Single.fromCallable { getPersonList(searchString ?: "") }
    }

    private fun getPersonList(searchString: String): List<Person> {
        val personList = mutableListOf<Person>()
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
                        Log.d(LOG_TAG, "Found contact: id=$id; Name: $displayName Photo=$strPhotoUri")
                        if (id != null && displayName != null) {
                            personList.add(Person(id, displayName, null, strPhotoUri, null))
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