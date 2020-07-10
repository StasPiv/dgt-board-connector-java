package com.stas.dgt;

import com.fazecast.jSerialComm.SerialPort;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Connector {

    private static WebSocketClient client;

    private static SerialPort dgtBoard;

    static public void main(String[] args) throws URISyntaxException, IOException {
        for (SerialPort port : SerialPort.getCommPorts()) {
            port.openPort();
            port.addDataListener(new DgtListener(new Connector()));
            port.getOutputStream().write(0x42);
        }
    }

    public static boolean isDgtBoardInitialized()
    {
        return dgtBoard != null;
    }

    public static void initializeDgtBoard(SerialPort port) throws URISyntaxException, IOException {
        dgtBoard = port;
        client = new WebSocketClient(new URI("ws://18.223.2.26:8000"), dgtBoard.getOutputStream());
        client.connect();
        System.out.println("Dgt board is initialized on " + dgtBoard.getDescriptivePortName());
//        dgtBoard.getOutputStream().flush();
    }

    public void sendWsMessage(String message) {
//        System.out.println("[ws_send] " + message);
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("direct", "stas");
        messageJson.addProperty("message", message);
        client.send(messageJson.toString());
    }
}
