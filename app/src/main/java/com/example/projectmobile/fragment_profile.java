package com.example.projectmobile;

import android.icu.text.Collator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class fragment_profile extends Fragment {

    public RequestQueue mRequestQueue = null;
    String username_text= null;
    String img =null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_profile, container, false);




    }



    @Override
    public void onStart() {
        super.onStart();
        String text = getArguments().getString(MainActivity.BUNDLE_KEY_TEXT);
        TextView tv = getActivity().findViewById(R.id.text_nome);
        tv.setText(text);
    }

    public void onClick_modifica(View v) {
        Log.d("fragment_profile", "modifica");
        modifica();
    }


    public void modifica(){
            mRequestQueue= Volley.newRequestQueue(this);
            final String url= " https://ewserver.di.unimi.it/mobicomp/mostri/setprofile.php";

            TextView tv = getView().findViewById(R.id.text_nome);
            username_text = tv.getText().toString();



            JSONObject jsonRequest = new JSONObject();
            try {

                jsonRequest.put("session_id",Model.getInstance().getSessionID());
                jsonRequest.put("username", username_text);
                jsonRequest.put("img", img);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest getProfileRequest = new JsonObjectRequest(
                    url,
                    jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            setprofile(response);
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

        public void setprofile(JSONObject response){


        }







}






