package com.example.tracynguyen.network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.UIManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tracy.nguyen on 3/3/2016.
 */
public class RouteTable implements Runnable{
    Set<RouteTableEntry> table;
    UIManager uiManager;
    Activity activity;

    public RouteTable(){
        table = new TreeSet<RouteTableEntry>();
    }

    public void getObjectReferences(Factory factory){
        uiManager = factory.getUiManager();
        activity = factory.getParentActivity();
    }

    public List<RouteTableEntry> getRouteList(){
        List<RouteTableEntry> returnList = new ArrayList<RouteTableEntry>();
        returnList.addAll(this.table);
        return returnList;
    }

    public void addEntry(Integer sourceLL3P, Integer network, Integer distance, Integer nextHop){
        RouteTableEntry newEntry = new RouteTableEntry(sourceLL3P, network, distance, nextHop);

        Iterator<RouteTableEntry> tableIterator = table.iterator();
        RouteTableEntry tmp = null;
        boolean found = false;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            if (tmp.getSourceLL3P().equals(sourceLL3P) && tmp.getNetDistPair().getNetworkNumber().equals(network)){
                table.remove(tmp);
                table.add(newEntry);
                found = true;
            }
        }

        if(!found){
            table.add(newEntry);
        }
    }

    public void removeOldRoutes(){
        boolean notDone = true;

        while(notDone) {
            Iterator<RouteTableEntry> tableIterator = table.iterator();
            RouteTableEntry tmp = null;
            boolean found = false;

            while (tableIterator.hasNext() && !found) {
                tmp = tableIterator.next();
                if (tmp.getCurrentAgeInSeconds() > NetworkConstants.ROUTE_UPDATE_VALUE * 3) {
                    Log.i(NetworkConstants.TAG, "Removing Route " + tmp.toString());
                    table.remove(tmp);
                    found = true;
                }
            }
            notDone = found;
        }
    }

    public void removeEntry(Integer network, Integer sourceRouter){
        Iterator<RouteTableEntry> tableIterator = table.iterator();
        boolean found = false;
        RouteTableEntry tmp = null;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            // ???
            if (tmp.getNetDistPair().getNetworkNumber().equals(network) &&
                    tmp.getSourceLL3P().equals(sourceRouter)){
                found = true;
                table.remove(tmp);
            }
        }
    }

    @Override
    public void run() {
        try {
            this.removeOldRoutes();
            // need to delete all entries on forwarding table and re-populate
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    uiManager.resetRoutingTableListAdapter();
                    uiManager.resetForwardingTableListAdapter();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
