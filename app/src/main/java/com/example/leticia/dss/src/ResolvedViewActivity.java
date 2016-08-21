package com.example.leticia.dss.src;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.leticia.dss.Model.Options;
import com.example.leticia.dss.R;
import com.example.leticia.dss.database.DatabaseHandler;
import com.example.leticia.dss.utils.JsonParser;
import com.example.leticia.dss.utils.JsonParser2;
import com.example.leticia.dss.widget.OptionsListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ResolvedViewActivity extends AppCompatActivity {
          private  String title ,settingsURL,optionsURL,pointsURL,offerURL;
          private String name,balance,test;
          public TextView txtTitle,txtUsers,txtBalance,txtAgreement,txtMsg;
          private OptionsListView lstView;
          public StringRequest request;
          public RequestQueue requestQueue;
          ArrayList<HashMap<String, String>> optionsList;
          ArrayList<HashMap<String, String>> pointsList;
          HashMap<String, String> map1;
          List<Options>  options;
          Context c;
          private ProgressBar pbpp;


            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.resolvednegotiationview);
                this.balance = balance;

                optionsList = new ArrayList<HashMap<String, String>>();
                pointsList = new ArrayList<HashMap<String, String>>();

                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                //actionBar.setIcon(R.mipmap.ic_launcher);
                //actionBar.setIcon();
                //databaseHelper = new PersonalDatabaseHelper(this);
                Intent in = getIntent();
                Bundle b = in.getExtras();

                this.title = b.getString("title");
                this.settingsURL = b.getString("settingsURL");
                this.optionsURL = b.getString("optionsURL");
                this.pointsURL = b.getString("pointsURL");
                this.offerURL = b.getString("offerURL");

                Log.d("Settings URL","  "+settingsURL);
                Log.d("Options URL","  "+optionsURL);
                Log.d("Settings URL","  "+pointsURL);
                Log.d("offerURL URL","  "+offerURL);
                 lstView = (OptionsListView) findViewById(R.id.optionListView);
                 txtTitle = (TextView) findViewById(R.id.txtTitle);
                 txtUsers = (TextView) findViewById(R.id.txtusers);
                 txtBalance = (TextView) findViewById(R.id.txtbalance);
                 txtAgreement = (TextView) findViewById(R.id.txtagreement);

                pbpp = (ProgressBar) findViewById(R.id.pbppl);
                txtMsg = (TextView) findViewById(R.id.progressMsg2);




                // we will using AsyncTask during parsing
                new AsyncTaskParseSettingsJson().execute();
                //new AsyncTaskParseLastofferJson().execute();
                new AsyncTaskParseOptionsJson().execute();
                txtTitle.setText(title);

            }

                public class AsyncTaskParseSettingsJson extends AsyncTask<JSONArray, JSONArray, JSONArray> {

                    final String TAG = "AsyncTaskParseJson.java";


                    @Override
                    protected void onPreExecute() {

                    }

                    @Override
                    protected JSONArray doInBackground(JSONArray... arg0) {

                        // instantiate our json parser
                        JsonParser jParser = new JsonParser();

                        // get json string from url
                        JSONArray json = jParser.getJSONFromUrl(settingsURL);
                        Log.d("settings ",""+json);
                        return json;
                    }

                    @Override
                    protected void onPostExecute(JSONArray json) {
                        try {


                            // loop through all users
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);

                                // Storing each json item in variable
                                balance = c.getString("balance");
                                name = c.getString("name");
                                //this. = balance;
                                // show the values in our logcat
                                Log.e(TAG, "balance: " + balance
                                        + ", username: " + name);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DecimalFormat dF = new DecimalFormat("0.00");
                        //
                        double b = Double.parseDouble(balance);


                        txtAgreement.setText("Agreement Reached");
                        txtBalance.setText("Balance " + " :" + dF.format(b));
                        txtUsers.setText("You " + "vs" + " " + name);

                    }
                }


           public class AsyncTaskParseOptionsJson extends AsyncTask<String, String,String> {

                    final String TAG = "AsyncTaskParseJson.java";

                    JSONArray dataJsonArr = null;

                    @Override
                    protected void onPreExecute() {}

                    @Override
                    protected String doInBackground(String... arg0) {

                        try {

                            // instantiate our json parser
                            JsonParser jParser = new JsonParser();

                            // get json string from url
                            JSONArray json = jParser.getJSONFromUrl(optionsURL);
                              Log.d("Options ",""+json);
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);

                                // Storing each json item in variable
                                String id = c.getString("id");
                                String title = c.getString("title");
                                //String agreement_action = c.getString("agreement_action");
                                String negotiation_id = c.getString("negotiation_id");
                                String color = "0";
                                String mycolor = "#ffffff";

                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                                db.addOptions(id,title,null,null,color,mycolor,color,negotiation_id);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String optionsList) {

                        new AsyncTaskParseopponentofferJson().execute();
                        new AsyncTaskParsePointsJson().execute();


                    }
                }
                public class AsyncTaskParsePointsJson extends AsyncTask<String, String, String> {

                    final String TAG = "AsyncTaskParseJson.java";


                    // contacts JSONArray
                    JSONArray dataJsonArr = null;

                    @Override
                    protected void onPreExecute() {}

                    @Override
                    protected String doInBackground(String... arg0) {

                        try {

                            // instantiate our json parser
                            JsonParser jParser = new JsonParser();

                            // get json string from url
                            JSONArray json = jParser.getJSONFromUrl(pointsURL);

                            // get the array of users
                            //dataJsonArr = json.getJSONArray("Users");

                            // loop through all users
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);

                                // Storing each json item in variable
                                String option_id = c.getString("option_id").toString();
                                String myrating = c.getString("myrating").toString();
                                String points = c.getString("points").toString();
                                //String color = "0";

;                               DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                                db.insertPoints(option_id,myrating,points);




                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                    @Override
                    protected void onPostExecute(String strFromDoInBg) {
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        options = db.getAllOptions();
                        Log.d("database List",""+options);
                        lstView.setItems(options);
                        pbpp.setVisibility(View.GONE);
                        txtMsg.setVisibility(View.GONE);


                    }
                }



          /*
                public class AsyncTaskParseLastofferJson extends AsyncTask<Void, Void, String> {

                    final String TAG = "AsyncofferJson.java";

                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected String doInBackground(Void... arg0) {

                        // instantiate our json parser
                        JsonParser jParser = new JsonParser();
                        String lastoffer = offerURL +"/last";
                        // get json string from url
                        JSONArray json = jParser.getJSONFromUrl(lastoffer);
                        Log.d("lastoffer ", "" + json);
                        try {

                            // loop through all users
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);
                                String option_id = c.getString("option_id");
                                Log.e(TAG, "OPTION_ID: " + option_id);
                                String opcolor = "#f0ad4e";

                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.insertColor(option_id,option_id,opcolor);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        return null;
                    }

                    @Override
                    protected void onPostExecute(String json) {



                    }
                }*/



                public class AsyncTaskParseopponentofferJson extends AsyncTask<Void, Void, String> {

                    final String TAG = "AsyncofferJson.java";

                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected String doInBackground(Void... arg0) {

                        // instantiate our json parser
                        JsonParser jParser = new JsonParser();
                        String lastoffer = offerURL +"/last";
                        // get json string from url
                        JSONArray json = jParser.getJSONFromUrl(lastoffer);
                        Log.d("opponentofferURL ", "" + json);
                        try {

                            // loop through all users
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);
                                String option_id = c.getString("option_id");
                                Log.e(TAG, "OPTION_ID: " + option_id);
                                String oppcolor = "#f0ad4e";
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.insertOpColor(option_id,option_id,oppcolor);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        return null;
                    }

                    @Override
                    protected void onPostExecute(String json) {


                    }
                }
        }



