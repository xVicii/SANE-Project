package com.example.tracynguyen.network;

import com.example.tracynguyen.support.Factory;

/**
 * Created by tracy.nguyen on 2/25/2016.
 */
public class ARPDaemon {
    ARPTable arpTable;
    LL2Daemon ll2Daemon;

    public ARPDaemon(){
        arpTable = new ARPTable();
    }

    public void getObjectReferences(Factory factory){
        ll2Daemon = factory.getLL2_Daemon();
    }

    public void addOrUpdate(Integer LL2PAddress, Integer LL3PAddress){
        arpTable.addOrUpdate(LL2PAddress, LL3PAddress);
    }

    public ARPTable getArpTable() {
        return arpTable;
    }
}
