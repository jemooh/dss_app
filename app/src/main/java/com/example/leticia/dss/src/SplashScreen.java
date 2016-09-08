package com.example.leticia.dss.src;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.example.leticia.dss.Adapters.TabsPagerAdapter;
import com.example.leticia.dss.R;
import com.example.leticia.dss.database.DatabaseHandler;
import com.example.leticia.dss.utils.JsonParser2;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    /** The thread to process splash screen events */
    private Thread mSplashThread;
    private  String username,userPassword;
    DatabaseHandler dbHandler;
    public static final String HOST = "http://3b496a31.ngrok.io";
    private String loginurl = HOST +"/decisions/api/login";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //final Bundle extras = getIntent().getExtras();
        // Splash screen view
        setContentView(R.layout.splashscreen);
        //getting user details from db.
        dbHandler = new DatabaseHandler(this);
        HashMap<String,String> user = new HashMap<String,String>();
        user = dbHandler.getUserDetails();
        username = user.get("name");
        userPassword = user.get("pass");

        new AsyncTaskParseNegotiationsJson().execute(username,userPassword);

        /*int SPLASH_TIME = 3000;
        Handler HANDLER = new Handler();

        // thread for displaying the SplashScreen
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();


            }
        }, SPLASH_TIME);*/
    }



    public class AsyncTaskParseNegotiationsJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... arg0) {

            String user = arg0[0];
            String pass = arg0[1];


            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("login", user));
                params.add(new BasicNameValuePair("password", pass));
                // instantiate our json parser
                JsonParser2 jParser = new JsonParser2();
                 Log.d("loginurl..",":"+loginurl);
                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(loginurl,params);

                // get the array of users
                dataJsonArr = json.getJSONArray("negotiations");
                Log.d("negotiationsJsonArr:",":"+dataJsonArr);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("negotiations",dataJsonArr.toString());
            startActivity(intent);





        }
    }
}
