package com.example.tracynguyen.network;

import android.util.Log;

import com.example.tracynguyen.support.LabException;
import com.example.tracynguyen.support.NetworkConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tracy.nguyen on 2/25/2016.
 */
public class ARPTable implements Runnable{
    Set<ARPTableEntry> table;

    public ARPTable(){
        table = new TreeSet<ARPTableEntry>();
    }

    public void addEntry(Integer ll2PAddress, Integer ll3PAddress){
        ARPTableEntry newEntry = new ARPTableEntry(ll2PAddress, ll3PAddress);
        try {
            table.add(newEntry);
        } catch (Exception e){

        }
    }

    public Integer getLL2PAddressFor(Integer LL3PAddress) throws LabException{
        Iterator<ARPTableEntry> tableIterator = table.iterator();
        boolean found = false;
        ARPTableEntry tmp = null;
        Integer ll2PAddressToBeFound = null;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            if (tmp.getLL3PAddress().equals(LL3PAddress)){
                found = true;
                ll2PAddressToBeFound = tmp.getLL2PAddress();
            }
        }
        if (!found) throw
            new LabException("Associated LL2P Address not found for LL3P Address: " + Integer.toString(LL3PAddress));

        return ll2PAddressToBeFound;
    }

    public void removeLL2P(Integer LL2PAddress) throws LabException{
        Iterator<ARPTableEntry> tableIterator = table.iterator();
        boolean found = false;
        ARPTableEntry tmp = null;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            if (tmp.getLL2PAddress().equals(LL2PAddress)){
                found = true;
                table.remove(tmp);
            }
        }
        if (!found) throw
            new LabException("Could not find entry for LL2P Address: " + Integer.toString(LL2PAddress));
    }

    public void removeLL3P(Integer LL3PAddress) throws LabException{
        Iterator<ARPTableEntry> tableIterator = table.iterator();
        boolean found = false;
        ARPTableEntry tmp = null;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            if (tmp.getLL3PAddress().equals(LL3PAddress)){
                found = true;
                table.remove(tmp);
            }
        }
        if (!found) throw
                new LabException("Could not find entry for LL3P Address: " + Integer.toString(LL3PAddress));
    }

    public List<ARPTableEntry> getARPTableAsList(){
        List<ARPTableEntry> returnList = new ArrayList<ARPTableEntry>();
        returnList.addAll(this.table);
        return returnList;
    }

    public boolean LL2PIsInTable(Integer LL2PAddress){
        Iterator<ARPTableEntry> tableIterator = table.iterator();
        boolean found = false;
        ARPTableEntry tmp = null;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            if (tmp.getLL3PAddress().equals(LL2PAddress)){
                found = true;
                table.remove(tmp);
            }
        }

        return found;
    }

    public boolean LL3PIsInTable(Integer LL3PAddress){
        Iterator<ARPTableEntry> tableIterator = table.iterator();
        boolean found = false;
        ARPTableEntry tmp = null;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            if (tmp.getLL3PAddress().equals(LL3PAddress)){
                found = true;
                table.remove(tmp);
            }
        }

        return found;
    }

    public void expireAndRemove(){
        Iterator<ARPTableEntry> tableIterator = table.iterator();
        ARPTableEntry tmp = null;

        while (tableIterator.hasNext()){
            tmp = tableIterator.next();
            if (tmp.getCurrentAgeInSeconds() > 10){
                table.remove(tmp);
                Log.i(NetworkConstants.TAG, "Entry expired. Removing LL2P entry: " + tmp.getLL2PAddress().toString());
            }
        }
    }

    public void addOrUpdate(Integer LL2PAddress, Integer LL3PAddress){
        Iterator<ARPTableEntry> tableIterator = table.iterator();
        ARPTableEntry tmp = null;

        while (tableIterator.hasNext()){
            tmp = tableIterator.next();
            if (tmp.getLL2PAddress().equals(LL2PAddress) && tmp.getLL3PAddress().equals(LL3PAddress)){
                tmp.updateTime();
            }
            else{
                addEntry(LL2PAddress, LL3PAddress);
            }
        }
    }

    @Override
    public void run(){
        this.expireAndRemove();
    }
}
