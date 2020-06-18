package com.example.projectmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.CollationElementIterator;
import java.util.ArrayList;

public class Object_detail extends AppCompatActivity {

    private ArrayList<MapObject> myMapObjectsModel = Model.getInstance().getMapObjectList();

    RequestQueue mRequestQueue=null;

    private String s, immagine;
    private int id;

    private String died,lp,exp;

    private Location currentLocation;
    private Location ObjectLocation;
    float distanceInMeters=0;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_detail);


        Intent myIntent = getIntent();
        s=myIntent.getStringExtra("IdObject");
        id=Integer.parseInt(s);
        Log.d("Object_detail", ""+id);
    }





    @Override
    protected void onStart() {
        super.onStart();
        ModificaDati();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void ModificaDati(){
        TextView type = findViewById(R.id.Type);
        TextView name = findViewById(R.id.Name);
        TextView size = findViewById(R.id.Size);
        Button b=findViewById(R.id.fight);

        ImageView img= findViewById(R.id.imageObj);
        img.setImageBitmap(Model.getInstance().getMapObjImgById(Integer.toString(id)));


        for(int i =0; i<myMapObjectsModel.size(); i++){
            if(myMapObjectsModel.get(i).getIdObject()==id){



                if(myMapObjectsModel.get(i).getType().equals("CA")){
                    type.setText("CANDY");
                    b.setText("EAT");
                }else{
                    type.setText("MONSTER");
                }

                name.setText(myMapObjectsModel.get(i).getName());
                size.setText(myMapObjectsModel.get(i).getSize());

                Log.d("Object_detail", "Immagine: "+Model.getInstance().getImgObject());

                currentLocation=new Location ("");
                currentLocation.setLatitude(Model.getInstance().getLatUser());
                currentLocation.setLongitude(Model.getInstance().getLonUser());
                Log.d("Object_detail", "Lomngitudine corrente" +Model.getInstance().getLatUser());

                ObjectLocation = new Location("");
                ObjectLocation.setLatitude(myMapObjectsModel.get(i).getLat());
                ObjectLocation.setLongitude(myMapObjectsModel.get(i).getLon());

                distanceInMeters = currentLocation.distanceTo(ObjectLocation);

                Log.d("Object_detail", ""+distanceInMeters);

                if(distanceInMeters>50){
                    Button btn = (Button) findViewById(R.id.fight);
                    btn.setEnabled(false);
                }

            }

        }



    }









    public void onClick(View view){
        RichiestaServerFighteat();






    }

    private void RichiestaServerFighteat() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = "https://ewserver.di.unimi.it/mobicomp/mostri/fighteat.php";
        Log.d("Object_detail", "funziona");

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("session_id", Model.getInstance().getSessionID());
            jsonRequest.put("target_id", s);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest getMapRequest = new JsonObjectRequest(
                url,
                jsonRequest,


                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Object_detail", ""+response);
                        getProfileResponse(response);


                    }

                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Object_detail", "Error: " + error.toString());
                    }
                });


        mRequestQueue.add(getMapRequest);
    }



    public void getProfileResponse(JSONObject response)  {

        try {
            died=response.getString("died");
            Log.d("Object_detail", ""+died);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            lp=response.getString("lp");
            Log.d("Object_detail", ""+lp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            exp=response.getString("xp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("CONGRATULATIONS ");
        builder.setMessage("YOU HAVE REACHED: "+lp+"LP AND "+exp+ "xp");
        builder.show();

        Intent backHomeIntent = new Intent(this, MainActivity.class);
        startActivity(backHomeIntent);

    }




}




