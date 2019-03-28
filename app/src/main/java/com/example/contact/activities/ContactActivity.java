package com.example.contact.activities;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact.R;
import com.example.contact.model.Contact;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends AppCompatActivity {
    @BindView(R.id.name_tv) TextView name;
    @BindView(R.id.surname_tv)TextView surname;
    @BindView(R.id.phone_tv)TextView phone;
    @BindView(R.id.email_tv)TextView email;
    @BindView(R.id.contact_iv) ImageView contactImage;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        contact = (Contact) bundle.getSerializable("contact");
        name.setText(contact.getName());
        surname.setText(contact.getSurname());
        phone.setText(contact.getPhone());
        email.setText(contact.getEmail());
        Picasso.get().load(contact.getImage()).placeholder(R.drawable.icon).into(contactImage);
    }

    @OnClick(R.id.phone_tv)
    public void onClick() {

        String phoneNumber = String.format("tel: %s", phone.getText().toString());
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse(phoneNumber));
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            Log.e("log", "Can't resolve app for ACTION_DIAL Intent.");
        }
    }

    @OnClick(R.id.email_tv)
    public void emailSend(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("mailto:"));
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL  , email.getText().toString());
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}