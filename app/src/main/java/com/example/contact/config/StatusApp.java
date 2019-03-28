package com.example.contact.config;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.contact.R;

public class StatusApp {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public StatusApp(Context context) {
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLogin(boolean login) {
        editor.putBoolean("login", login);
        editor.commit();
    }

    public boolean getLogin() {
        return prefs.getBoolean("login", false);
    }
}
