package com.example.projectmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public RequestQueue mRequestQueue = null;


    public String nome;



    Model myModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        home fragment_home = new home();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment_home).commit();

        myModel=Model.getInstance();
        myModel.initModel(this);



    }

    private void getPlayerRequest() {

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        final String url = "https://ewserver.di.unimi.it/mobicomp/mostri/ranking.php";

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("session_id", Model.getInstance().getSessionID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest getRankingRequest = new JsonObjectRequest(
                url,
                jsonRequest,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Model.getInstance().populate(response);
                        Log.d("MainActivity", "Eseguito Leaderboards: " + response);

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivity", "Error Leaderboards: " + error.toString());
                    }
                });


        mRequestQueue.add(getRankingRequest);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","nome disp" +getDeviceName());


    }



    //funzione per ottenere il session_idd
    public void firstRegister(){
        mRequestQueue=Volley.newRequestQueue(this);
        final String url= " https://ewserver.di.unimi.it/mobicomp/mostri/register.php";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences settings =
                                getSharedPreferences("salvaDati", 0);
                        SharedPreferences.Editor editor =
                                settings.edit();

                        try {
                            Log.d("MainActivity","responce.getstring: "+response.getString("session_id"));
                            editor.putString("session_id", response.getString("session_id"));
                            editor.commit();
                            Log.d("MainActivity","Trovato: "+settings.getString("session_id",null));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("MainActivity", "Correct: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivity", "Error: " + error.toString());
                    }});
        Log.d("MainActivity", "Sending request");

        mRequestQueue.add(request);
    }

    //funzione per ottenere il img, nome, vita, exp  ok
    public void getProfile(){
        mRequestQueue=Volley.newRequestQueue(this);
        final String url= " https://ewserver.di.unimi.it/mobicomp/mostri/getprofile.php";

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("session_id",myModel.getSessionID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getProfileRequest = new JsonObjectRequest(
                url,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        getProfileResponse(response);
                        Log.d("MainActivity", "Eseguito: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivity", "Error: " + error.toString());
                    }});


                mRequestQueue.add(getProfileRequest);

        if(Model.getInstance().getMapObjectList().size()==0){
            chiamataServerOggettiMappa();

        }

    }
    private void chiamataServerOggettiMappa(){
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = "https://ewserver.di.unimi.it/mobicomp/mostri/getmap.php";
        Log.d("Map", "funziona");

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("session_id", Model.getInstance().getSessionID());
            Log.d("Map", "Eseguito: "+Model.getInstance().getSessionID());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest getMapRequest = new JsonObjectRequest(
                url,
                jsonRequest,


                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Model.getInstance().MapObject(response);
                        Log.d("Map", "Eseguito: " + response);

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Map", "Error: " + error.toString());
                    }
                });


        mRequestQueue.add(getMapRequest);



    }



    public void getProfileResponse(JSONObject response) {


        TextView nometv = findViewById(R.id.textName);
        TextView lp = findViewById(R.id.textLife);
        TextView exp = findViewById(R.id.textExp);
        ImageView img= findViewById(R.id.imageView);

        try {
            Model.getInstance().setImgUser(response.getString("img"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bitmap bm = null;

        try {
            //bm = StringToBitMap(response.getString("img"));

            Model.getInstance().StringToBitMap(response.getString("img"));
            if(bm==null){
            }
            Log.d("MainActivity", response.getString("img"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bm==null) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.knight_2));

        }else {
            img.setImageBitmap(bm);   //MyPhoto is image control.
        }

            try {
                nometv.setText(response.getString("username"));
                Model.getInstance().setUsername(response.getString("username"));
                nome = nometv.getText().toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                lp.setText(response.getString("lp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                exp.setText(response.getString("xp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnMod:
                fragment_profile newFragment = new fragment_profile();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

                ft1.replace(R.id.fragment_container, newFragment);
                ft1.addToBackStack(null);

                ft1.commit();
                Log.d("MyMainActivity", "impostazioni funziona");

                break;

            case R.id.buttonLeaderboards:
                Log.d("MyMainActivity", "leaderbords tablet");

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                MyAdapter adapter = new MyAdapter(this, this,Model.getInstance().getPlayerList());
                recyclerView.setAdapter(adapter);

        }

    }

    //phone: Google Android SDK built for x86
    //tablet:






    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
        SharedPreferences datiSalvati =
            getSharedPreferences("salvaDati", 0);
        myModel.setSessionID(datiSalvati.getString("session_id", null));

        if (myModel.getSessionID()==null){
            firstRegister();
        }else{
            Log.d("MainActivity","session id locale: "+myModel.getSessionID());
            //TextView idtext = findViewById(R.id.textID); non mostriamo il SessionID
            //idtext.setText(myModel.getSessionID());

            getProfile();
            getPlayerRequest();
        }

            

        }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);

        } else {
            return capitalize(manufacturer) + " " + model;

        }
    }

    String g=getDeviceName();



        private String capitalize(String s) {
            if (s == null || s.length() == 0) {
                return "";
            }
            char first = s.charAt(0);
            if (Character.isUpperCase(first)) {
                return s;
            } else {
                return Character.toUpperCase(first) + s.substring(1);
            }
        }










    }