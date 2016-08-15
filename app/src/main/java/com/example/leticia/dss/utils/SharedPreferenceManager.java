package com.example.leticia.dss.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jkirwa on 7/26/16.
 */

public class SharedPreferenceManager  {
    private  static final String USER = "USERID";
    private  static final String PREFS = "prefs";
    private static final String TAG = "SharedPreferenceManager";


        public static SharedPreferences getSharedPreference(Context context){
            return context.getSharedPreferences(PREFS, Context.MODE_MULTI_PROCESS);
        }

        public static void saveUserId(Context context, String userId) {
            SharedPreferences.Editor editor = getSharedPreference(context).edit();
            editor.putString(USER, userId);
            editor.apply();
        }

        public static String getUserId(Context context){
            return getSharedPreference(context).getString(USER, "");
        }
}