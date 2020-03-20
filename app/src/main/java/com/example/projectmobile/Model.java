package com.example.projectmobile;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Model {
        private static Model instance = null;
        private Model() {


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
            JSONArray MapObjectJSON = serverResponse.getJSONArray("getmap");
            if(ObjectList.size()==0){
                for (int i = 0; i < MapObjectJSON.length(); i++) {
                    JSONObject objectJSON = MapObjectJSON.getJSONObject(i);
                    MapObject MapObject = new MapObject(objectJSON);
                    ObjectList.add(MapObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        private ArrayList<Player> PlayersList = new ArrayList<>();
        private ArrayList<MapObject> ObjectList = new ArrayList<>();






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

    public ArrayList<Player> getPlayerList() {
        return PlayersList;
    }

    public Player get(int index) {
        return PlayersList.get(index);
    }
    public int getSize() {
        return PlayersList.size();
    }

}
