package com.example.tracynguyen.network;

import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.UIManager;
import com.example.tracynguyen.support.Utilities;

/**
 * Created by tracy.nguyen on 2/18/2016.
 */
public class LL2Daemon {

    UIManager uiManager;
    LL1Daemon ll1Daemon;
    LL2P LL2PFrame;
    ARPDaemon arpDaemon;

    private String LL2PAddress;
    private Integer LL2PIntegerAddress;
    private String typeField;

    public LL2Daemon(){

    }

    public void getObjectReferences(Factory factory){
        uiManager = factory.getUiManager();
        ll1Daemon = factory.getLL1_Daemon();
        arpDaemon = factory.getArpDaemon();
    }

    public void setLocalLL2PAddress(Integer LocalLL2PAddress){
        LL2PIntegerAddress = LocalLL2PAddress;
    }

    public void sendLL2PFrame(byte[] payload, Integer destinationLL2PAddress, Integer LL2PType){

        LL2P newFrame = new LL2P(
                Utilities.padHexString(Integer.toHexString(destinationLL2PAddress), NetworkConstants.LL2P_ADDRESS_LENGTH),
                NetworkConstants.MY_LL2P_ADDRESS,
                Utilities.padHexString(Integer.toHexString(LL2PType), NetworkConstants.LL2P_TYPE_LENGTH),
                Utilities.byteToString(payload));

        sendLL2PFrame(newFrame);
    }

    public void sendLL2PFrame(LL2P frame){
        ll1Daemon.sendLL2PFrame(frame);
    }

    public void sendLL2PEchoRequest(String payload, Integer LL2PAddress){
        LL2P echoRequestFrame =  new LL2P(
                Utilities.padHexString(Integer.toHexString(LL2PAddress), NetworkConstants.LL2P_ADDRESS_LENGTH),
                NetworkConstants.MY_LL2P_ADDRESS,
                NetworkConstants.LL2P_ECHO_REQUEST,
                payload);

        sendLL2PFrame(echoRequestFrame);
    }

    private void replyToEchoRequest(LL2P echoRequestFrame){
        LL2P echoReplyFrame = new LL2P(echoRequestFrame.getSrcMACAddressHexString(),
                NetworkConstants.MY_LL2P_ADDRESS,
                NetworkConstants.LL2P_ECHO_REPLY,
                echoRequestFrame.getPayloadHexString());

        uiManager.updateLL2PDisplay(echoReplyFrame);
        uiManager.raiseToast("Echo reply to received frame: " + echoRequestFrame.toString());

        sendLL2PFrame(echoReplyFrame);
    }

    public void receiveLL2PFrame(byte[] byteArray){
        LL2P receivedFrame = new LL2P(byteArray);
        receiveLL2PFrame(receivedFrame);
    }

    public void receiveLL2PFrame(LL2P receivedFrame){
        if (receivedFrame.getDestMACAddressHexString().toUpperCase().equals(NetworkConstants.MY_LL2P_ADDRESS)){
            typeField = receivedFrame.getTypeFieldHexString();
            uiManager.updateLL2PDisplay(receivedFrame);
            if (typeField.equals(NetworkConstants.LL3P_PACKET)){
                uiManager.raiseToast("Received type: 0x8001 LL3P packet");
            }
            else if (typeField.equals(NetworkConstants.ARP_UPDATE)){
                uiManager.raiseToast("Received type: 0x8002 ARP update");
            }
            else if (typeField.equals(NetworkConstants.LRP)){
                uiManager.raiseToast("Received type: 0x8003 LRP");
            }
            else if (typeField.equals(NetworkConstants.LL2P_ECHO_REQUEST)){
                uiManager.raiseToast("Received type: 0x8004 LL2P Echo Request. Sending echo reply...");
                replyToEchoRequest(receivedFrame);
            }
            else if (typeField.equals(NetworkConstants.LL2P_ECHO_REPLY)){
                uiManager.raiseToast("Received type: 0x8005 LL2P Echo Reply");
            }
            else if (typeField.equals(NetworkConstants.LL2P_ARP_UPDATE)){
                uiManager.raiseToast("Received type: 0x8006 LL2P ARP Update.");
                arpDaemon.addOrUpdate(receivedFrame.getSrcMACAddress(),
                        Integer.valueOf(receivedFrame.getPayloadHexString(), 16));
                sendARPReply(receivedFrame.getSrcMACAddress());
            }
            else if (typeField.equals(NetworkConstants.LL2P_ARP_REPLY)){
                uiManager.raiseToast("Received type: 0x8007 LL2P ARP Reply.");

                arpDaemon.addOrUpdate(receivedFrame.getSrcMACAddress(),
                        Integer.valueOf(receivedFrame.getPayloadHexString(), 16));
            }
        }

        else{
            uiManager.raiseToast("The arrived frame is not destined for this node.");
        }
    }

    public void sendARPUpdate(Integer LL2PNode){
        LL2P arpUpdateFrame = new LL2P(
                Utilities.padHexString(Integer.toHexString(LL2PNode), NetworkConstants.LL2P_ADDRESS_LENGTH),
                NetworkConstants.MY_LL2P_ADDRESS,
                NetworkConstants.LL2P_ARP_UPDATE,
                NetworkConstants.MY_LL3P_ADDRESS
        );

        ll1Daemon.sendLL2PFrame(arpUpdateFrame);
    }

    public void sendARPReply(Integer LL2PAddress){
        LL2P arpReplyFrame = new LL2P(Utilities.padHexString(Integer.toHexString(LL2PAddress), NetworkConstants.LL2P_ADDRESS_LENGTH),
                NetworkConstants.MY_LL2P_ADDRESS,
                NetworkConstants.LL2P_ARP_REPLY,
                NetworkConstants.MY_LL3P_ADDRESS
        );

        ll1Daemon.sendLL2PFrame(arpReplyFrame);
    }

}
