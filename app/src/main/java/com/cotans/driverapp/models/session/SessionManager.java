package com.cotans.driverapp.models.session;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {

    private static final String USER_INFO = "userDetails";
    private static final String TOKEN = "token";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String IS_AUTHORISED = "isAuthorised";

    private final Context context;

    public SessionManager(Context context) {
        this.context = context;
    }


    public boolean isAuthorised() {
        return context.getSharedPreferences(USER_INFO, MODE_PRIVATE).getBoolean(IS_AUTHORISED, false);
    }

    public String getToken() {
        return context.getSharedPreferences(USER_INFO, MODE_PRIVATE).getString(TOKEN, null);
    }

    public String getEmail() {
        return context.getSharedPreferences(USER_INFO, MODE_PRIVATE).getString(EMAIL, null);
    }

    public String getName() {
        return context.getSharedPreferences(USER_INFO, MODE_PRIVATE).getString(NAME, null);
    }

    // TODO redo this
    public void setAuthorised() {
        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = context.getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
        sharedPreferencesEditor.remove(IS_AUTHORISED);
        sharedPreferencesEditor.putBoolean(IS_AUTHORISED, true);
        sharedPreferencesEditor.apply();
    }

    public void unsetAuthorised() {
        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = context.getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
        sharedPreferencesEditor.remove(IS_AUTHORISED);
        sharedPreferencesEditor.putBoolean(IS_AUTHORISED, false);
        sharedPreferencesEditor.apply();
    }

    public void setToken(String token) {
        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = context.getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
        sharedPreferencesEditor.remove(TOKEN);
        sharedPreferencesEditor.putString(TOKEN, token);
        sharedPreferencesEditor.apply();
    }

    public void setEmail(String email) {
        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = context.getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
        sharedPreferencesEditor.remove(EMAIL);
        sharedPreferencesEditor.putString(EMAIL, email);
        sharedPreferencesEditor.apply();
    }

    public void setName(String name) {
        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = context.getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
        sharedPreferencesEditor.remove(NAME);
        sharedPreferencesEditor.putString(NAME, name);
        sharedPreferencesEditor.apply();
    }

    public void logout() {
        unsetAuthorised();
        setToken("");
        setEmail("");
        setName("");
    }

    public void setMessagingToken(String refreshedToken) {
        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = context.getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
        sharedPreferencesEditor.remove("messagingToken");
        sharedPreferencesEditor.putString("messagingToken", refreshedToken);
        sharedPreferencesEditor.apply();
    }
}
