package com.example.tracynguyen.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tracy.nguyen on 3/3/2016.
 */
public class ForwardingTable extends RouteTable{
    public ForwardingTable(){
        super();
    }

    public void addFibEntry(RouteTableEntry newEntry){
        Iterator<RouteTableEntry> tableIterator = table.iterator();
        RouteTableEntry tmp = null;

        while (tableIterator.hasNext()){
            tmp = tableIterator.next();

            // only add the new route if it's a closer route than the existing one in the table
            if (tmp.equals(newEntry) &&
                    (newEntry.getNetDistPair().getDistance() < tmp.getNetDistPair().getDistance())){
                table.add(newEntry);
            }
        }
    }

    public void addRouteList(List<RouteTableEntry> routeList){
        for (int i = 0; i < routeList.size(); i++){
            addFibEntry(routeList.get(i));
        }
    }

    public Integer getNextHopAddress(Integer ll3PNetworkAddress){
        Iterator<RouteTableEntry> tableIterator = table.iterator();
        RouteTableEntry tmp = null;
        Integer nextHop = null;

        while (tableIterator.hasNext()){
            tmp = tableIterator.next();

            // only add the new route if it's a closer route than the existing one in the table
            if (tmp.getSourceLL3P().equals(ll3PNetworkAddress)){
                nextHop = tmp.getNextHop();
            }
        }

        return nextHop;
    }

    public List<RouteTableEntry> getFIBExcludingLL3PAddress(Integer ll3PAddress){
        List<RouteTableEntry> list = new ArrayList<RouteTableEntry>();
        Iterator<RouteTableEntry> tableIterator = table.iterator();
        RouteTableEntry tmp = null;

        while (tableIterator.hasNext()){
            tmp = tableIterator.next();

            // build a list that contains routes excluding the specified address
            if (!tmp.getSourceLL3P().equals(ll3PAddress)){
                list.add(tmp);
            }
        }

        return list;
    }
}
