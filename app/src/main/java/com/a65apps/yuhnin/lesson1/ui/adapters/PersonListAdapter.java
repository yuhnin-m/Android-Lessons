package com.a65apps.yuhnin.lesson1.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.PersonModelCompact;

import java.util.List;

public class PersonListAdapter extends BaseAdapter {

    Context context;
    List<PersonModelCompact> personList;

    public PersonListAdapter(Context context, List<PersonModelCompact> personList){
        this.context = context;
        this.personList = personList;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.personList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.personList.get(i).getId().hashCode();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_person_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.txtPhones = (TextView) convertView.findViewById(R.id.tvSubtext);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.imageAvatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(personList.get(position).getDisplayName());
        viewHolder.txtPhones.setText("Подробности: " + personList.get(position).getId());
        viewHolder.avatar.setImageURI(personList.get(position).getImageUri());
        viewHolder.id = personList.get(position).getId();

        return convertView;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtPhones;
        ImageView avatar;
        String id;
    }
}
