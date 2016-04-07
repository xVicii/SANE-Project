package com.example.tracynguyen.network;

import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tracy.nguyen on 3/9/2016.
 */
public class LRP {
    private Integer sourceAddress;
    private Integer LRPCounter;
    private Integer sequenceNumber;
    private Integer routeCount;
    private List<NetworkDistancePair> pairList;

    public LRP(){
        sourceAddress = 0;
        LRPCounter = 0;
        sequenceNumber = 0;
        routeCount = 0;
        pairList = new ArrayList<NetworkDistancePair>();
    }

    public LRP(Integer myLL3PAddress, ForwardingTable myForwardingTable,
               Integer LL3PAddressThatWillReceiveThisLRPPacket){

        setSourceAddress(myLL3PAddress);
        setSequenceNumber(0);
        setRouteCount(myForwardingTable.getFIBExcludingLL3PAddress(
                        LL3PAddressThatWillReceiveThisLRPPacket).size());
        setRoutes(myForwardingTable, LL3PAddressThatWillReceiveThisLRPPacket);
    }

    public LRP(byte[] receivedLRPPacket){
        fillInFromBytes(receivedLRPPacket);
    }

    public void fillInFromBytes(byte[] receivedLRPPacket) {
        String lrpPacket = Utilities.byteToString(receivedLRPPacket);
        setSourceAddress(Integer.valueOf(lrpPacket.substring(0, 4), 16));
        setSequenceNumber(Integer.valueOf(lrpPacket.substring(4, 5), 16));
        setRouteCount(Integer.valueOf(lrpPacket.substring(5, 6), 16));
        pairList = new ArrayList<NetworkDistancePair>();

        for (int i = 0; i < routeCount; i++){
            // Find the network number
            Integer network = Integer.valueOf(lrpPacket.substring(
                    NetworkConstants.LRP_BASE
                            + i*NetworkConstants.LRP_NETDISTPAIR_LENGTH,
                    NetworkConstants.LRP_BASE
                            + i*NetworkConstants.LRP_NETDISTPAIR_LENGTH
                            + NetworkConstants.LRP_NETWORK_LENGTH), 16);

            // Find the distance
            Integer distance = Integer.valueOf(lrpPacket.substring(
                    NetworkConstants.LRP_BASE
                            + NetworkConstants.LRP_NETWORK_LENGTH
                            + i*NetworkConstants.LRP_NETDISTPAIR_LENGTH,
                    NetworkConstants.LRP_BASE
                            + i*NetworkConstants.LRP_NETDISTPAIR_LENGTH
                            + NetworkConstants.LRP_NETWORK_LENGTH
                            + NetworkConstants.LRP_DISTANCE_LENGTH), 16);

            // Create a new Network Distance Pair Object and add it to the list
            NetworkDistancePair tmpPair = new NetworkDistancePair(network, distance);
            pairList.add(tmpPair);
        }
    }

    public Integer getSourceAddress() {
        return sourceAddress;
    }

    public List getRoutes(){
        return pairList;
    }

    public byte[] getBytes(){
        return toString().getBytes();
    }

    public void setSourceAddress(Integer sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setRouteCount(Integer routeCount) {
        this.routeCount = routeCount;
    }

    public void setRoutes(ForwardingTable table, Integer targetLL3P){
        List<RouteTableEntry> fibExcludingLL3P = table.getFIBExcludingLL3PAddress(targetLL3P);
        List<NetworkDistancePair> networkDistancePairList = new ArrayList<NetworkDistancePair>();

        for (RouteTableEntry entry : fibExcludingLL3P){
            networkDistancePairList.add(entry.getNetDistPair());
        }

        this.pairList = networkDistancePairList;
    }

    public String toString(){

        String routeList = "";
        for (NetworkDistancePair pair : pairList){
            routeList += Utilities.padHexString(Integer.toHexString(pair.getNetworkNumber()), 1)
                    + Utilities.padHexString(Integer.toHexString(pair.getDistance()), 1);
        }

        String lrpPacket =
                Utilities.padHexString(Integer.toHexString(sourceAddress), 2)
                        + Integer.toHexString(sequenceNumber)
                        + Integer.toHexString(routeCount)
                        + routeList;

        return lrpPacket;
    }
}
