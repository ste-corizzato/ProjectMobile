package com.example.projectmobile;

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

import java.util.ArrayList;

public class Object_detail extends AppCompatActivity {

    private ArrayList<MapObject> myMapObjectsModel = Model.getInstance().getMapObjectList();

    private String s;
    private int id;




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
        ImageView img= findViewById(R.id.imageObj);
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

                Bitmap bm = StringToBitMap(Model.getInstance().getImgObject());
                Log.d("Object_detail", ""+bm);
                img.setImageBitmap(bm);



            }

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


        Intent backHomeIntent = new Intent(this, MainActivity.class);
        startActivity(backHomeIntent);



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



}




