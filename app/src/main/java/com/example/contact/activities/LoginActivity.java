package com.example.contact.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contact.R;
import com.example.contact.config.StatusApp;
import com.example.contact.datebase.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity  {

    @BindView(R.id.email_tv) EditText email;
    @BindView(R.id.password_et) EditText password;
    @BindView(R.id.login_btn) Button login;
    @BindView(R.id.register_btn)Button register;

    private StatusApp statusApp;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dbHelper = new DbHelper(this);
        statusApp = new StatusApp(this);

        if (statusApp.getLogin()) {
            startActivity(new Intent(this, ContactsListActivity.class));
            finish();
        }
    }

    @OnClick({R.id.login_btn,R.id.register_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.register_btn:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void login() {
        String str_email = email.getText().toString().trim();
        String str_password = password.getText().toString().trim();
        if (dbHelper.getUser(str_email, str_password)) {
            statusApp.setLogin(true);
            startActivity(new Intent(this, ContactsListActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_email_or_password), Toast.LENGTH_SHORT).show();
        }
    }
}