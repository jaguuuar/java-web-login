package com.codecool.krk;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Map;

public class Hello implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();

        if(method.equals("GET")){
            String response = this.getHelloTemplate();

            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        if(method.equals("POST")){
            String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
            HttpCookie cookie;
            cookie = HttpCookie.parse(cookieStr).get(0);
            httpExchange.getResponseHeaders().add("Set-cookie", "first=" + cookie.getValue() + "; Max-Age=0; Path=/");

            httpExchange.getResponseHeaders().set("Location", "/login");
            httpExchange.sendResponseHeaders(302,-1);
        }
    }

    private String getHelloTemplate() {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/example.twig");
        JtwigModel model = JtwigModel.newModel();

        String response = template.render(model);

        return response;
    }
}
