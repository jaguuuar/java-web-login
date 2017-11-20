package com.codecool.krk.dao;

import com.codecool.krk.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionDAO {

    public static List<Session> sessions = new ArrayList<>();

    public Boolean checkIsSession(String sessionId) {

        Boolean isSession = false;
        for(Session session: sessions) {
            if(session.getSessionId().equals(sessionId)) {
                isSession = true;
            }
        }
        return isSession;
    }

    public String getUserName(String sessionId){

        String userName = "";
        for(Session session: sessions) {
            if(session.getSessionId().equals(sessionId)) {
                userName = session.getUserName();
            }
        }
        return userName;
    }
}
