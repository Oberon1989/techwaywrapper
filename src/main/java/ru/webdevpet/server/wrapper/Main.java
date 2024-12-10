package ru.webdevpet.server.wrapper;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new Exception("usage java -jar techway-wrapper.jar {param webscoketserver url}");
        }
        String urlStr = args[0];


        try {

            WebSockClient client = new WebSockClient(urlStr);
            client.connect();
            System.out.println("Connected to " + urlStr);
            StartMinecraftServer server = new StartMinecraftServer(client);
            server.start();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    private static boolean isValidUrl(String url) {

        System.out.println(url.length());

        if (url.trim().isEmpty()) {
            return false;
        }
        Pattern ipWithPortPattern = Pattern.compile(
                "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}(:[0-9]+)?$"
        );


        Matcher matcher = ipWithPortPattern.matcher(url);
        if (matcher.matches()) {
            return true;
        }
        if (!url.contains("://")) {
            url = "http://" + url;
        }

        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
