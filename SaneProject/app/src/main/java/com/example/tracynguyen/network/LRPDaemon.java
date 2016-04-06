package com.example.tracynguyen.network;

import android.app.Activity;

import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.UIManager;
import com.example.tracynguyen.support.Utilities;

import java.util.List;

/**
 * Created by tracy.nguyen on 3/18/2016.
 */
public class LRPDaemon implements Runnable{
    private ARPTable arpTable;
    private RouteTable routeTable;
    private ForwardingTable forwardingTable;
    private UIManager uiManager;
    private Activity activity;
    private LL2Daemon ll2Daemon;

    public LRPDaemon(){
        routeTable = new RouteTable();
        forwardingTable = new ForwardingTable();
    }

    public void getObjectReferences(Factory factory){
        arpTable = factory.getArpTable();
        //routeTable = factory.getRouteTable();
        //forwardingTable = factory.getForwardingTable();
        uiManager = factory.getUiManager();
        activity = factory.getParentActivity();
        ll2Daemon = factory.getLL2_Daemon();
        routeTable.getObjectReferences(factory);
        forwardingTable.getObjectReferences(factory);
    }

    public RouteTable getRouteTable() {
        return routeTable;
    }

    public List<RouteTableEntry> getRoutingTableAsList(){
        return routeTable.getRouteList();
    }

    public ForwardingTable getFIB(){
        return forwardingTable;
    }

    public List<RouteTableEntry> getForwardingTableAsList(){
        return forwardingTable.getRouteList();
    }

    public void receiveNewLRP(byte[] lrpPacket, Integer LL2PSource){
        LRP receivedLRP = new LRP(lrpPacket);

        // Touch ARP table entry
        arpTable.addOrUpdate(LL2PSource, receivedLRP.getSourceAddress());

        List<NetworkDistancePair> pairList = receivedLRP.getRoutes();

        // Add every route in the routing table
        for(NetworkDistancePair netDistPair : pairList){
            routeTable.addEntry(
                    receivedLRP.getSourceAddress(),
                    netDistPair.getNetworkNumber(),
                    netDistPair.getDistance() + 1,
                    receivedLRP.getSourceAddress());
        }

        forwardingTable.addRouteList(routeTable.getRouteList());

        // Update tables on UI
        uiManager.resetRoutingTableListAdapter();
        uiManager.resetForwardingTableListAdapter();
    }

    @Override
    public void run() {
        try {
            List<ARPTableEntry> arpTableList = arpTable.getARPTableAsList();

            routeTable.addEntry(
                    Integer.valueOf(NetworkConstants.MY_LL3P_ADDRESS, 16),
                    Utilities.getNetworkFromInteger(Integer.valueOf(NetworkConstants.MY_LL3P_ADDRESS, 16)),
                    0,
                    Integer.valueOf(NetworkConstants.MY_LL3P_ADDRESS, 16)
            );
            // For every entry in the ARP table, add a RouteTableEntry to the routing table
            for (ARPTableEntry entry : arpTableList) {
                routeTable.addEntry(
                        Integer.valueOf(NetworkConstants.MY_LL3P_ADDRESS, 16),
                        Utilities.getNetworkFromInteger(entry.getLL3PAddress()),
                        1,
                        entry.getLL3PAddress());
            }

            // Update the forwarding table
            forwardingTable.addRouteList(routeTable.getRouteList());

            // For each entry in the ARP table, build an LRP Packet
            for (ARPTableEntry entry : arpTableList) {
                LRP lrpPacket = new LRP(
                        Integer.valueOf(NetworkConstants.MY_LL3P_ADDRESS, 16),
                        forwardingTable,
                        entry.getLL3PAddress()
                );

                ll2Daemon.sendLL2PFrame(
                        lrpPacket.getBytes(),
                        entry.getLL2PAddress(),
                        Integer.valueOf(NetworkConstants.LRP, 16)
                );

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
