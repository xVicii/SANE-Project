package com.example.tracynguyen.network;

import com.example.tracynguyen.support.NetworkConstants;
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
        fillInLL2PFrame(newByte);
    }

    public void createFields(){
        srcMACAddress = 0;
        destMACAddress = 0;
        typeField = 0;
        payload = new byte[0];
        CRC16 = new CRC();
        calculateCRC();
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
        typeField = Integer.valueOf(inputValue, 16);
    }

    public void setCRCField(String inputValue){
        CRC16.setCRC(Integer.valueOf(inputValue,16));
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

    public void setPayload(byte[] newByte){
        payload = newByte;
    }

    /* THESE METHODS RETURNS A HEX STRING
    REPRESENTING THE VALUE OF THE FIELD*/

    public String getSrcMACAddressHexString(){
        return Utilities.padHexString(Integer.toHexString(srcMACAddress),
                NetworkConstants.LL2P_ADDRESS_LENGTH);
    }

    public String getDestMACAddressHexString(){
        return Utilities.padHexString(Integer.toHexString(destMACAddress),
                NetworkConstants.LL2P_ADDRESS_LENGTH);
    }

    public String getTypeFieldHexString(){
        return Utilities.padHexString(Integer.toHexString(typeField),
                NetworkConstants.LL2P_TYPE_LENGTH);
    }

    public String getPayloadHexString(){
        return Utilities.byteToString(payload);
    }

    public String getCRCHexString(){
        return Utilities.padHexString(CRC16.getCRCHexString(), NetworkConstants.CRC_LENGTH);
    }

    public String toString(){
        return getDestMACAddressHexString() + getSrcMACAddressHexString() + getTypeFieldHexString()
                + getPayloadHexString() + getCRCHexString();
    }


    /* THESE METHODS RETURNS THE VALUE AS A PRIMITIVE INT TYPE*/
    /* Should I return Integer values for simplicity? */

    public Integer getSrcMACAddress(){
        return srcMACAddress;
    }

    public Integer getDestMACAddress(){
        return destMACAddress;
    }

    public Integer getTypeField(){
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
        setDestMACAddressField(frameChars.substring(0, 6));
        setSrcMACAddressField(frameChars.substring(6, 12));
        setTypeField(frameChars.substring(12, 16));
        setPayload(Utilities.stringToByte(frameChars.substring(16, frame.length - 4)));
        CRC16 = new CRC(); // had to put this in to reset CRC to 0 for calculation
        calculateCRC();
    }
}
