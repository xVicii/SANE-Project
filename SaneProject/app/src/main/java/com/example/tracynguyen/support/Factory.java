package com.example.tracynguyen.support;

import android.app.Activity;
import android.net.Network;

import com.example.tracynguyen.network.ARPDaemon;
import com.example.tracynguyen.network.ARPTable;
import com.example.tracynguyen.network.LL1Daemon;
import com.example.tracynguyen.network.LL2Daemon;
import com.example.tracynguyen.network.LL2P;
import com.example.tracynguyen.network.Scheduler;
import com.example.tracynguyen.network.TestTables;

/**
 * Created by tracy.nguyen on 1/21/2016.
 */
public class Factory {
    Activity parentActivity;
    UIManager uiManager;
    NetworkConstants networkConstants;
    LL2P LL2PFrame;
    LL1Daemon LL1_Daemon;
    LL2Daemon LL2_Daemon;
    ARPTable arpTable;
    Scheduler scheduler;
    ARPDaemon arpDaemon;
    TestTables testTables;

    public Factory(Activity activity){
        parentActivity = activity;
        createAllObjects();
        getAllObjectReferences();
        testStuff();
    }

    private void createAllObjects(){
        uiManager = new UIManager();
        networkConstants = new NetworkConstants(parentActivity);
        LL2PFrame = new LL2P("EEFFDD", "BEEFED", "8001", "HelloWorld");
        LL1_Daemon = new LL1Daemon();
        LL2_Daemon = new LL2Daemon();
        arpDaemon = new ARPDaemon(); // create new ARP table
        arpTable = arpDaemon.getArpTable();
        scheduler = new Scheduler();
        testTables = new TestTables();
    }

    private void getAllObjectReferences(){
        uiManager.getObjectReferences(this);
        LL1_Daemon.getObjectReferences(this);
        LL2_Daemon.getObjectReferences(this);
        arpDaemon.getObjectReferences(this);
        scheduler.getObjectReferences(this);
        testTables.getObjectReferences(this);
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

    public TestTables getTestTables() {
        return testTables;
    }
    private void testStuff() {
        uiManager.updateLL2PDisplay(LL2PFrame);
        testTables.loadTestRoutes();
        //testTables.removeTestRoutes();
        testTables.checkExpiration();
    }
}
