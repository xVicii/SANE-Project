package com.example.tracynguyen.network;

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

        uiManager.updateLL3PDisplay(receivedLL3P); // ?????

        if(receivedLL3P.getDestLL3PAddressHexString().toUpperCase().equals(NetworkConstants.MY_LL3P_ADDRESS)){
            uiManager.updateLL3PDisplay(receivedLL3P);
        }
        else{
            try{
                ARPUpdate(receivedLL3P.getDestLL3PAddress(),
                        arpTable.getLL2PAddressFor(receivedLL3P.getDestLL3PAddress()));
                this.sendPayloadToLL3PDestination(
                        forwardingTable.getNextHopAddress(receivedLL3P.getDestLL3PAddress()),
                        receivedLL3P.getPayload());

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendLL3PPacket(LL3P packet){
        try{
            ll2Daemon.sendLL2PFrame(Utilities.stringToByte(packet.toString()),
                    arpTable.getLL2PAddressFor(packet.getDestLL3PAddress()),
                    Integer.valueOf(NetworkConstants.LL3P_PACKET, 16));
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void sendPayloadToLL3PDestination(Integer LL3PAddress, byte[] payload){
        LL3P packet = new LL3P(NetworkConstants.MY_LL3P_ADDRESS,
                Integer.toHexString(LL3PAddress),
                NetworkConstants.LL3P_PACKET, // what to set type?
                "1",
                Utilities.byteToString(payload));

        sendLL3PPacket(packet);
    }

    public void ARPUpdate(Integer LL3PAddress, Integer LL2PAddress){
        arpTable.addOrUpdate(LL2PAddress, LL3PAddress);
    }
}
