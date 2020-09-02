package com.a65apps.library.ui.fragments

import android.content.Context
import android.net.Uri
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
        val app = requireActivity().application
        check(app is HasAppContainer)
        val contactDetailsContainer = (app as HasAppContainer).appContainer()
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
        personId = arguments?.getString(Constants.KEY_PERSON_ID) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnOpenSetLocation.setOnClickListener {
            person.let {
                onPersonSetLocation?.onPersonSetLocation(it.id)
            }
        }
        togglebtnRemindBirthday.setOnCheckedChangeListener(this)
    }

    override fun onResume() {
        eventActionBarListener?.setVisibleToolBarBackButton(true)
        personDetailsPresenter.requestContactsByPerson(personId)
        requireActivity().title = getString(R.string.toolbar_header_person_details)
        super.onResume()
    }

    private fun updateFields() {
        togglebtnRemindBirthday.let {
            it.isEnabled = !person.dateBirthday.isNullOrEmpty()
            it.isChecked = personDetailsPresenter.checkBirthdayReminderEnabled(person.id)
            it.setText(R.string.button_text_remind_birthday_on)
            Log.d(LOG_TAG, "Birthday reminder is " + if (it.isChecked) " enabled" else " disabled")
        }
        imageviewAvatar.setImageURI(Uri.parse(person.photoUriString))
        textviewFullname.text = person.displayName
        textviewDescription.text = person.description ?: getString(R.string.text_notset)
        textviewBirthday.text = person.dateBirthday ?: getString(R.string.text_notset)
    }

    override fun fetchContactDetails(personModel: PersonModelAdvanced) {
        person = personModel
        updateFields()
    }

    override fun fetchContactsInfo(listOfContacts: List<ContactModel>) {
        contactInfoList = listOfContacts
        recyclerviewContacts.let {
            val contactListAdapter = ContactListAdapter(listOfContacts)
            it.adapter = contactListAdapter
        }
    }

    override fun fetchError(errorMessage: String) {
        Toast.makeText(requireActivity().applicationContext, errorMessage, Toast.LENGTH_SHORT)
                .show()
    }

    override fun showProgressBar() {
        progressbarLoadDetails.isVisible = true;
    }

    override fun hideProgressBar() {
        progressbarLoadDetails.isVisible = false;
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            personDetailsPresenter.birthdayReminderEnable(person)
        } else {
            personDetailsPresenter.birthdayReminderDisable(personId)
        }
    }
}