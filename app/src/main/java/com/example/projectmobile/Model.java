package com.example.projectmobile;

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
}
