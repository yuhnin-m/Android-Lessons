package com.a65apps.yuhnin.lesson1.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;
import com.a65apps.yuhnin.lesson1.ui.listeners.OnPersonClickedListener;

import java.util.List;

public class PersonListAdapter extends ListAdapter<PersonModelCompact, PersonListAdapter.PersonViewHolder> {

    @NonNull
    OnPersonClickedListener personClickedListener;

    public PersonListAdapter(OnPersonClickedListener personClickedListener) {
        super(DIFF_CALLBACK);
        this.personClickedListener = personClickedListener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_person_list_item, parent, false);
        return new PersonViewHolder(view, personClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setItems(@NonNull List<PersonModelCompact> personList) {
        submitList(personList);
    }

    public static final DiffUtil.ItemCallback<PersonModelCompact> DIFF_CALLBACK = new DiffUtil.ItemCallback<PersonModelCompact>() {
        @Override
        public boolean areItemsTheSame(@NonNull PersonModelCompact oldItem, @NonNull PersonModelCompact newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PersonModelCompact oldItem, @NonNull PersonModelCompact newItem) {
            return oldItem.getDisplayName().equals(newItem.getDisplayName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getImageUri().equals(newItem.getImageUri());
        }
    };

    /**
     * Предоставляет прямую ссылку на каждый View-компонент
     * Используется для кэширования View-компонентов и последующего быстрого доступа к ним
     **/
    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Nullable
        TextView txtName;
        @Nullable
        TextView txtDescription;
        @Nullable
        ImageView ivAvatar;
        @Nullable
        OnPersonClickedListener clickedListener;

        public PersonViewHolder(@NonNull View itemView, final OnPersonClickedListener clickedListener) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tvName);
            txtDescription = itemView.findViewById(R.id.tvSubtext);
            ivAvatar =  itemView.findViewById(R.id.imageAvatar);
            this.clickedListener = clickedListener;
            itemView.setOnClickListener(this);
        }

        public void bind(PersonModelCompact person) {
            if (txtName != null) {
                txtName.setText(person.getDisplayName());
            }
            if (txtDescription != null) {
                txtDescription.setText(person.getDescription());
            }
            if (ivAvatar != null) {
                ivAvatar.setImageURI(person.getImageUri());
            }
        }

        @Override
        public void onClick(View v) {
            if (clickedListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickedListener.onItemClick(getItem(position).getId());
                }
            }
        }
    }
}
