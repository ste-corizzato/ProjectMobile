package com.example.projectmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Leaderboards extends AppCompatActivity {

    public RequestQueue mRequestQueue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        Model.getInstance().initWithFakeData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this);
        recyclerView.setAdapter(adapter);
        Log.d("Leaderboards", "Esegui");
        richiesta();
        }

    private void richiesta() {
        Log.d("Leaderboards", "dentro");
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
                        Log.d("Leaderboards", "Eseguito: "+response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Leaderboards", "Error: " + error.toString());
                    }});


        mRequestQueue.add(getProfileRequest);




    }

  
}

