package com.codecool.krk;

public class Session {

    private String userName;
    private String sessionId;

    public Session(String userName, String sessionId) {

        this.userName = userName;
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public String getSessionId() {
        return sessionId;
    }
}
