package com.stas.dgt;

import com.fazecast.jSerialComm.SerialPort;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Connector {

    private static WebSocketClient client;

    static public void main(String[] args) throws IOException, URISyntaxException {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("\nAvailable Ports:\n");
        for (int i = 0; i < ports.length; ++i)
            System.out.println(
                    "   [" + i + "] " + ports[i].getSystemPortName() + ": " +
                    ports[i].getDescriptivePortName() + " - " + ports[i].getPortDescription()
            );
        SerialPort comPort;

        System.out.print("Your choice: ");

        Scanner myInput = new Scanner( System.in );
        comPort = ports[myInput.nextInt()];

        comPort.openPort();
        comPort.addDataListener(new DgtListener(new Connector()));
        client = new WebSocketClient(new URI("ws://18.223.2.26:8000"), comPort.getOutputStream());
        client.connect();
    }

    public void sendWsMessage(String message) {
//        System.out.println("[ws_send] " + message);
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("direct", "stas");
        messageJson.addProperty("message", message);
        client.send(messageJson.toString());
    }
}
