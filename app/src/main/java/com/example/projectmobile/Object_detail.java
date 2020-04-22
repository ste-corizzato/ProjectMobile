package com.example.projectmobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.CollationElementIterator;
import java.util.ArrayList;

public class Object_detail extends AppCompatActivity {

    private ArrayList<MapObject> myMapObjectsModel = Model.getInstance().getMapObjectList();

    RequestQueue mRequestQueue=null;

    private String s, immagine;
    private int id;

    private String lp,exp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_detail);


        Intent myIntent = getIntent();
        s=myIntent.getStringExtra("IdObject");
        id=Integer.parseInt(s);
        Log.d("Object_detail", ""+id);

        getImageObjectRequest();



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



        for(int i =0; i<myMapObjectsModel.size(); i++){
            if(myMapObjectsModel.get(i).getIdObject()==id){

                if(myMapObjectsModel.get(i).getType().equals("CA")){
                    type.setText("CARAMELLA");
                    b.setText("EAT");
                }else{
                    type.setText("MOSTRO");
                }

                name.setText(myMapObjectsModel.get(i).getName());
                size.setText(myMapObjectsModel.get(i).getSize());

                Log.d("Object_detail", "Immagine: "+Model.getInstance().getImgObject());





            }

        }



    }


    private void getImageObjectRequest(){
        final ImageView img= findViewById(R.id.imageObj);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url2 = "https://ewserver.di.unimi.it/mobicomp/mostri/getimage.php";
        Log.d("Map", "funziona richiesta immagine");

        JSONObject jsonRequest = new JSONObject();
        try {

            jsonRequest.put("session_id", Model.getInstance().getSessionID());
            jsonRequest.put("target_id", Integer.toString(id));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest getMapRequest = new JsonObjectRequest(
                url2,
                jsonRequest,


                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Map", "Eseguito: " + response);
                        getImgResponse(response);
                        Bitmap bm = StringToBitMap(immagine);
                        Log.d("Object_detail", ""+bm);
                        img.setImageBitmap(bm);




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

    private void getImgResponse(JSONObject response) {

        try {
            immagine = response.getString("img");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Bitmap StringToBitMap (String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }



    public void onClick(View view){
        RichiestaServerFighteat();
        getProfile();






        /*Intent backHomeIntent = new Intent(this, MainActivity.class);
        startActivity(backHomeIntent);
    */


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

    public void getProfile(){
        mRequestQueue=Volley.newRequestQueue(this);
        final String url= " https://ewserver.di.unimi.it/mobicomp/mostri/getprofile.php";

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


    public void getProfileResponse(JSONObject response)  {

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

    }




}




