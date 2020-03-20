package com.example.projectmobile;

import org.json.JSONException;
import org.json.JSONObject;


public class MapObject {
    private String idObject;
    private String lat;
    private String lon;
    private String type;
    private String size;
    private String name;

    public MapObject(String idObject, String lat, String lon, String type, String size, String name) {

        this.idObject=idObject;
        this.lat= lat;
        this.lon = lon;
        this.type=type;
        this.size = size;
        this.name=name;
    }

    public MapObject(JSONObject MapObjectJSON) {

        try {
            this.idObject= MapObjectJSON.getString("id");
            this.lat= MapObjectJSON.getString("lat");
            this.lon= MapObjectJSON.getString("lon");
            this.type= MapObjectJSON.getString("type");
            this.size= MapObjectJSON.getString("size");
            this.name= MapObjectJSON.getString("name");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIdObject() {
        return idObject;
    }

    public String getLat(){
        return lat;
    }


    public String getLon(){
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

}
