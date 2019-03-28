package com.example.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.contact.R;
import com.example.contact.model.Contact;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsItemAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<Contact> list;


    public ContactsItemAdapter(Context context, List<Contact> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.view, null);

        Contact contact = list.get(position);

        ImageView image = rowView.findViewById(R.id.logo_iv);
        TextView text_name = rowView.findViewById(R.id.name_tv);
        TextView text_phone = rowView.findViewById(R.id.phone_tv);

        Picasso.get().load(contact.getImage()).placeholder(R.drawable.icon).into(image);
        text_name.setText(contact.getName());
        text_phone.setText(contact.getPhone());
        return rowView;
    }
}
