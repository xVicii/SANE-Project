package com.example.tracynguyen.network;

import com.example.tracynguyen.support.Factory;

import java.util.Calendar;

/**
 * Created by tracy.nguyen on 3/3/2016.
 */
public class RouteTableEntry implements Comparable<RouteTableEntry>{
    private Integer SourceLL3P;
    private NetworkDistancePair netDistPair;
    private long lastTimeTouched;
    private Integer nextHop;


    public RouteTableEntry(Integer sourceLL3P, Integer network, Integer distance, Integer nextHop){
        SourceLL3P = sourceLL3P;
        netDistPair = new NetworkDistancePair(network, distance);
        this.nextHop = nextHop;
    }

    public void getObjectReferences(Factory factory){

    }

    public Integer getSourceLL3P() {
        return SourceLL3P;
    }

    public void setSourceLL3P(Integer sourceLL3P) {
        SourceLL3P = sourceLL3P;
    }

    public NetworkDistancePair getNetDistPair() {
        return netDistPair;
    }

    public void setNetDistPair(NetworkDistancePair netDistPair) {
        this.netDistPair = netDistPair;
    }

    public long getLastTimeTouched() {
        return lastTimeTouched;
    }

    public void setLastTimeTouched(int lastTimeTouched) {
        this.lastTimeTouched = lastTimeTouched;
    }

    public Integer getNextHop() {
        return nextHop;
    }

    public void setNextHop(Integer nextHop) {
        this.nextHop = nextHop;
    }

    // TODO
    public int compareTo(RouteTableEntry newEntry){
        return this.netDistPair.getNetworkNumber().compareTo(
                newEntry.getNetDistPair().getNetworkNumber());
    }

    public void updateLastTimeTouched(){
        lastTimeTouched = Calendar.getInstance().getTimeInMillis()/1000;
    }

    public boolean isNotExpired(){
        boolean expired = true;

        if (getCurrentAgeInSeconds() > 10){
            expired = false;
        }

        return expired;
    }

    public long getCurrentAgeInSeconds(){
        return Calendar.getInstance().getTimeInMillis()/1000 - lastTimeTouched;
    }

    // TODO
    public String toString(){
        return Integer.toHexString(getSourceLL3P())
                + "... next hop: "
                + Integer.toHexString(getNextHop());
    }

}