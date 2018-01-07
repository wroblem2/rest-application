package com.mwroblewski.exception;

public class Error {
    private String comment;

    public Error(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return comment;
    }
}

