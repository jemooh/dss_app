package com.example.leticia.dss.utils;


import java.util.ArrayList;
import java.util.List;





import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.leticia.dss.database.DatabaseHandler;

            public class ServerInteractions {

                    //private JSONParser jsonParser;
                /*
                    private static String loginURL = "https://www.ratibar.com/app/appRegLogin.php";
                    private static String transURL = "http://dev.ratibar.com/app/appRegLogin.php?";
                    private static String feedbackURL = "http://dev.ratibar.com/app/appRegLogin.php?";
                    private static String login_tag = "login";
                    private static String feedback_tag = "feedback";
                    private static String trans_tag = "login";
                */

                    // constructor
                    public ServerInteractions(){
                        //jsonParser = new JSONParser();
                    }

                    /**
                     * function make Login Request
                     * @param email
                     * @param password
                     *
                    public JSONObject loginUser(String email, String password){
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("tag", login_tag));
                        params.add(new BasicNameValuePair("email", email));
                        params.add(new BasicNameValuePair("password", password));
                        //Log.i("Details--:"+email+ " "+password);
                        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

                        return json;
                    }*/




                    /**
                     * Function get Login status
                     * */
                    public boolean isUserLoggedIn(Context context){
                        DatabaseHandler db = new DatabaseHandler(context);
                        int count = db.getRowCount();
                        if(count > 0){
                            // user logged in
                            return true;
                        }
                        return false;
                    }

                    /**
                     * Function to logout user
                     * Reset Database
                     * */
                    public boolean logoutUser(Context context){
                        DatabaseHandler db = new DatabaseHandler(context);
                        db.resetTables();
                        return true;
                    }
                    public static boolean isOnline(Context context) {
                        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnected())
                            return true;
                        return false;
                    }

            }