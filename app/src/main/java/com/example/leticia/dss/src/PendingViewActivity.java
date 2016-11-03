package com.example.leticia.dss.src;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.example.leticia.dss.utils.JsonParserpost;
import com.example.leticia.dss.widget.OptionsListView;
import com.example.leticia.dss.widget.SeekBarListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;


public class PendingViewActivity extends AppCompatActivity implements OptionsClickListener {
                private String title, settingsURL, optionsURL, pointsURL, myratingURL,offerURL,opponentofferURL,agreementRdURL;
                private String name, balance, test;
                public TextView txtTitle, txtUsers, txtBalance, txtAgreement, txtlike, txtdislike, txtMsg;
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
                ArrayList<HashMap<String, String>> myratings;
                JSONArray rating;
                private AlertDialog resultDialog;
                private ProgressBar pbpp;
                private final Handler handler = new Handler();

                Context c;
                private String point, pointArray;
                public OptionsAdapter customAdapter;
                private Timer autoUpdate;
                    /*
                       public SingleLessonActivity1() {
                            super(R.string.Details_name);
                        }*/

                @Override
                public void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.pendingnegotiationview);

                    doTheAutoRefresh();

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
                     //get Bundle Extras :
                    this.title = b.getString("title");
                    this.settingsURL = b.getString("settingsURL");
                    this.optionsURL = b.getString("optionsURL");
                    this.pointsURL = b.getString("pointsURL");
                    this.myratingURL = b.getString("myratingURL");
                    this.offerURL = b.getString("offerURL");
                    this.opponentofferURL = b.getString("opponentofferURL");
                    this.agreementRdURL = b.getString("agreementRdURL");

                    Log.d("Settings URL", "  " + settingsURL);
                    Log.d("Options URL", "  " + optionsURL);
                    Log.d("Points URL", "  " + pointsURL);
                    Log.d("Myrating URL", "  " + myratingURL);
                    Log.d("Offer URL", "  " + offerURL);
                    Log.d("opponentOffer URL", "  " + opponentofferURL);

                    //initializing button,textview,seebars,listview
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

                    DatabaseHandler db = new DatabaseHandler(this);
                    db.resetTables();

                    // we will using AsyncTask during parsing
                    new AsyncTaskParseSettingsJson().execute();
                    new AsyncTaskParseOptionsJson().execute();
                    //new AsyncTaskParsePointsJson().execute();

                    txtTitle.setText(title);
                    pbpp.setVisibility(View.VISIBLE);
                    txtMsg.setVisibility(View.VISIBLE);

                    btn = (Button) findViewById(R.id.btnsubmit);
                    btn.setVisibility(View.GONE);

                    btn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                            try {
                                rating = db.getJsonmyratings();
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
                    String opponentcolor = Item.getOpponent_color_id();

                    if (opponentcolor.equals(option_id)){
                     new  AsyncTaskParseresolvedOfferJson().execute(option_id);
                    }else
                     new  AsyncTaskParseOfferJson().execute(option_id);


                }

                private class SendMyratingTask extends AsyncTask<JSONArray, Void, Boolean> {
                    private String status;
                    private String dialogTitle = "Success", statusMessage = "";

                    private ProgressDialog progressDialog = new ProgressDialog(
                            PendingViewActivity.this);

                    protected void onPreExecute() {


                        dialogTitle = "Added to queue";
                        statusMessage = "Your selections are scheduled to be sent";
                        resultDialog = new AlertDialog.Builder(PendingViewActivity.this)
                                .create();
                        resultDialog.setTitle(dialogTitle);
                        resultDialog.setMessage(statusMessage);
                        resultDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        resultDialog.dismiss();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                });
                        //resultDialog.show();
                    }

                    @Override
                    protected Boolean doInBackground(final JSONArray... r) {
                        JSONArray rating = r[0];
                        Log.d("JSONArray_rating:", "" + rating);
                        JSONArray points;
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        // loop through all users
                        try {

                            for (int i = 0; i < rating.length(); i++) {

                                JSONObject c = rating.getJSONObject(i);


                                params.add(new BasicNameValuePair(c.getString("preference"), c.getString("myrating")));


                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        Log.d("params:", "" + params);


                        try {
                            JsonParserpost jsonParser2 = new JsonParserpost();

                            JSONObject json = jsonParser2.getJSONFromUrl(myratingURL, params);
                            status = json.get("status").toString();
                            if (status.equals("ok")) {
                                dialogTitle = "Success";
                                statusMessage = "Successfully submitted your selections";

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
                            }
                            if (resultDialog.isShowing()) {
                                resultDialog.dismiss();
                            }
                            startActivity(getIntent());
                            /*if (status.equals("ok")) {
                                resultDialog = new AlertDialog.Builder(
                                        PendingViewActivity.this).create();
                                resultDialog.setTitle(dialogTitle);
                                resultDialog.setMessage(statusMessage);
                                resultDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                        "OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                finish();
                                                startActivity(getIntent());
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
                                                startActivity(getIntent());
                                            }
                                        });
                                resultDialog.show();
                            }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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
                        Log.d("settings ", "" + json);
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
                                } else
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
                        if (!balance.equals("null")) {
                            double b = Double.parseDouble(balance);
                            txtBalance.setText("Balance " + " :" + dF.format(b));
                        }

                        txtUsers.setText("You " + "vs" + " " + name);

                    }
                }


                public class AsyncTaskParseOptionsJson extends AsyncTask<String, String, String> {

                    final String TAG = "AsyncTaskParseJson.java";

                    JSONArray dataJsonArr = null;

                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected String doInBackground(String... arg0) {

                        try {

                            // instantiate our json parser
                            JsonParser jParser = new JsonParser();

                            // get json string from url
                            JSONArray json = jParser.getJSONFromUrl(optionsURL);
                            Log.d("Options ", "" + json);
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);


                                // Storing each json item in variable
                                String id = c.getString("id");
                                String title = c.getString("title");
                                //String agreement_action = c.getString("agreement_action");
                                String negotiation_id = c.getString("negotiation_id");
                                String color = "0";
                                String mycolor = "#ffffff";

                                 //store in db
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.addOptions(id, title, null, null,color,mycolor,color, negotiation_id);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String optionsList) {
                        new AsyncTaskParseLastofferJson().execute();
                        new AsyncTaskParseopponentofferJson().execute();
                        new AsyncTaskParsePointsJson().execute();

                    }
                }

                public class AsyncTaskParsePointsJson extends AsyncTask<Void, Void, String> {

                    final String TAG = "AsyncTaskParseJson.java";


                    // contacts JSONArray
                    JSONArray dataJsonArr = null;
                    String pointsnull;

                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected String doInBackground(Void... arg0) {

                        try {

                            // instantiate our json parser
                            JsonParser jParser = new JsonParser();

                            // get json string from url
                            JSONArray json = jParser.getJSONFromUrl(pointsURL);

                            if (json.length() == 0) {

                                pointsnull = "nullArray";
                            }

                            Log.d("to check array",""+json);

                            // get the array of users
                            //dataJsonArr = json.getJSONArray("Users");

                            // loop through all users
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);

                                // Log.d("String Points:_", "" + c.getString("points"));
                                if (c.getString("points") == "null") {
                                    pointsnull = "null_points";
                                }

                                //Log.d("points_check",""+c.getString("points"));
                                // Storing each json item in variable
                                String option_id = c.getString("option_id").toString();
                                String myrating = c.getString("myrating").toString();
                                String points = c.getString("points").toString();
                                //String color = "0";

                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.insertPoints(option_id, myrating, points);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return pointsnull;
                    }

                    @Override
                    protected void onPostExecute(String strFromDoInBg) {

                        if (strFromDoInBg == null) {

                            lstView.setVisibility(View.VISIBLE);
                            //lstView.setSelector( R.drawable.listview_bg);
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            options = db.getAllOptions();
                            Log.d("database List", "" + options);
                            lstView.setItems(options);
                            pbpp.setVisibility(View.GONE);
                            txtMsg.setVisibility(View.GONE);


                        } else if (strFromDoInBg.equals("null_points")) {
                            pbpp.setVisibility(View.GONE);
                            txtMsg.setVisibility(View.GONE);
                            txtAgreement.setText("Waiting for opponent's input");

                        } else if (strFromDoInBg.equals("nullArray")) {

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
                                String mycolor = "#5BB75B";

                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.insertColor(option_id,option_id,mycolor);
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


                public class AsyncTaskParseopponentofferJson extends AsyncTask<Void, Void, String> {

                    final String TAG = "AsyncofferJson.java";
                    String agreentment_check;
                    String id;

                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected String doInBackground(Void... arg0) {

                        // instantiate our json parser
                        JsonParser jParser = new JsonParser();
                        //String lastoffer = offerURL +"/last";
                        // get json string from url
                        JSONArray json = jParser.getJSONFromUrl(opponentofferURL);
                        Log.d("opponentofferURL ", "" + json);
                        try {

                            // loop through all users
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);
                                String option_id = c.getString("option_id");
                                id = c.getString("id");

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
                      new  AsyncTaskParseagreementreachedJson().execute(id);
                    }
                }

                public class AsyncTaskParseagreementreachedJson extends AsyncTask<String, Void, String> {

                    final String TAG = "AsyncofferJson.java";
                    String agreement_check;

                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected String doInBackground(String... arg0) {

                        String id = arg0[0];

                        // instantiate our json parser
                        JsonParser jParser = new JsonParser();
                        //String lastoffer = offerURL +"/last";
                        // get json string from url
                        JSONArray json = jParser.getJSONFromUrl(agreementRdURL+id+"/last");
                        Log.d("agreementRdURL ", "" + json);
                        try {

                            // loop through all users
                            for (int i = 0; i < json.length(); i++) {

                                JSONObject c = json.getJSONObject(i);
                                agreement_check = c.getString("agreement");


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        return null;
                    }

                    @Override
                    protected void onPostExecute(String json) {
                        if (agreement_check!=null){
                        txtAgreement.setText("Agreement Reached" );
                        lstView.setEnabled(false);}

                    }
                }


                    public class AsyncTaskParseOfferJson extends AsyncTask<String, Void, Boolean> {
                            private String status;
                            private String dialogTitle = "Success", statusMessage = "";

                            private ProgressDialog progressDialog = new ProgressDialog(
                                    PendingViewActivity.this);

                            protected void onPreExecute() {
                            }

                            @Override
                            protected Boolean doInBackground(final String... r) {
                                String offer = r[0];
                                Log.d("JSONArray_rating:", "" + rating);
                                JSONArray points;
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("option_id",offer));
                                Log.d("params:", "" + params);
                                try {
                                    JsonParserpost jsonParser2 = new JsonParserpost();

                                    JSONObject json = jsonParser2.getJSONFromUrl(offerURL, params);
                                    status = json.get("status").toString();
                                    if (status.equals("ok")) {
                                        dialogTitle = "Success";
                                        statusMessage = "Successfully submitted your selection";
                                    } else {
                                        dialogTitle = "Failure";
                                        statusMessage = "An error occurred!";

                                    }
                                    Log.e("SUCCESS>>>", "Successfully send option_id");
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
                                    }

                                    startActivity(getIntent());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                public class AsyncTaskParseresolvedOfferJson extends AsyncTask<String, Void, Boolean> {
                    private String status;
                    private String dialogTitle = "Success", statusMessage = "";

                    private ProgressDialog progressDialog = new ProgressDialog(
                            PendingViewActivity.this);
                    protected void onPreExecute() {
                    }

                    @Override
                    protected Boolean doInBackground(final String... r) {
                        String offer = r[0];
                        Log.d("JSONArray_rating:", "" + rating);
                        JSONArray points;
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("option_id",offer));
                        Log.d("params:", "" + params);
                        try {
                            JsonParserpost jsonParser2 = new JsonParserpost();

                            JSONObject json = jsonParser2.getJSONFromUrl(offerURL, params);
                            status = json.get("status").toString();
                            if (status.equals("ok")) {
                                dialogTitle = "Success";
                                statusMessage = "Successfully submitted your selection";
                            } else {
                                dialogTitle = "Failure";
                                statusMessage = "An error occurred!";

                            }
                            Log.e("SUCCESS>>>", "Successfully send option_id");
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
                            }
                            startActivity(getIntent());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    }


                    @Override
                    public boolean onMenuOpened(int featureId, Menu menu)
                    {
                        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
                            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                                try{
                                    Method m = menu.getClass().getDeclaredMethod(
                                            "setOptionalIconsVisible", Boolean.TYPE);
                                    m.setAccessible(true);
                                    m.invoke(menu, true);
                                }
                                catch(NoSuchMethodException e){
                                }
                                catch(Exception e){
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        return super.onMenuOpened(featureId, menu);
                    }


                    @Override
                    public boolean onCreateOptionsMenu(Menu menu) {

                        getMenuInflater().inflate(R.menu.menu_main, menu);
                        return true;

                    }



                //update after every 60sec
                private void doTheAutoRefresh() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(getIntent());
                            doTheAutoRefresh();
                        }
                    }, 60000);
                }


                public boolean onOptionsItemSelected(MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.menu_refresh:
                           startActivity(getIntent());
                            break;

                    }
                    return super.onOptionsItemSelected(item);
                }




            }


