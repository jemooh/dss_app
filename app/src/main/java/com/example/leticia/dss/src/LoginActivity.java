package com.example.leticia.dss.src;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.leticia.dss.R;
import com.example.leticia.dss.database.DatabaseHandler;
import com.example.leticia.dss.utils.ServerInteractions;
import com.example.leticia.dss.utils.SharedPreferenceManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String HOST = "http://716107a2.ngrok.io";
    private EditText edtusername,edtpassword;
    private Button btnLogin;
    private TextView result,txtlogin_error;
    public RequestQueue requestQueue;
    public static final String URL =  HOST +"/decisions/api/login";
    //private static final String URL = "http://192.168.1.104/decisions/api/login";
    private StringRequest request;
    private String passwd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_launcher);

        edtusername = (EditText) findViewById(R.id.edtuname);
        edtpassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtlogin_error = (TextView) findViewById(R.id.login_error);

        requestQueue = Volley.newRequestQueue(this);

        DatabaseHandler db = new DatabaseHandler(this);
        db.resetTableLogin();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(
                        LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                //progressDialog.setProgress(1);
                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                                    try {
                                        txtlogin_error.setText("");
                                        JSONObject jsonObject = new JSONObject(response);

                                                if(jsonObject.get("status").equals("ok")){

                                                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                                                    String pass = passwd;
                                                    String username =  edtusername.getText().toString();
                                                    Log.d("username",".."+username+ " " +pass);

                                                    db.adduserdetails(username,pass);
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                                                    startActivity(intent);

                                                }else {
                                                    progressDialog.dismiss();
                                                    txtlogin_error.setText(jsonObject.get("error").toString());
                                                    //Toast.makeText(getApplicationContext(),"Login " + jsonObject.get("error"),Toast.LENGTH_SHORT).show();
                                                }

                                        }
                                    catch (Exception e) {
                                        e.printStackTrace();

                                    }
                    }
                }, new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        txtlogin_error.setText("Poor Connectivity..");
                        progressDialog.dismiss();
                       /* if (!ServerInteractions.isOnline(LoginActivity.this)) {

                        }*/

                    }
                })

                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError
                        {
                            HashMap<String,String> hashMap = new HashMap<String, String>();
                            passwd = edtpassword.getText().toString();
                            passwd = computeMD5Hash(passwd);
                            hashMap.put("login",edtusername.getText().toString());
                            hashMap.put("password",passwd);

                            return hashMap;
                        }
                };
                requestQueue.add(request);
            }


        });
    }




    public String computeMD5Hash(String password){
            try {
                // Functions Creates MD5 Hash for entered password
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(password.getBytes());
                byte messageDigest[] = digest.digest();

                StringBuffer MD5Hash = new StringBuffer();
                for (int i = 0; i < messageDigest.length; i++)
                {
                    String h = Integer.toHexString(0xFF & messageDigest[i]);
                    while (h.length() < 2)
                        h = "0" + h;
                    MD5Hash.append(h);
                }

                return MD5Hash.toString();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            return null;
        }
}
