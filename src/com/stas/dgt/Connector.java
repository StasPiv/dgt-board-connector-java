package com.stas.dgt;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Connector {

    static public void main(String[] args) throws IOException {
        System.out.println("\nUsing Library Version v" + SerialPort.getVersion());
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("\nAvailable Ports:\n");
        for (int i = 0; i < ports.length; ++i)
            System.out.println("   [" + i + "] " + ports[i].getSystemPortName() + ": " + ports[i].getDescriptivePortName() + " - " + ports[i].getPortDescription());
        SerialPort comPort;

        System.out.print("Enter port: ");

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        String portName = reader.readLine();
        comPort = SerialPort.getCommPort(portName);
        comPort.openPort();

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                byte[] newData = event.getReceivedData();

                for (int i = 0; i < newData.length; ++i) {
                    System.out.print(Integer.toHexString(newData[i]) + " ");
                }
            }
        });

        comPort.getOutputStream().write("B".getBytes());
        comPort.getOutputStream().write("D".getBytes());

    }
}
