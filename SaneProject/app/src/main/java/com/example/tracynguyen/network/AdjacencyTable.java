package com.example.tracynguyen.network;

import com.example.tracynguyen.support.LabException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tracy.nguyen on 2/4/2016.
 */
public class AdjacencyTable {
    Set<AdjacencyTableEntry> table;

    public AdjacencyTable(){
        table = new TreeSet<AdjacencyTableEntry>();
    }

    public void addEntry(Integer MACAddress, String newIP){
        AdjacencyTableEntry newEntry = new AdjacencyTableEntry(MACAddress, newIP);
        try {
            table.add(newEntry);
        } catch (Exception e){

        }
    }

    public void removeEntry(Integer MACtoBeRemoved){
        Iterator<AdjacencyTableEntry> tableIterator = table.iterator();
        boolean found = false;
        AdjacencyTableEntry tmp = null;

        // while there is another entry and nothing has been found yet, iterate through
        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            if (tmp.getLL2PAddress().equals((MACtoBeRemoved))){
                found = true;
                table.remove(tmp);
            }
        }
        if (!found){

        }
        // return tmp;
    }

    public InetAddress getIPAddressForMAC(Integer LL2PAddress) throws LabException{
        Iterator<AdjacencyTableEntry> tableIterator = table.iterator();
        boolean found = false;
        AdjacencyTableEntry tmp = null;
        InetAddress InetToBeFound = null;

        while (tableIterator.hasNext() && !found){
            tmp = tableIterator.next();
            if (tmp.getLL2PAddress().equals(LL2PAddress)){
                found = true;
                InetToBeFound = tmp.getINetIPAddress();  //return associated InetAddress
            }
        }
        if (!found) throw new
                LabException("IP Address not found for MAC. " + Integer.toString(LL2PAddress));
        return InetToBeFound;
    }

    public List<AdjacencyTableEntry> getTableAsList(){
        List<AdjacencyTableEntry> returnList = new ArrayList<AdjacencyTableEntry>();
        returnList.addAll(this.table);
        return returnList;
    }
}
