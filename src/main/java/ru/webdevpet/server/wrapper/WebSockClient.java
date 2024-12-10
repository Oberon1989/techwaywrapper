package ru.webdevpet.server.wrapper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class WebSockClient {

    private WebSocketClient client;
    private final String uriString;

    public WebSockClient(String uriStr) {
        this.uriString = uriStr;
    }
    public void connect() {
        try {

            URI uri = new URI(this.uriString);


             client = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Opened connection");
                }

                @Override
                public void onMessage(String message) {

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Closed connection: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };


            client.connect();

            long startTime = System.currentTimeMillis();
            while (!client.isOpen()) {
                Thread.sleep(100);
                if (System.currentTimeMillis() - startTime > 5000) { // 5 секунд
                    throw new RuntimeException("Connection timeout");
                }
            }
            System.out.println("Connection established!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if(client!=null) {
                if(client.isOpen()) {
                    client.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void send(String message) {
        try{
            client.send(message);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}
