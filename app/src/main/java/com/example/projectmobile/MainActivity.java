package com.example.projectmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    public static final String BUNDLE_KEY_TEXT = "";
    public RequestQueue mRequestQueue = null;

    //public String id;
    public String nome;
    ImageButton button_setting;
    //Button button_indietro;
    Button play;
    Button classifica;
    Model myModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        home fragment_home = new home();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment_home).commit();


        button_setting = findViewById(R.id.button_imp);
        //button_indietro = findViewById(R.id.indietro);
        play = findViewById(R.id.button_map);
        classifica = findViewById(R.id.button_leaderboards);

        myModel=Model.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void onButtonTapped(View v) {
        fragment_profile newFragment2 = new fragment_profile();

        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();

        transaction2.replace(R.id.fragment_container, newFragment2);
        transaction2.addToBackStack(null);

        transaction2.commit();
        Log.d("MyMainActivity", "indietro funziona");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_imp:
                fragment_profile newFragment = new fragment_profile();
                Bundle args = new Bundle();
                args.putString(BUNDLE_KEY_TEXT,nome);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
                Log.d("MyMainActivity", "impostazioni funziona");


                break;

            case R.id.indietro:

                /*Log.d("MainActivity", "ciao");
                fragment_profile newFragment2 = new fragment_profile();

                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();

                transaction2.replace(R.id.fragment_container, newFragment2);
                transaction2.addToBackStack(null);

                transaction2.commit();
                Log.d("MyMainActivity", "indietro funziona");

                break;*/
        }

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

    //funzione per ottenere il img, nome, vita, exp
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




    }

    public void getProfileResponse(JSONObject response) {


        TextView nometv = findViewById(R.id.textName);
        TextView lp = findViewById(R.id.textLife);
        TextView exp = findViewById(R.id.textExp);



        try {
            nometv.setText(response.getString("username"));
            nome=nometv.getText().toString();

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
            TextView idtext = findViewById(R.id.textID);
            idtext.setText(myModel.getSessionID());

            getProfile();
        }

    }



}