package com.example.leticia.dss.src;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.leticia.dss.Adapters.OptionsAdapter;
import com.example.leticia.dss.Model.Myrating;
import com.example.leticia.dss.Model.Options;
import com.example.leticia.dss.Model.SeekBars;
import com.example.leticia.dss.R;
import com.example.leticia.dss.database.DatabaseHandler;
import com.example.leticia.dss.listener.OptionsClickListener;
import com.example.leticia.dss.utils.JsonParser;
import com.example.leticia.dss.utils.JsonParser2;
import com.example.leticia.dss.widget.OptionsListView;
import com.example.leticia.dss.widget.SeekBarListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


    public class PendingViewActivity extends AppCompatActivity implements OptionsClickListener {
              private  String title ,settingsURL,optionsURL,pointsURL,myratingURL;
              private String name,balance,test;
              public TextView txtTitle,txtUsers,txtBalance,txtAgreement,txtlike,txtdislike,txtMsg;
              public Button btn;
              private SeekBarListView listViewseekbar;
              private OptionsListView lstView;
              public StringRequest request;
              public RequestQueue requestQueue;
              ArrayList<HashMap<String, String>> optionsList;
              ArrayList<HashMap<String, String>> pointsList;
              HashMap<String, String> map1;
              List<Options> options;
              List<SeekBars> seekBarsList;
              List<Myrating> mypoints;
              ArrayList<HashMap<String, String>>   myratings;
              JSONArray rating;
              private AlertDialog resultDialog;
              private ProgressBar pbpp;

              Context c;
              private String point,pointArray;
              public   OptionsAdapter customAdapter;
            /*
                public SingleLessonActivity1() {
                    super(R.string.Details_name);
                }*/

                @Override
                public void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.pendingnegotiationview);

                   // submitpoints = new ArrayList<SubmitPoints>();

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
                    this.myratingURL = b.getString("myratingURL");

                    Log.d("Settings URL","  "+settingsURL);
                    Log.d("Options URL","  "+optionsURL);
                    Log.d("Settings URL","  "+pointsURL);
                    Log.d("Settings URL","  "+myratingURL);

                     listViewseekbar = (SeekBarListView) findViewById(R.id.seekbarlistview);
                     lstView = (OptionsListView) findViewById(R.id.optionListView);
                     txtTitle = (TextView) findViewById(R.id.txtTitle);
                     txtUsers = (TextView) findViewById(R.id.txtusers);
                     txtBalance = (TextView) findViewById(R.id.txtbalance);
                     txtlike = (TextView) findViewById(R.id.textViewlike);
                     txtAgreement = (TextView) findViewById(R.id.txtagreement);
                     txtdislike = (TextView) findViewById(R.id.textViewdis);
                     pbpp = (ProgressBar) findViewById(R.id.pbppl);
                     txtMsg = (TextView) findViewById(R.id.progressMsg2);



                    lstView.setOnItemClickListener(this);
                    listViewseekbar.setVisibility(View.GONE);
                    lstView.setVisibility(View.GONE);
                    txtlike.setVisibility(View.GONE);
                    txtdislike.setVisibility(View.GONE);


                    // we will using AsyncTask during parsing
                    new AsyncTaskParseSettingsJson().execute();
                    new AsyncTaskParseOptionsJson().execute();
                    //new AsyncTaskParsePointsJson().execute();



                    txtTitle.setText(title);
                    pbpp.setVisibility(View.VISIBLE);
                    txtMsg.setVisibility(View.VISIBLE);

                    btn = (Button) findViewById(R.id.btnsubmit);
                    btn.setVisibility(View.GONE);

                    btn.setOnClickListener( new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                            try {
                                rating= db.getJsonmyratings();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //String r = rating.toString();
                            new SendMyratingTask().execute(rating);

                        }
                    });

                }

        @Override
        public void onItemClicked(Options Item, int position) {

            String option_id = Item.getOption_id();
            String title = Item.getTitle();


            Log.d("title..",":"+title);


        }

        private class SendMyratingTask extends AsyncTask<JSONArray, Void, Boolean> {
            private String status;
            private String dialogTitle = "Success", statusMessage = "";

            private ProgressDialog progressDialog = new ProgressDialog(
                    PendingViewActivity.this);

            protected void onPreExecute() {


                dialogTitle = "Added to queue";
                statusMessage = "The Your points is scheduled to be sent";
                resultDialog = new AlertDialog.Builder(PendingViewActivity.this)
                          .create();
                resultDialog.setTitle(dialogTitle);
                resultDialog.setMessage(statusMessage);
                resultDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                resultDialog.dismiss();
                                finish();
                            }
                        });
                resultDialog.show();
            }

            @Override
            protected Boolean doInBackground(final JSONArray... r) {
                JSONArray rating = r[0];
                Log.d("JSONArray_rating:",""+rating);
                JSONArray points;
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                        // loop through all users
                   try {

                       for (int i = 0; i < rating.length(); i++) {

                           JSONObject c = rating.getJSONObject(i);


                           params.add(new BasicNameValuePair( c.getString("preference"), c.getString("myrating")));


                       }
                   }catch (Exception e) {
                       e.printStackTrace();

                   }

                    Log.d("params:",""+params);


                try {
                     JsonParser2 jsonParser2 = new JsonParser2();

                    JSONObject json = jsonParser2.getJSONFromUrl(myratingURL, params);
                    status = json.get("status").toString();
                    if (status.equals("ok")) {
                        dialogTitle = "Success";
                        statusMessage = "Successfully added points";

                    } else {
                        dialogTitle = "Failure";
                        statusMessage = "An error occurred!";

                    }
                    Log.e("SUCCESS>>>", "Successfully send alert POINTS");
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return null;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }if (resultDialog.isShowing()) {
                        resultDialog.dismiss();
                    }
                    if (status.equals("ok")) {
                        resultDialog = new AlertDialog.Builder(
                                PendingViewActivity.this).create();
                        resultDialog.setTitle(dialogTitle);
                        resultDialog.setMessage(statusMessage);
                        resultDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                "OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                });
                        resultDialog.show();
                    } else {
                        resultDialog = new AlertDialog.Builder(
                                PendingViewActivity.this).create();
                        resultDialog.setTitle(dialogTitle);
                        resultDialog.setMessage(statusMessage);
                        resultDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                "OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        resultDialog.dismiss();
                                        finish();
                                    }
                                });
                        resultDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
         }


        public class AsyncTaskParseSettingsJson extends AsyncTask<JSONArray, JSONArray, JSONArray> {

                        final String TAG = "AsyncTaskParseJson.java";
                       @Override
                        protected void onPreExecute() {}

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
                                    if (c.getString("balance") != "null") {
                                        balance = c.getString("balance");
                                    }else
                                    balance = "null";

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
                            if (!balance.equals("null")){
                                double b = Double.parseDouble(balance);
                                txtBalance.setText("Balance " + " :" + dF.format(b));
                             }

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

                                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                        db.addOptions(id, title, null, null,negotiation_id);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(String optionsList) {

                            new AsyncTaskParsePointsJson().execute();

                        }
                    }

                    public class AsyncTaskParsePointsJson extends AsyncTask<Void, Void, String> {

                        final String TAG = "AsyncTaskParseJson.java";


                        // contacts JSONArray
                        JSONArray dataJsonArr = null;
                        String pointsnull;

                        @Override
                        protected void onPreExecute() {}

                        @Override
                        protected String doInBackground(Void... arg0) {

                            try {

                                // instantiate our json parser
                                JsonParser jParser = new JsonParser();

                                // get json string from url
                                JSONArray json = jParser.getJSONFromUrl(pointsURL);

                                if (json.length()==0){

                                    pointsnull = "nullArray";
                                }

                                // get the array of users
                                //dataJsonArr = json.getJSONArray("Users");

                                // loop through all users
                                for (int i = 0; i < json.length(); i++) {

                                    JSONObject c = json.getJSONObject(i);

                                   // Log.d("String Points:_", "" + c.getString("points"));
                                          if(c.getString("points") == "null") {
                                              pointsnull = "null_points";
                                          }

                                        //Log.d("points_check",""+c.getString("points"));
                                        // Storing each json item in variable
                                        String option_id = c.getString("option_id").toString();
                                        String myrating = c.getString("myrating").toString();
                                        String points = c.getString("points").toString();

                                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                        db.insertPoints(option_id, myrating, points);
                                    /*}else {
                                        pointsnull = "null_points";
                                        Log.d("pointsnull_check",""+pointsnull);
                                    }


                                    */
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return pointsnull;
                        }
                        @Override
                        protected void onPostExecute(String strFromDoInBg) {

                            if(strFromDoInBg == null){
                                lstView.setVisibility(View.VISIBLE);
                                //lstView.setSelector( R.drawable.listview_bg);
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                options = db.getAllOptions();
                                Log.d("database List", "" + options);
                                lstView.setItems(options);
                                pbpp.setVisibility(View.GONE);
                                txtMsg.setVisibility(View.GONE);

                            }else if (strFromDoInBg.equals("null_points") ){
                                pbpp.setVisibility(View.GONE);
                                txtMsg.setVisibility(View.GONE);
                                txtAgreement.setText("Waiting for opponent's input");

                            }else if (strFromDoInBg.equals("nullArray")) {

                                btn.setVisibility(View.VISIBLE);
                                listViewseekbar.setVisibility(View.VISIBLE);
                                txtlike.setVisibility(View.VISIBLE);
                                txtdislike.setVisibility(View.VISIBLE);
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                seekBarsList = db.getAllSeekBarList();
                                Log.d("database SeekBarTitles", "" + seekBarsList);
                                listViewseekbar.setItems(seekBarsList);
                                pbpp.setVisibility(View.GONE);
                                txtMsg.setVisibility(View.GONE);

                            }

                        }




                    }

            }



