package com.example.tracynguyen.support;

import android.app.Activity;
import android.net.Network;

import com.example.tracynguyen.network.LL1Daemon;
import com.example.tracynguyen.network.LL2P;

/**
 * Created by tracy.nguyen on 1/21/2016.
 */
public class Factory {
    Activity parentActivity;
    UIManager uiManager;
    NetworkConstants networkConstants;
    LL2P LL2PFrame;
    LL1Daemon LL1_Daemon;


    public Factory(Activity activity){
        parentActivity = activity;
        createAllObjects();
        getAllObjectReferences();
        testStuff();
    }

    private void createAllObjects(){
        uiManager = new UIManager();
        networkConstants = new NetworkConstants(parentActivity);
        LL2PFrame = new LL2P("010203", "BEEFED", "ABCD", "HelloWorld");
        LL1_Daemon = new LL1Daemon();
    }

    private void getAllObjectReferences(){
        uiManager.getObjectReferences(this);
        LL1_Daemon.getObjectReferences(this);
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

    private void testStuff() {
        uiManager.updateLL2PDisplay(LL2PFrame);
    }
}
