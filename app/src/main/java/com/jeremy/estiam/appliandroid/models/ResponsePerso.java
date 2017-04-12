package com.jeremy.estiam.appliandroid.models;

/**
 * Created by jeremy on 27/03/2017.
 */

public class ResponsePerso {
    private Boolean success;
    private String error ="";

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
