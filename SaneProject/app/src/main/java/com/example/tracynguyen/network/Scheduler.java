package com.example.tracynguyen.network;

import android.net.Network;

import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.NetworkConstants;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tracy.nguyen on 2/25/2016.
 */
public class Scheduler {
    ARPTable arpTable;
    ARPDaemon arpDaemon;
    RouteTable routeTable;
    LRPDaemon lrpDaemon;
    ScheduledThreadPoolExecutor threadPoolManager;

    public Scheduler(){
        // Task manager that has 3 active tasks at a time
        threadPoolManager = new ScheduledThreadPoolExecutor(3);

    }

    public void getObjectReferences(Factory factory){
        arpDaemon = factory.getArpDaemon();
        arpTable = arpDaemon.getArpTable();
        routeTable = factory.getRouteTable();
        lrpDaemon = factory.getLrpDaemon();
        createThreads();
    }

    private void createThreads(){
        threadPoolManager.scheduleAtFixedRate(
                arpTable,
                NetworkConstants.ROUTER_BOOT_TIME,
                NetworkConstants.ROUTE_UPDATE_VALUE,
                TimeUnit.SECONDS);


        threadPoolManager.scheduleAtFixedRate(
                routeTable,
                NetworkConstants.ROUTER_BOOT_TIME,
                NetworkConstants.ROUTE_UPDATE_VALUE,
                TimeUnit.SECONDS
        );

        threadPoolManager.scheduleAtFixedRate(
                lrpDaemon,
                NetworkConstants.ROUTER_BOOT_TIME,
                NetworkConstants.ROUTE_UPDATE_VALUE,
                TimeUnit.SECONDS
        );

    }

}
