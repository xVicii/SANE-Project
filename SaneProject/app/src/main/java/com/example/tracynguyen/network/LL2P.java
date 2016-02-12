package com.example.tracynguyen.network;

import com.example.tracynguyen.support.UIManager;
import com.example.tracynguyen.support.Utilities;

/**
 * Created by tracy.nguyen on 1/28/2016.
 */
public class LL2P {
    private Integer srcMACAddress;
    private Integer destMACAddress;
    private Integer typeField;
    private byte[] payload;
    private CRC CRC16;
    private int start;
    private int end;
    UIManager uiManager;


    public LL2P(String destAddress, String srcAddress, String type, String payload){
        setSrcMACAddressField(srcAddress);
        setDestMACAddressField(destAddress);
        setTypeField(type);
        setPayload(Utilities.stringToByte(payload));
        CRC16 = new CRC();
        calculateCRC();
    }

    public LL2P(){
        createFields();
    }

    public LL2P(byte[] newByte){
        start = 0;
        end = newByte.length;
    }

    public void createFields(){
        srcMACAddress = 0;
        destMACAddress = 0;
        typeField = 0;
        payload = new byte[0];
        CRC16 = new CRC();
        calculateCRC();
        // empty strings??
    }

    /* THESE METHODS CONVERT A STRING OF HEX CHARACTERS
    TO THE APPROPRIATE OBJECT FOR THE CLASS. */

    public void setSrcMACAddressField(String inputValue){
        srcMACAddress = Integer.valueOf(inputValue, 16);
    }

    public void setDestMACAddressField(String inputValue){
        destMACAddress = Integer.valueOf(inputValue, 16);
    }

    public void setTypeField(String inputValue){
        typeField = Integer.valueOf(inputValue, 16); // if statements for LL3P, ARP, LRP?
    }

    public void setCRCField(String inputValue){
        //CRC = Integer.valueOf(inputValue, 16); // ???
    }

    /* THESE METHODS CONVERT PRIMITIVE
    INT VALUES TO INTEGER VALUES*/

    public void setSrcMACAddressField(int inputValue){
        srcMACAddress = new Integer(inputValue);
    }

    public void setDestMACAddressField(int inputValue){
        destMACAddress = new Integer(inputValue);
    }

    public void setTypeField(int inputValue){
        typeField = new Integer(inputValue);
    }

    // THIS METHOD SETS THE CONTENTS OF THE PAYLOAD.
    public void setPayload(byte[] newByte){
        payload = newByte;
    }

    /* THESE METHODS RETURNS A HEX STRING
    REPRESENTING THE VALUE OF THE FIELD*/

    // TODO return correct length

    public String getSrcMACAddressHexString(){
        return Integer.toHexString(srcMACAddress);
    }

    public String getDestMACAddressHexString(){
        return Integer.toHexString(destMACAddress);
    }

    public String getTypeFieldHexString(){
        return Integer.toHexString(typeField);
    }

    public String getPayloadHexString(){
        return Utilities.byteToString(payload);
    }

    public String getCRCHexString(){
        return CRC16.getCRCHexString();
    }

    public String toString(){
        return getDestMACAddressHexString() + getTypeFieldHexString()
                + getPayloadHexString() + getCRCHexString();
    }


    /* THESE METHODS RETURNS THE VALUE AS A PRIMITIVE INT TYPE */

    public int getSrcMACAddress(){ // does this function automatically convert Integer to int?
        return srcMACAddress;
    }

    public int getDestMACAddress(){
        return destMACAddress;
    }

    public int getTypeField(){
        return typeField;
    }

    public byte[] getPayload() { return payload; }

    public byte[] getFrameBytes(){
        String bigFrame;
        bigFrame = getDestMACAddressHexString() + getSrcMACAddressHexString()
                + getTypeFieldHexString() + getPayloadHexString() + CRC16.getCRCHexString();

        return Utilities.stringToByte(bigFrame);
    }

    private void calculateCRC(){
        String bigFrame;
        bigFrame = getDestMACAddressHexString() + getSrcMACAddressHexString()
                + getTypeFieldHexString() + getPayloadHexString();

        CRC16.update(bigFrame.getBytes());
    }

    public void fillInLL2PFrame(byte[] frame){
        String frameChars = new String(frame);
        setDestMACAddressField(frameChars.substring(start, end));
        setSrcMACAddressField(frameChars.substring(start, end));
        setTypeField(frameChars.substring(start, end));
        setPayload(Utilities.stringToByte(frameChars.substring(start, end)));
        calculateCRC();
        uiManager.updateLL2PDisplay(this);
    }
}
