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

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
