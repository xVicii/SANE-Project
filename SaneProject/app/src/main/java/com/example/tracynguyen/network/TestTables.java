package com.example.tracynguyen.network;

import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.UIManager;

import java.util.List;

/**
 * Created by tracy.nguyen on 3/8/2016.
 */
public class TestTables {
    RouteTable routingTable;
    ForwardingTable forwardingTable;
    UIManager uiManager;

    public TestTables(){
        routingTable = new RouteTable();
        forwardingTable = new ForwardingTable();
    }

    public void getObjectReferences(Factory factory){
        uiManager = factory.getUiManager();
    }

    public List getRouteTableList(){
        return routingTable.getRouteList();
    }

    public List getForwardingTableList(){
        return forwardingTable.getRouteList();
    }

    public void loadTestRoutes(){
        // sourceLL3P = 0F01, Network = 0F, Distance = 1, nextHop = 0C01
        routingTable.addEntry(3841,15,1,3073);
        forwardingTable.addEntry(3841,15,1,3073);
        uiManager.resetRoutingTableListAdapter();
        uiManager.resetForwardingTableListAdapter();
    }

    public void removeTestRoutes(){
        // Network = 0F, sourceLL3P = 0F01
        routingTable.removeEntry(15, 3841);
        forwardingTable.removeEntry(15, 3841);
        uiManager.resetRoutingTableListAdapter();
        uiManager.resetForwardingTableListAdapter();
    }

    public void checkExpiration(){
        routingTable.removeOldRoutes();
        forwardingTable.removeOldRoutes();
        uiManager.resetRoutingTableListAdapter();
        uiManager.resetForwardingTableListAdapter();
    }
}
