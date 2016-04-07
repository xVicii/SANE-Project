package com.example.tracynguyen.network;

import com.example.tracynguyen.support.Utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
        boolean found = false;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();

            // only add the new route if it's a closer route than the existing one in the table
            if (tmp.getNetDistPair().getNetworkNumber().equals(newEntry.getNetDistPair().getNetworkNumber())){
                table.remove(tmp);
                table.add(newEntry);
                found = true;
            }
        }
        if (!found){
            table.add(newEntry);
        }

    }

    public void addRouteList(List<RouteTableEntry> routeList){
        table.clear();
        Integer tmpNetwork = routeList.get(0).getNetDistPair().getNetworkNumber();
        addFibEntry(routeList.get(0));

        for (int i = 0; i < routeList.size(); i++){
            if (!routeList.get(i).getNetDistPair().getNetworkNumber().equals(tmpNetwork)){
                tmpNetwork = routeList.get(i).getNetDistPair().getNetworkNumber();
                addFibEntry(routeList.get(i));
            }
        }
    }

    public Integer getNextHopAddress(Integer ll3PNetworkAddress){

        boolean notDone = true;
        Integer nextHop = new Integer(-1);

        while (notDone){
            Set<RouteTableEntry> copy = new TreeSet<RouteTableEntry>(table);
            //copy = table;

            Iterator<RouteTableEntry> tableIterator = copy.iterator();
            RouteTableEntry tmp = null;
            boolean found = false;

            while (tableIterator.hasNext() && !found){
                tmp = tableIterator.next();

                if (tmp.getNetDistPair().getNetworkNumber().equals(Utilities.getNetworkFromInteger(ll3PNetworkAddress))){
                    nextHop = tmp.getNextHop();
                    found = true;
                }
            }
            notDone = !found;
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
