package com.example.leticia.dss.src;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.leticia.dss.Adapters.NegotiationsAdapter;
import com.example.leticia.dss.Model.Negotiation;
import com.example.leticia.dss.R;
import com.example.leticia.dss.database.DatabaseHandler;
import com.example.leticia.dss.listener.ItemClickListener;
import com.example.leticia.dss.utils.SharedPreferenceManager;
import com.example.leticia.dss.widget.ItemsListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


                public class ResolvedFragment extends Fragment implements ItemClickListener {
                    private ListView involved, pending;
                    //578835b2.ngrok.io
                    public static final String HOST = "http://3b496a31.ngrok.io";
                    private final String URL = "http://" + HOST + "/decisions/api";
                    private final String negURL = HOST + "/decisions/api/negotiations/:negotiation_id/users/:user_id/settings";
                    ;
                    public StringRequest request;
                    private LoginActivity loginActivity;
                    private  ListView lstViewClosed;

                    ArrayList<String> listItems = new ArrayList<String>();
                    //ArrayAdapter<String> adapter;
                    ArrayList<String> lst1 = new ArrayList<String>();
                    ArrayAdapter<String> adapter1;
                    ArrayList<Negotiation> activeNegotiations, closedNegotiations;
                    public RequestQueue requestQueue;
                    ItemsListView listView;
                    NegotiationsAdapter adapter;
                    List<Negotiation> items;

                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                        View view = inflater.inflate(R.layout.closed_fragment, container, false);

                        listView = (ItemsListView) view.findViewById(R.id.todayListView);
                        // Construct the data source
                         items = new ArrayList<Negotiation>();

                        Bundle bundle = this.getArguments();



                        if (bundle != null) {
                            final String negotiations = bundle.getString("negotiations");

                            try {
                                JSONArray negotiationsArray = new JSONArray(negotiations);

                                for (int i = 0; i < negotiationsArray.length(); i++) {

                                    final JSONObject negotiation = (JSONObject) negotiationsArray.get(i);
                                    if (negotiation.get("active").equals("0")) {
                                        String opponent = negotiation.get("opponent").toString();
                                        String negotiation_users_id = negotiation.get("negotiations_users_id").toString();
                                        String negotiation_id = negotiation.get("id").toString();
                                        String title = negotiation.get("title").toString();
                                        String active = negotiation.get("active").toString();
                                        items.add(new Negotiation(title, negotiation_id, negotiation_users_id, opponent, active));
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        listView.setOnItemClickListener(this);
                        listView.setItems(items);
                        return view;
                    }

                    @Override
                    public void onItemClicked(Negotiation Item, int position) {
                        //showDetails(Item, position);
                        String title = Item.getTitle();
                        String negotiations_user_id = Item.getNegotiationsUsersId();
                        String opponent_id = Item.getOpponent();
                        String userId = SharedPreferenceManager.getUserId(getActivity());
                        String negotiation_id = Item.getNegotiation_id();
                        Log.d("negotiations_user_id",""+negotiations_user_id);
                        Log.d("negotiations_id",""+Item.getNegotiation_id());
                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        db.resetTables();
                        String settingsURL = HOST + "/decisions/api/negotiations/"+ negotiation_id +"/users/"+ negotiations_user_id + "/settings";
                        String optionsURL = HOST + "/decisions/api/negotiations/"+ negotiation_id +"/options";
                        //String optionsURL = "http://e5dadd62.ngrok.io/options.php";
                        String pointsURL =  HOST + "/decisions/api/negotiations/"+ negotiation_id +"/users/"+ negotiations_user_id + "/opponents/"+opponent_id+"/points";
                        String offerURL =  HOST + "/decisions/api/negotiations/"+ negotiation_id +"/users/"+ negotiations_user_id + "/offers";


                        Intent si = new Intent(getActivity(), ResolvedViewActivity.class);
                        Bundle b=new Bundle();

                        b.putString("title", title);
                        b.putString("settingsURL", settingsURL);
                        b.putString("optionsURL", optionsURL);
                        b.putString("pointsURL", pointsURL);
                        b.putString("offerURL", offerURL);

                        si.putExtras(b);
                        startActivity(si);
                    }


 }
