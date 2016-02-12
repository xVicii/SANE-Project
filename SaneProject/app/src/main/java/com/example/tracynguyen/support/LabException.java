package com.example.tracynguyen.support;

/**
 * Created by tracy.nguyen on 1/21/2016.
 */
public class LabException extends Exception {

    /**
     * This is a generic exception class for the lab exceptions we may generate.
     */
    private static final long serialVersionUID = 1L;

    public LabException(String errorMessage){
        super(errorMessage);
    }
}
