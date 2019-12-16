package com.example.projectmobile;

import android.icu.text.Collator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class fragment_profile extends Fragment implements View.OnClickListener {

    public RequestQueue mRequestQueue = null;
    String username_text= null;
    String img =null;
    Button modifica;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View w= inflater.inflate(R.layout.fragment_fragment_profile, container, false);

        modifica= (Button) w.findViewById(R.id.Modifica);;
        modifica.setOnClickListener(this);


        return w;

    }


    @Override
    public void onStart() {
        super.onStart();
        String text = getArguments().getString(MainActivity.BUNDLE_KEY_TEXT);
        TextView tv = getActivity().findViewById(R.id.text_nome);
        tv.setText(text);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Modifica:

                Log.d("fragment_profile", "modifica");
                modifica();

                break;

            case R.id.indietro:




        }

    }






    public void modifica(){
            mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
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

                            Model.getInstance().setUsername(username_text);
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





}






