package com.stas.dgt;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

class DgtListener implements SerialPortDataListener
{
    Connector connector;

    String mode = "";

    String stringForSending = "";

    public DgtListener(Connector connector) {
        this.connector = connector;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < serialPortEvent.getReceivedData().length; ++i) {
            int oneByte = serialPortEvent.getReceivedData()[i] & 0xff;

            switch (oneByte) {
                case 0x86 -> mode = "board";
                case 0x8e -> mode = "move";
            }

            list.add(oneByte);
        }

        StringBuilder receivedStringFromSerial = new StringBuilder();
        for (int value : list) {
            System.out.print(Integer.toHexString(value) + " ");
            receivedStringFromSerial.append((char) value);
        }
        stringForSending += receivedStringFromSerial.toString();

        if (mode.equals("move") && stringForSending.length() % 5 == 0 || mode.equals("board") && stringForSending.length() == 67) {
            System.out.println();

            if (!Connector.isDgtBoardInitialized() ) {
                try {
                    Connector.initializeDgtBoard(serialPortEvent.getSerialPort());
                    stringForSending = "";
                    return;
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Mode: " + mode + ". Send string to ws");
            connector.sendWsMessage(stringForSending);
            stringForSending = "";
        }
    }
}
