package com.example.tracynguyen.network;

import java.util.Calendar;

/**
 * Created by tracy.nguyen on 2/25/2016.
 */
public class ARPTableEntry implements Comparable<ARPTableEntry> {
    private Integer LL2PAddress;
    private Integer LL3PAddress;
    private long lastTimeTouched;

    public ARPTableEntry(Integer ll2PAddress, Integer ll3PAddress){
        LL2PAddress = ll2PAddress;
        LL3PAddress = ll3PAddress;
        updateTime();
    }

    public Integer getLL2PAddress() {
        return LL2PAddress;
    }

    public Integer getLL3PAddress() {
        return LL3PAddress;
    }

    public void updateTime(){
        lastTimeTouched = Calendar.getInstance().getTimeInMillis()/1000;
    }

    public long getCurrentAgeInSeconds(){
        return Calendar.getInstance().getTimeInMillis()/1000 - lastTimeTouched;
    }

    public int compareTo(ARPTableEntry newEntry){
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
