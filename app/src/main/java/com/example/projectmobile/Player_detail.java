package com.example.projectmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Player_detail extends AppCompatActivity {
    public RequestQueue mRequestQueue = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        richiesta();

    }


    private void richiesta() {

        mRequestQueue= Volley.newRequestQueue(getApplicationContext());
        final String url= " https://ewserver.di.unimi.it/mobicomp/mostri/ranking.php";

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("session_id",Model.getInstance().getSessionID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest getProfileRequest = new JsonObjectRequest(
                url,
                jsonRequest,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Model.getInstance().populate(response);
                        getPlayerResponse(response);

                        Log.d("Player_detail", "Eseguito: "+response);

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Player_detail", "Error: " + error.toString());
                    }});


        mRequestQueue.add(getProfileRequest);




    }

    private void getPlayerResponse(JSONObject response) {
    }


}