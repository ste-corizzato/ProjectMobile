package com.example.projectmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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



        for(int i =0; i<myMapObjectsModel.size(); i++){
            if(myMapObjectsModel.get(i).getIdObject()==id){

                if(myMapObjectsModel.get(i).getType().equals("CA")){
                    type.setText("CARAMELLA");
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


}
