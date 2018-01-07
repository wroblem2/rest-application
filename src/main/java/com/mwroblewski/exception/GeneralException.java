package com.mwroblewski.exception;

public class GeneralException extends RuntimeException {

    private String comment;

    public GeneralException(String parameters){
        this.comment = parameters;
    }

    public String getComment() {
        return comment;
    }
}
