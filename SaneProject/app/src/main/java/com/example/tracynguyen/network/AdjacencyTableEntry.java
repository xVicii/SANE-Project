package com.example.tracynguyen.network;

import com.example.tracynguyen.support.LabException;
import com.example.tracynguyen.support.Utilities;

import java.net.InetAddress;

/**
 * Created by tracy.nguyen on 2/4/2016.
 */
public class AdjacencyTableEntry implements Comparable<AdjacencyTableEntry>{
    private Integer LL2PAddress;
    private String IPAddressString;
    private InetAddress INetIPAddress;

    // Creates Adjacency entry with zeros for all addresses
    public AdjacencyTableEntry(){
        LL2PAddress = 0;
        IPAddressString = "000000";
        INetIPAddress = null;
    }

    // Converts IP Address to internal InetAddress object
    public AdjacencyTableEntry(Integer LL2P_Address, String dottedIPAddress){
        try {
            LL2PAddress = LL2P_Address;
            IPAddressString = dottedIPAddress;
            INetIPAddress = InetAddress.getByName(dottedIPAddress);
        } catch (Exception e){

        }
    }

    public String getIPAddressString() {
        return IPAddressString; // do I need to modify to InetAddress Object???
    }

    public Integer getLL2PAddress() {
        return LL2PAddress;
    }

    public InetAddress getINetIPAddress() {
        return INetIPAddress;
    }

    public void setIPAddressString(String IPAddressString) {
        this.IPAddressString = IPAddressString;
    }

    public void setLL2PAddress(Integer LL2PAddress) {
        this.LL2PAddress = LL2PAddress;
    }

    public String toString(){
        return "LL2P: 0x" + Integer.toHexString(LL2PAddress) + ".. IP: " + IPAddressString;
    }

    public String getLL2PAddressHexString(){
        return Integer.toHexString(LL2PAddress);
    }

    public int compareTo(AdjacencyTableEntry newEntry){
        int result;
        result = this.LL2PAddress.compareTo(newEntry.getLL2PAddress());

        if (result > 0){
            return result = 1;
        }
        else if (result < 0){
            return result = -1;
        }
        else
            return result = 0;
    }
}
