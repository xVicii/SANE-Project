package com.example.tracynguyen.network;

import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.Utilities;

/**
 * Created by tracy.nguyen on 4/4/2016.
 */
public class LL3P {
    private Integer srcLL3PAddress;
    private Integer destLL3PAddress;
    private Integer typeField;
    private Integer identifier;
    private Integer timeToLive;
    private byte[] payload;
    private Integer checksum;

    public LL3P(){
        this.srcLL3PAddress = 0;
        this.destLL3PAddress = 0;
        this.typeField = 0;
        this.identifier = 0;
        this.timeToLive = 0;
        this.payload = new byte[0];
        this.checksum = 0;
    }

    public LL3P(String srcAddress, String destAddress, String type, String ID, String payload){
        setSrcLL3PAddress(srcAddress);
        setDestLL3PAddress(destAddress);
        setTypeField(type);
        setIdentifier(ID);
        setPayload(Utilities.stringToByte(payload));
    }

    public LL3P(byte[] newByte){
        fillInLL3PFrame(newByte);
    }

    public void setSrcLL3PAddress(String srcLL3PAddress){
        this.srcLL3PAddress = Integer.valueOf(srcLL3PAddress, 16);
    }

    public void setDestLL3PAddress(String destLL3PAddress){
        this.destLL3PAddress = Integer.valueOf(destLL3PAddress, 16);
    }

    public void setTypeField(String typeField){
        this.typeField = Integer.valueOf(typeField, 16);
    }

    public void setIdentifier(String identifier){
        this.identifier = Integer.valueOf(identifier, 16);
    }

    public void setTimeToLive(String timeToLive){
        this.timeToLive = Integer.valueOf(timeToLive, 16);
    }

    public void setSrcLL3PAddress(Integer srcLL3PAddress) {
        this.srcLL3PAddress = srcLL3PAddress;
    }

    public void setDestLL3PAddress(Integer destLL3PAddress) {
        this.destLL3PAddress = destLL3PAddress;
    }

    public void setTypeField(Integer typeField) {
        this.typeField = typeField;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setPayload(byte[] payload){
        this.payload = payload;
    }

    public Integer getSrcLL3PAddress() {
        return srcLL3PAddress;
    }

    public Integer getDestLL3PAddress() {
        return destLL3PAddress;
    }

    public Integer getTypeField() {
        return typeField;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public byte[] getPayload() {return payload; }

    public String getSrcLL3PAddressHexString(){
        return Utilities.padHexString(Integer.toHexString(srcLL3PAddress),
                NetworkConstants.LL3P_ADDRESS_LENGTH);
    }

    public String getDestLL3PAddressHexString(){
        return Utilities.padHexString(Integer.toHexString(destLL3PAddress),
                NetworkConstants.LL3P_ADDRESS_LENGTH);
    }

    public String getTypeFieldHexString(){
        return Utilities.padHexString(Integer.toHexString(typeField),
                NetworkConstants.LL3P_TYPE_LENGTH);
    }

    public String getIDHexString(){
        return Utilities.padHexString(Integer.toHexString(identifier),
                NetworkConstants.LL3P_IDENTIFIER_LENGTH);
    }

    public String getTTLHexString(){
        return Utilities.padHexString(Integer.toHexString(timeToLive),
                NetworkConstants.LL3P_TTL_LENGTH);
    }

    public String getPayloadHexString(){
        return Utilities.byteToString(payload);
    }

    public String getChecksumHexString(){
        return Utilities.padHexString(Integer.toHexString(checksum),
                NetworkConstants.LL3P_CHECKSUM_LENGTH);
    }

    public String toString(){
        return getSrcLL3PAddressHexString()
                + getDestLL3PAddressHexString()
                + getTypeFieldHexString()
                + getIDHexString()
                + getTTLHexString()
                + getPayloadHexString()
                + getChecksumHexString();
    }

    public void fillInLL3PFrame(byte[] frame){
        String frameChars = Utilities.byteToString(frame);
        setSrcLL3PAddress(frameChars.substring(0, 4));
        setDestLL3PAddress(frameChars.substring(4, 8));
        setTypeField(frameChars.substring(8, 12));
        setIdentifier(frameChars.substring(12, 16));
        setTimeToLive(frameChars.substring(16, 18));
        setPayload(Utilities.stringToByte(frameChars.substring(18, frameChars.length() - 4)));
    }
}
