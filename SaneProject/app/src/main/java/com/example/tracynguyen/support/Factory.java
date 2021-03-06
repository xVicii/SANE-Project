package com.example.tracynguyen.support;

import android.app.Activity;
import android.net.Network;

import com.example.tracynguyen.network.ARPDaemon;
import com.example.tracynguyen.network.ARPTable;
import com.example.tracynguyen.network.ForwardingTable;
import com.example.tracynguyen.network.LL1Daemon;
import com.example.tracynguyen.network.LL2Daemon;
import com.example.tracynguyen.network.LL2P;
import com.example.tracynguyen.network.LL3PDaemon;
import com.example.tracynguyen.network.LRPDaemon;
import com.example.tracynguyen.network.Messenger;
import com.example.tracynguyen.network.RouteTable;
import com.example.tracynguyen.network.Scheduler;
import com.example.tracynguyen.network.TestTables;

/**
 * Created by tracy.nguyen on 1/21/2016.
 */
public class Factory {
    private Activity parentActivity;
    private UIManager uiManager;
    private NetworkConstants networkConstants;
    private LL2P LL2PFrame;
    private LL1Daemon LL1_Daemon;
    private LL2Daemon LL2_Daemon;
    private ARPTable arpTable;
    private Scheduler scheduler;
    private ARPDaemon arpDaemon;
    private RouteTable routeTable;
    private ForwardingTable forwardingTable;
    private LRPDaemon lrpDaemon;
    private LL3PDaemon ll3PDaemon;
    private TestTables testTables;

    public Factory(Activity activity){
        parentActivity = activity;
        createAllObjects();
        getAllObjectReferences();
        uiManager.initWidgets();
        testStuff();

    }

    private void createAllObjects(){
        uiManager = new UIManager();
        networkConstants = new NetworkConstants(parentActivity);
        //LL2PFrame = new LL2P("EEFFDD", "BEEFED", "8001", "Hello World!");
        LL1_Daemon = new LL1Daemon();
        LL2_Daemon = new LL2Daemon();
        ll3PDaemon = new LL3PDaemon();
        arpDaemon = new ARPDaemon();
        arpTable = arpDaemon.getArpTable();
        scheduler = new Scheduler();
        lrpDaemon = new LRPDaemon();
        routeTable = lrpDaemon.getRouteTable();
        forwardingTable = lrpDaemon.getFIB();
        //testTables = new TestTables();
    }

    private void getAllObjectReferences(){
        uiManager.getObjectReferences(this);
        LL1_Daemon.getObjectReferences(this);
        LL2_Daemon.getObjectReferences(this);
        arpDaemon.getObjectReferences(this);
        scheduler.getObjectReferences(this);
        lrpDaemon.getObjectReferences(this);
        ll3PDaemon.getObjectReferences(this);
        //testTables.getObjectReferences(this);

    }

    public Activity getParentActivity(){
        return parentActivity;
    }

    public UIManager getUiManager(){
        return uiManager;
    }

    public LL2P getLL2PFrame(){
        return LL2PFrame;
    }

    public LL1Daemon getLL1_Daemon(){
        return LL1_Daemon;
    }

    public LL2Daemon getLL2_Daemon() { return LL2_Daemon; }

    public ARPDaemon getArpDaemon() {
        return arpDaemon;
    }

    public ARPTable getArpTable() { return arpTable; }

    public RouteTable getRouteTable() { return routeTable; }

    public ForwardingTable getForwardingTable() { return forwardingTable; }

    public LRPDaemon getLrpDaemon() {
        return lrpDaemon;
    }

    public LL3PDaemon getLl3PDaemon() { return ll3PDaemon; }

    public TestTables getTestTables() {
        return testTables;
    }

    private void testStuff() {
        //uiManager.updateLL2PDisplay(LL2PFrame);
        //testTables.loadTestRoutes();
        //testTables.removeTestRoutes();
        //testTables.checkExpiration();
    }
}
