package com.example.projectmobile;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class MapObject {
    private int idObject;
    private Double lat;
    private Double lon;
    private String type;
    private String size;
    private String name;
    private String image=null;


    public MapObject(int idObject, Double lat, Double lon, String type, String size, String name) {

        this.idObject=idObject;
        this.lat= lat;
        this.lon = lon;
        this.type=type;
        this.size = size;
        this.name=name;
    }

    public MapObject(JSONObject MapObjectJSON) {

        try {
            this.idObject= Integer.parseInt(MapObjectJSON.getString("id"));
            this.lat= Double.parseDouble(MapObjectJSON.getString("lat"));
            this.lon= Double.parseDouble(MapObjectJSON.getString("lon"));
            this.type= MapObjectJSON.getString("type");
            this.size= MapObjectJSON.getString("size");
            this.name= MapObjectJSON.getString("name");


        } catch (JSONException e) {
            e.printStackTrace();
        }

//        ImageRequest();
    }



    public int getIdObject() {
        return idObject;
    }

    public Double getLat(){
        return lat;
    }


    public Double getLon(){
        return lon;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public void ImageRequest(){


        final String url2 = "https://ewserver.di.unimi.it/mobicomp/mostri/getimage.php";
        Log.d("Map", "funziona richiesta immagine");

        JSONObject jsonRequest = new JSONObject();
        try {

            jsonRequest.put("session_id", Model.getInstance().getSessionID());
            jsonRequest.put("target_id", Integer.toString(idObject));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest getMapRequest = new JsonObjectRequest(
                url2,
                jsonRequest,


                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("MapObject", "Eseguito: " + response);
                        Toast.makeText(getApplicationContext(), "weee"+idObject, Toast.LENGTH_SHORT).show();


                        try {
                           image = response.getString("img");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MapObject", "Error: " + error.toString());
                    }
                });


        Model.getInstance().addRequesteQueue(getMapRequest);


    }

    public Bitmap getImageBitmap() {
        if(image==null)
            return null;
        return Model.getInstance().StringToBitMap(image);
    }

}
