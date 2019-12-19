package com.example.projectmobile;

import org.json.JSONException;
import org.json.JSONObject;

class Player {
    private String username;
    private String img;
    private String xp;
    private String lp;

    public Player(String username, String img, String xp, String lp) {

        this.username = username;
        this.img = img;
        this.xp = xp;
        this.lp=lp;
    }


    public Player(JSONObject playerJSON) {

        try {
            this.username = playerJSON.getString("username");
            this.img = playerJSON.getString("img");
            this.xp= playerJSON.getString("xp");
            this.lp= playerJSON.getString("lp");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getImg(){
        return img;
    }
    public String getXp(){
        return xp;
    }

    public String getLp() {
        return lp;
    }
}
