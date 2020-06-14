package com.example.projectmobile;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MapObjectImage {
    private String id;
    private String image;


    public MapObjectImage(String id){
        this.id=id;
        ImageRequest();
    }

    public void ImageRequest(){


        final String url2 = "https://ewserver.di.unimi.it/mobicomp/mostri/getimage.php";
        Log.d("MapObjectImage", "funziona richiesta immagine");

        JSONObject jsonRequest = new JSONObject();
        try {

            jsonRequest.put("session_id", Model.getInstance().getSessionID());
            jsonRequest.put("target_id", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest getImgRequest = new JsonObjectRequest(
                url2,
                jsonRequest,


                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("MapObject", "Eseguito: " + response);



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


        Model.getInstance().addRequesteQueue(getImgRequest);


    }

    public Bitmap getImageBitmap() {
        if(image==null)
            return null;
        return Model.getInstance().StringToBitMap(image);
    }

    public String getId() {
        return id;
    }
}
