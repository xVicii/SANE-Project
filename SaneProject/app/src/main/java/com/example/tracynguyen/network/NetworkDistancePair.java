package com.example.tracynguyen.network;

import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.Utilities;

/**
 * Created by tracy.nguyen on 3/3/2016.
 */
public class NetworkDistancePair {
    private Integer networkNumber;
    private Integer distance;

    public NetworkDistancePair(){
        networkNumber = 0;
        distance = 0;
    }

    public NetworkDistancePair(Integer netNumber, Integer distanceAway){
        networkNumber = netNumber;
        distance = distanceAway;
    }

    public Integer getNetworkNumber() {
        return networkNumber;
    }

    public void setNetworkNumber(Integer networkNumber) {
        this.networkNumber = networkNumber;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String toString(){
        return Utilities.padHexString(Integer.toHexString(getNetworkNumber()), 1) +
                Utilities.padHexString(Integer.toHexString(getDistance()), 1);
    }
}
