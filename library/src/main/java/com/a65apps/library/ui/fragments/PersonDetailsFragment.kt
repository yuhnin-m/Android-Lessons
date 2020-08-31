package com.a65apps.library.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.isVisible
import com.a65apps.library.Constants
import com.a65apps.library.R
import com.a65apps.library.di.containers.HasAppContainer
import com.a65apps.library.models.ContactModel
import com.a65apps.library.models.PersonModelAdvanced
import com.a65apps.library.presenters.PersonDetailsPresenter
import com.a65apps.library.ui.adapters.ContactListAdapter
import com.a65apps.library.ui.listeners.EventActionBarListener
import com.a65apps.library.ui.listeners.OnPersonSetLocation
import com.a65apps.library.views.PersonDetailsView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_contact_details.*
import javax.inject.Inject
import javax.inject.Provider

private const val LOG_TAG = "details_fragment"

class PersonDetailsFragment : MvpAppCompatFragment(), PersonDetailsView,
        CompoundButton.OnCheckedChangeListener {
    private var eventActionBarListener: EventActionBarListener? = null
    private var onPersonSetLocation: OnPersonSetLocation? = null
    private var personId: String = ""
    private var contactInfoList: List<ContactModel>? = null
    private lateinit var person: PersonModelAdvanced
    @Inject
    lateinit var detailsPresenterProvider: Provider<PersonDetailsPresenter>

    @InjectPresenter
    lateinit var personDetailsPresenter: PersonDetailsPresenter

    @ProvidePresenter
    fun providerContactDetailsPresenter(): PersonDetailsPresenter {
        return detailsPresenterProvider.get()
    }


    companion object {
        fun newInstance(): PersonListFragment = PersonListFragment()
    }

    override fun onAttach(context: Context) {
        var app = requireActivity().application
        check(app is HasAppContainer)
        var contactDetailsContainer = (app as HasAppContainer).appContainer()
                .plusPersonDetailsComponent()
        contactDetailsContainer.inject(this)
        if (context is EventActionBarListener) {
            eventActionBarListener = context
        }
        if (context is OnPersonSetLocation) {
            onPersonSetLocation = context
        }
        super.onAttach(context)
    }

    override fun onDetach() {
        eventActionBarListener = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            personId = arguments?.getString(Constants.KEY_PERSON_ID) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnOpenSetLocation.setOnClickListener {
            person?.let {
                onPersonSetLocation?.onPersonSetLocation(it.id)
            }
        }
        togglebtnRemindBirthday?.setOnCheckedChangeListener(this)
    }

    override fun onResume() {
        eventActionBarListener?.setVisibleToolBarBackButton(true)
        personDetailsPresenter.requestContactsByPerson(personId)
        requireActivity().title = getString(R.string.toolbar_header_person_details)
        super.onResume()
    }

    private fun updateFields() {
        togglebtnRemindBirthday?.let {
            it.isEnabled = (person.dateBirthday != null)
            val reminderEnabled = personDetailsPresenter.checkBirthdayReminderEnabled(person!!.id)
            it.isChecked = reminderEnabled
            it.setText(R.string.button_text_remind_birthday_on)
            Log.d(LOG_TAG, "Напоминание " + if (reminderEnabled) " включено" else " выключено")
        }
        imageviewAvatar.setImageURI(person.imageUri)
        textviewFullname.text = person.fullName
        textviewDescription.text = person.description
        val birthday = if (person.stringBirthday.isNullOrEmpty())
            getString(R.string.text_birthday_notset)
        else person.stringBirthday
        textviewBirthday.text = birthday

    }
    override fun fetchContactDetails(personModel: PersonModelAdvanced) {
        person = personModel
        updateFields()
        Log.d(LOG_TAG, "Создаем список контактных данных контакта " + person.fullName)
    }

    override fun fetchContactsInfo(listOfContacts: List<ContactModel>) {
        contactInfoList = listOfContacts
        listviewContacts?.let {
            val contactListAdapter = ContactListAdapter(context, contactInfoList)
            it.adapter = contactListAdapter
        }
    }

    override fun fetchError(errorMessage: String) {
        Toast.makeText(requireActivity().applicationContext, errorMessage, Toast.LENGTH_SHORT)
                .show()
    }

    override fun showProgressBar() {
        progressbarLoadDetails?.isVisible = true;
    }

    override fun hideProgressBar() {
        progressbarLoadDetails?.isVisible = false;
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            Log.d(LOG_TAG, "Напоминания включены")
            personDetailsPresenter.birthdayReminderEnable(person)
        } else {
            Log.d(LOG_TAG, "Напоминания выключены")
            personDetailsPresenter.birthdayReminderDisable(personId)
        }
    }

}