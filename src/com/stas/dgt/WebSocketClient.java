package com.stas.dgt;

import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    OutputStream outputStream;

    public WebSocketClient(URI serverUri, OutputStream oS) {
        super(serverUri);
        outputStream = oS;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println ("[ws_open] Connection established");
        this.send("subscribe.direct.stas");
    }

    @Override
    public void onMessage(String message) {
        if (message.contains("Engine output")) {
            return;
        }

//        System.out.println ("[ws_message] " + message);

        // String to be scanned to find the pattern.
        String pattern = "SEND TO DGT: (.+)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(message);

        if (!m.find( )) {
            return;
        }

        String command = m.group(1);
        try {
            outputStream.write(command.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("[ws_close] Connection closed, code: " + code + " reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println ("[ws_error] " + ex.getMessage());
    }
}
