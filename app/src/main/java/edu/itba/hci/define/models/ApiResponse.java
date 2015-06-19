package edu.itba.hci.define.models;

import java.io.Serializable;

import edu.itba.hci.define.api.ApiError;


public class ApiResponse implements Serializable{

    protected transient ApiError error;

    public ApiResponse() {
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {

        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }
}
