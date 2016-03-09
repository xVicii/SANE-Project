package com.example.tracynguyen.network;

import android.app.Activity;

import com.example.tracynguyen.support.UIManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tracy.nguyen on 3/3/2016.
 */
public class RouteTable {
    Set<RouteTableEntry> table;
    UIManager uiManager;
    Activity activity;

    public RouteTable(){
        table = new TreeSet<RouteTableEntry>();
    }

    public List<RouteTableEntry> getRouteList(){
        List<RouteTableEntry> returnList = new ArrayList<RouteTableEntry>();
        returnList.addAll(this.table);
        return returnList;
    }

    public void addEntry(Integer sourceLL3P, Integer network, Integer distance, Integer nextHop){
        RouteTableEntry newEntry = new RouteTableEntry(sourceLL3P, network, distance, nextHop);
        try {
            table.add(newEntry);
        }
        catch (Exception e){

        }
    }

    public void removeOldRoutes(){
        Iterator<RouteTableEntry> tableIterator = table.iterator();
        RouteTableEntry tmp = null;

        while (tableIterator.hasNext()){
            tmp = tableIterator.next();
            if (tmp.getCurrentAgeInSeconds() > 30){
                table.remove(tmp);
            }
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
}
