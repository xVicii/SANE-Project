package com.example.tracynguyen.network;

import android.app.Activity;

import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.UIManager;

import java.util.List;

/**
 * Created by tracy.nguyen on 3/18/2016.
 */
public class LRPDaemon {
    private ARPTable arpTable;
    private RouteTable routeTable;
    private ForwardingTable forwardingTable;
    private UIManager uiManager;
    private Activity activity;
    private LL2Daemon ll2Daemon;

    public LRPDaemon(){
        routeTable = new RouteTable();
        forwardingTable = new ForwardingTable();

        //routeTable.addEntry(3841,12,1,3841);
    }

    public void getObjectReferences(Factory factory){
        arpTable = factory.getArpTable();
        //routeTable = factory.getRouteTable();
        //forwardingTable = factory.getForwardingTable();
        uiManager = factory.getUiManager();
        activity = factory.getParentActivity();
        ll2Daemon = factory.getLL2_Daemon();
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
                    netDistPair.getDistance(),
                    receivedLRP.getSourceAddress());
        }

        forwardingTable.addRouteList(routeTable.getRouteList());

        // Update tables on UI
        uiManager.resetRoutingTableListAdapter();
        uiManager.resetForwardingTableListAdapter();
    }
}
