package com.example.tracynguyen.support;

/**
 * Created by tracy.nguyen on 1/21/2016.
 */
import android.util.Base64;

import java.util.Calendar;

public class Utilities {

    /**
     * This static variable is the value for the number of seconds in the current time since some time back in the 70's.
     * It's used to calculate the number of seconds since the program began by the method which follows.
     */
    public static long baseDateSeconds = Calendar.getInstance().getTimeInMillis()/1000;

    /**
     * This method returns the number of seconds since the program began.
     * @return
     */
    public static int getTimeInSeconds(){
        return (int) (Calendar.getInstance().getTimeInMillis()/1000 - baseDateSeconds);
    }

    public static String padHexString(String hexString, int length){
        for (int i = 0; i < length; i++){
            hexString = "0" + hexString;
        }
        return hexString;
    }

    public static String prependString(String string, int stringLength){
        for (int i = 0; i < stringLength; i++){
            string = " " + string;
        }
        return string;
    }

    public static String byteToString(byte[] newByte){
        return new String(Base64.decode(newByte, Base64.DEFAULT));
    }

    public static byte[] stringToByte(String string){
        return Base64.encode(string.getBytes(), Base64.DEFAULT);
    }
}