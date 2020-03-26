package com.example.projectmobile;

import org.json.JSONException;
import org.json.JSONObject;


public class MapObject {
    private int idObject;
    private Double lat;
    private Double lon;
    private String type;
    private String size;
    private String name;

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

}
