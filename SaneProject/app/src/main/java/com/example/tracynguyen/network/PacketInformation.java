package com.example.tracynguyen.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by tracy.nguyen on 2/7/2016.
 */
public class PacketInformation {

    public DatagramPacket packet; // changed to public so log messages can be created
    private DatagramSocket socket;

    public PacketInformation(DatagramSocket sendSocket, DatagramPacket sendPacket){
        socket = sendSocket;
        packet = sendPacket;
    }

    public DatagramPacket getPacket() {
        return packet;
    }

    public void setPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }
}
