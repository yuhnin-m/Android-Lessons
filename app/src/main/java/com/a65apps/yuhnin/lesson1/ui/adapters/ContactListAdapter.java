package com.a65apps.yuhnin.lesson1.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.pojo.ContactInfoModel;
import java.util.List;

public class ContactListAdapter  extends BaseAdapter {
    List<ContactInfoModel> contactList;
    Context context;

    ImageView ivContactType;
    TextView tvContactType;
    EditText etValue;

    public ContactListAdapter(Context context, List<ContactInfoModel> contactList){
        this.context = context;
        this.contactList = contactList;
    }


    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contactList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_contact_list_item, parent, false);
            ivContactType = convertView.findViewById(R.id.iv_contacts_type);
            tvContactType = convertView.findViewById(R.id.tv_contacts_type);
            etValue = convertView.findViewById(R.id.et_contacts_value);
            switch (contactList.get(position).getContactType()) {
                case EMAIL:
                    ivContactType.setImageResource(R.drawable.typeemail);
                    tvContactType.setText("Email:");
                    break;
                case PHONE_NUMBER:
                    ivContactType.setImageResource(R.drawable.typephone);
                    tvContactType.setText("Телефон:");
                    break;
            }
            etValue.setText(contactList.get(position).getValue());
        }
        return convertView;
    }
}