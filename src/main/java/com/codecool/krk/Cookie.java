package com.codecool.krk;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.UUID;

public class Cookie implements HttpHandler {

    private HttpCookie cookie = null;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");

        if (cookieStr != null) {
            cookie = HttpCookie.parse(cookieStr).get(0);

        } else {
            UUID sessionId;
            sessionId = UUID.randomUUID();
            cookie = new HttpCookie("SessionId", String.valueOf(sessionId));
            httpExchange.getResponseHeaders().add("Set-cookie", "SessionCookie=" + cookie.getValue() + "; Max-Age=300");
        }
    }

    public String getSessionId(HttpExchange httpExchange) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        cookie = HttpCookie.parse(cookieStr).get(0);
        String sessionId = cookie.getValue();

        return sessionId;
    }

    public void cleanCookie(HttpExchange httpExchange) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        cookie = HttpCookie.parse(cookieStr).get(0);
        httpExchange.getResponseHeaders().add("Set-cookie", "SessionCookie=" + cookie.getValue() + "; Max-Age=0; Path=/");
    }
}