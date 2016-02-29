package com.example.tracynguyen.network;

import com.example.tracynguyen.support.Factory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tracy.nguyen on 2/25/2016.
 */
public class Scheduler {
    ARPTable arpTable;
    ARPDaemon arpDaemon;
    ScheduledThreadPoolExecutor threadPoolManager;

    public Scheduler(){
        // Task manager that has 3 active tasks at a time
        threadPoolManager = new ScheduledThreadPoolExecutor(3);

    }

    public void getObjectReferences(Factory factory){
        arpDaemon = factory.getArpDaemon();
        arpTable = arpDaemon.getArpTable();
        createThreads();
    }

    private void createThreads(){
        threadPoolManager.scheduleAtFixedRate(arpTable, 10, 30, TimeUnit.SECONDS);
    }

}
