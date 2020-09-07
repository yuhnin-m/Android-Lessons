package com.a65apps.library.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var contactListAdapter: ContactListAdapter? = null
    private lateinit var viewManager: RecyclerView.LayoutManager

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = LinearLayoutManager(requireContext())

        btnOpenSetLocation.setOnClickListener {
            onPersonSetLocation?.onPersonSetLocation(person.id, person.displayName)
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
        with(togglebtnRemindBirthday) {
            isEnabled = !person.dateBirthday.isNullOrEmpty()
            isChecked = personDetailsPresenter.checkBirthdayReminderEnabled(person.id)
            setText(R.string.button_text_remind_birthday_on)
            Log.d(LOG_TAG, "Birthday reminder is " + if (isChecked) " enabled" else " disabled")
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
        val contactListAdapter = ContactListAdapter(listOfContacts)
        recyclerviewContacts.layoutManager = viewManager
        recyclerviewContacts.adapter = contactListAdapter
    }

    override fun fetchError(errorMessage: String) {
        Toast.makeText(requireActivity().applicationContext, errorMessage, Toast.LENGTH_SHORT)
                .show()
    }

    override fun showProgressBar() {
        progressbarLoadDetails.isVisible = true
    }

    override fun hideProgressBar() {
        progressbarLoadDetails.isVisible = false
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            personDetailsPresenter.birthdayReminderEnable(person)
        } else {
            personDetailsPresenter.birthdayReminderDisable(personId)
        }
    }
}