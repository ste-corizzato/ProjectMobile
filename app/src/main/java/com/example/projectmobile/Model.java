package com.example.projectmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class Model {

        private RequestQueue mRequestQueue;
        private static Model instance = null;
        private Model() {



        }

        public void initModel(Context c){
            mRequestQueue= Volley.newRequestQueue(c);

        }

        public void addRequesteQueue(JsonObjectRequest Request){
            mRequestQueue.add(Request);
        }


    public void populate(JSONObject serverResponse) {
        Log.d("Leaderboards", "populate model");
        try {


            JSONArray playersJSON = serverResponse.getJSONArray("ranking");
            if(PlayersList.size()==0){
            for (int i = 0; i < playersJSON.length(); i++) {
                JSONObject playerJSON = playersJSON.getJSONObject(i);
                Player player = new Player(playerJSON);
                PlayersList.add(player);
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void MapObject(JSONObject serverResponse) {
        Log.d("Map", "populate map");

        try {

            JSONArray MapObjectJSON = serverResponse.getJSONArray("mapobjects");
//            ObjectList= [];
            ObjectList.clear();
            Boolean downloadImage = mapObjectImageList.size()==0;


                for (int i = 0; i < MapObjectJSON.length(); i++) {
                    JSONObject objectJSON = MapObjectJSON.getJSONObject(i);
                    MapObject MapObject = new MapObject(objectJSON);
                    ObjectList.add(MapObject);
                    //add image
                    if(downloadImage)
                        mapObjectImageList.add(new MapObjectImage(objectJSON.getString("id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public Bitmap getMapObjImgById(String id){
            for(int i=0; i<mapObjectImageList.size(); i++){
                if (mapObjectImageList.get(i).getId().equals(id))
                    return mapObjectImageList.get(i).getImageBitmap();
            }
            return null;
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


        public static synchronized Model getInstance() {
            if (instance == null) {
                instance = new Model();
            }
            return instance;
        }


        private String sessionID;
        private String username;
        private String imgUser;
        private String ImgObject;
        private double LatUser;
        private double LonUser;
        private ArrayList<Player> PlayersList = new ArrayList<>();
        private ArrayList<MapObject> ObjectList = new ArrayList<>();
        private ArrayList<MapObjectImage> mapObjectImageList = new ArrayList<>();






    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUser(){ return imgUser; }

    public void setImgUser(String imgUser){ this.imgUser=imgUser; }

    public Double getLatUser(){ return LatUser; }

    public void setLatUser(Double LatUser){ this.LatUser=LatUser; }

    public Double getLonUser(){ return LonUser; }

    public void setLonUser(Double LonUser){ this.LonUser=LonUser; }


    public ArrayList<Player> getPlayerList() {
        return PlayersList;
    }

    public ArrayList<MapObject> getMapObjectList(){return ObjectList;}

    public Player get(int index) {
        return PlayersList.get(index);
    }


    public String getImgObject(){ return ImgObject; }



    public int getSize() {
        return PlayersList.size();
    }

}


