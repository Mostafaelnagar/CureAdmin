package app.grand.ophthalmicadmin.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import app.grand.ophthalmicadmin.auth.model.UserData;


public class UserPreferenceHelper {
    private static UserPreferenceHelper mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "myshared";
    private static final String SHARED_FAZ3A_NAME = "Faz3aOrderRequest";
    private static final String SHARED_FILTER = "FILTER";
    private static final String SHARED_SEARCH = "SHARED_SEARCH";

    private UserPreferenceHelper(Context context) {
        mCtx = context;
    }

    //
    public static synchronized UserPreferenceHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserPreferenceHelper(context);
        }
        return mInstance;
    }

    public void userLogin(UserData userData) {
        loggout();
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userData);
        editor.putString("userData", json);
        editor.apply();
        editor.commit();

    }
    public String addUserData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("userData", null);

    }
    public UserData getUserData() {
        Gson gson = new Gson();
        String json = addUserData();
        UserData obj = gson.fromJson(json, UserData.class);
        return obj;
    }
    public void saveToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();

    }


    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }


    public boolean loggout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }


    public void setLanguage(Context context, String language) {
        Log.e("setLanguage", "setLanguage: " + language);
        SharedPreferences userDetails = context.getSharedPreferences("languageData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString("language", language);
        editor.putBoolean("haveLanguage", true);
        editor.apply();
    }

    public String getCurrentLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("languageData", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("haveLanguage", false)) return "en";
        return sharedPreferences.getString("language", "en");
    }

}
