package edu.itba.hci.define.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum OrderStatus implements Serializable{

    @SerializedName("1")
    CREATED,

    @SerializedName("2")
    CONFIRMED,

    @SerializedName("3")
    TRANSPORTED,

    @SerializedName("4")
    DELIVERED;

    @Override
    public String toString() {
        return "OrderStatus{"+this.name()+"}";
    }
}
