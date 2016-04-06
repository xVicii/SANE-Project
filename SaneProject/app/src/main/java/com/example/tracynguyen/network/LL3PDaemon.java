package com.example.tracynguyen.network;

import android.util.Log;

import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.UIManager;
import com.example.tracynguyen.support.Utilities;

/**
 * Created by tracy.nguyen on 4/4/2016.
 */
public class LL3PDaemon {
    private ARPTable arpTable;
    private UIManager uiManager;
    private LL3P ll3P;
    private LL2Daemon ll2Daemon;
    private ForwardingTable forwardingTable;

    public LL3PDaemon(){

    }

    public void getObjectReferences(Factory factory){
        arpTable = factory.getArpTable();
        uiManager = factory.getUiManager();
        ll2Daemon = factory.getLL2_Daemon();
        forwardingTable = factory.getForwardingTable();
    }

    public void receiveLL3PPacket(byte[] packet){
        LL3P receivedLL3P = new LL3P(packet);

        uiManager.updateLL3PDisplay(receivedLL3P);

        if(receivedLL3P.getDestLL3PAddressHexString().toUpperCase().equals(NetworkConstants.MY_LL3P_ADDRESS)){
            uiManager.updateLL3PDisplay(receivedLL3P);
            uiManager.receiveMessage(receivedLL3P.getDestLL3PAddress(), receivedLL3P.getPayloadHexString());
        }
        else if (receivedLL3P.getTimeToLive().equals(255)){
            try{
                // Touch the ARP entry
                ARPUpdate(receivedLL3P.getDestLL3PAddress(),
                        arpTable.getLL2PAddressFor(receivedLL3P.getDestLL3PAddress()));

                // Decrement TTL and forward the packet
                LL3P nextHopLL3P = new LL3P(
                        receivedLL3P.getSrcLL3PAddressHexString(),
                        receivedLL3P.getDestLL3PAddressHexString(),
                        receivedLL3P.getTypeFieldHexString(),
                        receivedLL3P.getIDHexString(),
                        Utilities.padHexString(Integer.toHexString(receivedLL3P.getTimeToLive() - 1), NetworkConstants.LL3P_TTL_LENGTH),
                        receivedLL3P.getPayloadHexString()
                );

                sendLL3PPacket(nextHopLL3P);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            // Decrement TTL and forward the packet
            LL3P nextHopLL3P = new LL3P(
                    receivedLL3P.getSrcLL3PAddressHexString(),
                    receivedLL3P.getDestLL3PAddressHexString(),
                    receivedLL3P.getTypeFieldHexString(),
                    receivedLL3P.getIDHexString(),
                    Utilities.padHexString(Integer.toHexString(receivedLL3P.getTimeToLive() - 1), NetworkConstants.LL3P_TTL_LENGTH),
                    receivedLL3P.getPayloadHexString()
            );

            sendLL3PPacket(nextHopLL3P);
        }
    }

    public void sendLL3PPacket(LL3P packet){
        // Decrement TTL here?
        try{
            ll2Daemon.sendLL2PFrame(Utilities.stringToByte(packet.toString()),
                    arpTable.getLL2PAddressFor(forwardingTable.getNextHopAddress(packet.getDestLL3PAddress())),
                    Integer.valueOf(NetworkConstants.LL3P_PACKET, 16)); // 0x8001
            Log.i(NetworkConstants.TAG, packet.toString());
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void sendPayloadToLL3PDestination(Integer LL3PAddress, byte[] payload){
        LL3P packet = new LL3P(NetworkConstants.MY_LL3P_ADDRESS,
                Utilities.padHexString(Integer.toHexString(forwardingTable.getNextHopAddress(LL3PAddress)), NetworkConstants.LL3P_ADDRESS_LENGTH),
                NetworkConstants.LL3P_PACKET, // what to set type?
                "0000", // what to set ID?
                "FF",
                Utilities.byteToString(payload));

        sendLL3PPacket(packet);
    }

    public void ARPUpdate(Integer LL3PAddress, Integer LL2PAddress){
        arpTable.addOrUpdate(LL2PAddress, LL3PAddress);
    }
}
