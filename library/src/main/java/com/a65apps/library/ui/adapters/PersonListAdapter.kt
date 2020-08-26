package com.a65apps.library.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.a65apps.library.R
import com.a65apps.library.models.PersonModelCompact
import com.a65apps.library.ui.listeners.OnPersonClickedListener
import kotlinx.android.synthetic.main.layout_person_list_item.view.*


class PersonListAdapter(val clickedListener: OnPersonClickedListener): ListAdapter<PersonModelCompact,
        PersonListAdapter.PersonViewHolder>(DIFF_CALLBACK) {

    companion object{
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<PersonModelCompact> = object : DiffUtil.ItemCallback<PersonModelCompact>() {
            override fun areItemsTheSame(oldItem: PersonModelCompact, newItem: PersonModelCompact): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: PersonModelCompact, newItem: PersonModelCompact): Boolean {
                return oldItem.displayName == newItem.displayName &&
                        oldItem.description == newItem.description &&
                        oldItem.photoPreviewUri == newItem.photoPreviewUri
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_person_list_item, parent, false)
        return PersonViewHolder(view, clickedListener, this)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setItems(personList: List<PersonModelCompact>) {
        submitList(personList)
    }

    class PersonViewHolder(itemView: View, val clickedListener: OnPersonClickedListener,
                           val personListAdapter: PersonListAdapter) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            var position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickedListener.onItemClick(personListAdapter.currentList[position].id)
            }
        }

        fun bind(person: PersonModelCompact) {
            itemView.textviewName.text = person.displayName
            itemView.textviewSubtext.text = person.description
            itemView.imageAvatar.setImageURI(person.photoPreviewUri)
        }
    }
}