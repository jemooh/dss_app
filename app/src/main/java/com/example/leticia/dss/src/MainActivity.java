package com.example.leticia.dss.src;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.example.leticia.dss.Adapters.TabsPagerAdapter;
import com.example.leticia.dss.R;
import com.example.leticia.dss.database.DatabaseHandler;
import com.example.leticia.dss.utils.JsonParser2;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

            private String[] tabs = {"Pending Negotiations", "Resolved Negotiations"};
            private ViewPager viewPager;
            private TabsPagerAdapter mAdapter;
            private ActionBar actionBar;
            private LoginActivity loginActivity;
            private  String username,userPassword;
            DatabaseHandler dbHandler;
            private static final String HOST = LoginActivity.HOST;
            private String loginurl = HOST +"/decisions/api/login";
            /**
             * ATTENTION: This was auto-generated to implement the App Indexing API.
             * See https://g.co/AppIndexing/AndroidStudio for more information.
             */
            private GoogleApiClient client;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.main_activity);

               // final Bundle extras = getIntent().getExtras();


                //loginActivity = (LoginActivity) getParent();
                //getting user details from db.
                dbHandler = new DatabaseHandler(this);
                HashMap<String,String> user = new HashMap<String,String>();
                user = dbHandler.getUserDetails();
                username = user.get("name");
                userPassword = user.get("pass");

                viewPager = (ViewPager) findViewById(R.id.pager);
                actionBar = getSupportActionBar();
                new AsyncTaskParseNegotiationsJson().execute(username,userPassword);


                actionBar.setHomeButtonEnabled(false);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setIcon(R.mipmap.ic_launcher);

                // Adding Tabs
                for (String tab_name : tabs) {
                    actionBar.addTab(actionBar.newTab().setText(tab_name)
                            .setTabListener(this));
                }

                /**
                 * on swiping the viewpager make respective tab selected
                 */
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        // on changing the page
                        // make respected tab selected
                        actionBar.setSelectedNavigationItem(position);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }
                });

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
                        Bundle extras = new Bundle();
                        extras.putString("negotiations",dataJsonArr.toString());
                        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), extras);
                        viewPager.setAdapter(mAdapter);

                    }

                }/*@Override
                        public boolean onCreateOptionsMenu(Menu menu) {
                            // Inflate the menu; this adds items to the action bar if it is present.
                            getMenuInflater().inflate(R.menu.main, menu);
                            return true;
                        }
                        @Override
                        public boolean onOptionsItemSelected(MenuItem item) {
                            // Handle action bar item clicks here. The action bar will
                            // automatically handle clicks on the Home/Up button, so long
                            // as you specify a parent activity in AndroidManifest.xml.
                            int id = item.getItemId();
                            if (id == R.id.me) {
                                return true;
                            }
                            return super.onOptionsItemSelected(item);
                        }
                        */


            public void onOptions(String response) throws JSONException {

            }


            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // on tab selected
                // show respected fragment view
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }


            // so that we know something was triggered
            public void showToast(String msg) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();


            }

            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Quit?")
                            .setMessage("Do you want to quit DSS?")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                                            db.resetTables();
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);
                                            MainActivity.this.finish();
                                        }

                                    }).setNegativeButton("No", null).show();

                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }

            }

        }
