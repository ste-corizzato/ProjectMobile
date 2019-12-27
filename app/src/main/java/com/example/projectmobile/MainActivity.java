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

public class MainActivity extends AppCompatActivity {

    public RequestQueue mRequestQueue = null;


    public String nome;
    ImageButton button_setting;

    Button play;
    Button classifica;
    Model myModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        home fragment_home = new home();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment_home).commit();

        myModel=Model.getInstance();




    }

    @Override
    protected void onStart() {
        super.onStart();

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