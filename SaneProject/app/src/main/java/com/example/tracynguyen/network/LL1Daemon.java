package com.example.tracynguyen.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.LabException;
import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.UIManager;
import com.example.tracynguyen.support.Utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

/**
 * Created by tracy.nguyen on 2/4/2016.
 */
public class LL1Daemon {

    private UIManager uiManager;
    private LL2P LL2PFrame;
    private LL2Daemon LL2P_Daemon;

    // define constant for port number
    private final static int receivePort = 49999;

    // define a socket
    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;
    private AdjacencyTable MAC_IPAddressTable;

    public LL1Daemon(){
        MAC_IPAddressTable = new AdjacencyTable(); // set up adjacency table
        openUDPPort(); // open the UDP sockets and initialize them
        //MAC_IPAddressTable.addEntry(15663069, "172.16.1.59");
        MAC_IPAddressTable.addEntry(3355443, "10.30.54.163");
        MAC_IPAddressTable.addEntry(5592405, "10.30.54.163");
        new listenForUDPPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, receiveSocket);
    }

    public void getObjectReferences(Factory factory){
        LL2PFrame = factory.getLL2PFrame();
        uiManager = factory.getUiManager();
        LL2P_Daemon = factory.getLL2_Daemon();
    }

    public void setAdjacency(Integer LL2PAddress, String IPAddress){
        MAC_IPAddressTable.addEntry(LL2PAddress, IPAddress);
    }

    public void removeAdjacency(Integer LL2PAddress){
        MAC_IPAddressTable.removeEntry(LL2PAddress);
    }

    public List getAdjacencyList(){
        return MAC_IPAddressTable.getTableAsList();
    }

    public void sendLL2PFrame(){
        sendLL2PFrame(LL2PFrame);
    }

    public void sendLL2PFrame(LL2P frame){
        String frameToSend = new String(frame.toString());
        Log.i(NetworkConstants.TAG, "Here is the frame being Sent: "+frameToSend);
        boolean foundValidAddress = true;

        InetAddress IPAddress = null;
        try{
            IPAddress = MAC_IPAddressTable.getIPAddressForMAC(frame.getDestMACAddress());
        } catch (LabException e){
            foundValidAddress = false;
        }

        if (foundValidAddress) {
            DatagramPacket sendPacket = new DatagramPacket(
                    frameToSend.getBytes(),
                    frameToSend.length(), // # of bytes
                    IPAddress, // IP Address retrieved above
                    receivePort); // Port 49999

            new sendUDPPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new PacketInformation(sendSocket, sendPacket));
        }
        else {
            uiManager.raiseToast("Attempt to send to unknown LL2P: " +
            frame.getDestMACAddressHexString(), Toast.LENGTH_LONG);
        }
    }

    public void openUDPPort(){
        sendSocket = null;
        try{
            sendSocket = new DatagramSocket();
        } catch (SocketException e){
            e.printStackTrace();
        }

        receiveSocket = null;
        try{
            receiveSocket = new DatagramSocket(receivePort);
        } catch (SocketException e ){
            e.printStackTrace();
        }
    }

    private class sendUDPPacket extends AsyncTask<PacketInformation, Void, Void>{
        @Override
        protected Void doInBackground(PacketInformation... arg0){
            PacketInformation pktInfo = arg0[0]; // get the first argument
            try {
                pktInfo.getSocket().send(pktInfo.getPacket());
                Log.i(NetworkConstants.TAG, "Finished Sending UDP Packet, Data =" +
                        pktInfo.packet.getData().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class listenForUDPPacket extends  AsyncTask<DatagramSocket, Void, byte[]>{
        @Override
        protected byte[] doInBackground(DatagramSocket... socketList){
            byte[] receiveData = new byte[1024];
            DatagramSocket serverSocket = socketList[0];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try{
                serverSocket.receive(receivePacket);
            } catch (IOException e){
                e.printStackTrace();
            }

            int bytesReceived = receivePacket.getLength();
            byte[] rxData = new String(receivePacket.getData()).substring(0, bytesReceived).getBytes();
            return rxData;
        }

        @Override
        protected void onPostExecute(byte[] rxData){
            //String temp = new String(rxData);
            LL2P_Daemon.receiveLL2PFrame(rxData);
            uiManager.raiseToast(new String(rxData));
            new listenForUDPPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, receiveSocket);
        }

    }
}
