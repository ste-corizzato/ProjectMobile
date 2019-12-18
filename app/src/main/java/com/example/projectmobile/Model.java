package com.example.projectmobile;

import java.util.ArrayList;

public class Model {
        private static Model instance = null;
        private Model() {}
        public static synchronized Model getInstance() {
            if (instance == null) {
                instance = new Model();
            }
            return instance;
        }
        private String sessionID;
        private String username;
        private ArrayList<String> player = new ArrayList<>();


        //dati provvisori
        public void initWithFakeData() {
            player.add("Andrea");
            player.add("Bruna");
            player.add("Carlo");
            player.add("sadfsra");
            player.add("esra");
            player.add("jyhtrg");
        }



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

    public String get(int index) {
        return player.get(index);
    }
    public int getSize() {
        return player.size();
    }

}
