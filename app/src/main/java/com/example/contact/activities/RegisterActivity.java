package com.example.contact.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contact.R;
import com.example.contact.datebase.DbHelper;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.email_tv) EditText email;
    @BindView(R.id.password_et) EditText password;
    @BindView(R.id.register_btn) Button btn_register;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        dbHelper = new DbHelper(this);
    }


    @OnClick({R.id.register_btn})
    public void onclick()
    {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.register_successful), Toast.LENGTH_SHORT).show();
        String str_email = email.getText().toString().trim();
        String str_password = password.getText().toString().trim();
        if (!isEmail(str_email)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.fields_empty), Toast.LENGTH_SHORT).show();
        } else if (isvalidPassword(str_password)) {
            dbHelper.addUser(str_email, str_password);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.register_successful), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    boolean isEmail(String str_email) {
        return (!TextUtils.isEmpty(str_email) && Patterns.EMAIL_ADDRESS.matcher(str_email).matches());
    }

    boolean isvalidPassword(String str_password) {
        if (str_password.length() > 15 || str_password.length() < 8) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_lenght_password), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!str_password.matches("(.*[A-Z].*)")) {

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_uppercase_password), Toast.LENGTH_SHORT).show();
            return false;

        } else if (!str_password.matches("(.*[a-z].*)")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_lower_case), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!str_password.matches("(.*[0-9].*)")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_number_password), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }
}