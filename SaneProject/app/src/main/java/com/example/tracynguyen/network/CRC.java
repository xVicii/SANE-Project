package com.example.tracynguyen.network;

/**
 * Created by tracy.nguyen on 1/28/2016.
 */
public class CRC {
    //private Integer crcValue;
    private int CRCvalue;
    private int crcPattern = 0x11021;
    private int checkValue;

    public CRC(){
        resetCRC();
    }

    public int getCRC(){
         return CRCvalue; // valid syntax?
    }

    public String getCRCHexString(){
        return Integer.toHexString(CRCvalue);
    }

    public void setCRC(int CRC){
        CRCvalue = CRC;
    }

    public void resetCRC(){
        CRCvalue = 0;
    }

    public void update(byte newByte){

        int temp = newByte << 8;
        int tempCRC = temp ^ CRCvalue;

        for (int i = 0; i < 8; i++){
            tempCRC = tempCRC << 1;
            checkValue = tempCRC & 0x00010000;

            if (checkValue > 0)
                tempCRC = tempCRC ^ crcPattern;
        }
        CRCvalue = tempCRC;
    }

    public void update(byte[] byteArray){
        for (int i = 0; i < byteArray.length; i++){
            update(byteArray[i]);
        }
    }

    public String toString(){
        return getCRCHexString();
    }
}
