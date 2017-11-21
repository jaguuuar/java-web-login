package com.codecool.krk;

import com.codecool.krk.dao.SessionDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class LoginController implements HttpHandler {

    private static Cookie cookie = new Cookie();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        cookie.handle(httpExchange);
        cookie.checkIfCookieNull(httpExchange);

//        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
//        if(cookieStr == null) {
//            httpExchange.getResponseHeaders().set("Location", "/login");
//            httpExchange.sendResponseHeaders(302, -1);
//        }

        String sessionId = cookie.getSessionId(httpExchange);
        SessionDAO sessionDAO = new SessionDAO();
        Boolean isSession = sessionDAO.checkIsSession(sessionId);

        if(isSession == true) {
            httpExchange.getResponseHeaders().set("Location", "/hello");
            httpExchange.sendResponseHeaders(302,-1);

        } else {

            String method = httpExchange.getRequestMethod();

            if(method.equals("GET")){
                String response = this.getLoginTemplate();

                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

            if(method.equals("POST")){
                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                Map<String, String> inputs = parseFormData(formData);
                String username = inputs.get("username");
                String password = inputs.get("password");

                this.loginHandle(httpExchange, username, password);

            }
        }

    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private String getLoginTemplate() {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login_Page.twig");
        JtwigModel model = JtwigModel.newModel();
        String response = template.render(model);

        return response;
    }

    public void loginHandle(HttpExchange httpExchange, String userName, String password) throws IOException {

        if (password.equals(userName)) {
            String sessionId = cookie.getSessionId(httpExchange);
            Session session = new Session(userName, sessionId);
            SessionDAO.sessions.add(session);

            httpExchange.getResponseHeaders().set("Location", "/hello");
            httpExchange.sendResponseHeaders(302,-1);

        } else {
            httpExchange.getResponseHeaders().set("Location", "/login");
            httpExchange.sendResponseHeaders(302,-1);
        }
    }
}
