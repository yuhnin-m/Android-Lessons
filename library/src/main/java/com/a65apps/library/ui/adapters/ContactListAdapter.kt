package com.a65apps.library.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a65apps.core.entities.ContactType
import com.a65apps.library.R
import com.a65apps.library.models.ContactModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_contact_list_item.*


class ContactListAdapter(private val contactList: List<ContactModel>) :
        RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_contact_list_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    override fun getItemCount() = contactList.size

    class ContactViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(contact: ContactModel) {
            when (contact.contactType) {
                ContactType.EMAIL -> {
                    imageviewContactsType.setImageResource(R.drawable.typeemail)
                    textviewContactsType.text = containerView.context.getText(R.string.text_contact_type_email)
                }
                ContactType.PHONE_NUMBER -> {
                    imageviewContactsType.setImageResource(R.drawable.typephone)
                    textviewContactsType.text = containerView.context.getText(R.string.text_contact_type_phone)
                }
                ContactType.SKYPE -> {
                    imageviewContactsType.setImageResource(R.drawable.typeskype)
                    textviewContactsType.text = containerView.context.getText(R.string.text_contact_type_skype)
                }
                ContactType.WEBSITE -> {
                    imageviewContactsType.setImageResource(R.drawable.typesite)
                    textviewContactsType.text = containerView.context.getText(R.string.text_contact_type_site)
                }
                else -> {
                    imageviewContactsType.setImageResource(R.drawable.typesite)
                    textviewContactsType.text = containerView.context.getText(R.string.text_notset)
                }
            }
            textviewContactsValue.text = contact.value
        }
    }
}