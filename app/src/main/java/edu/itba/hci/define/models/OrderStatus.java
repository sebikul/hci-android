package edu.itba.hci.define.models;


import com.google.gson.annotations.SerializedName;

public enum OrderStatus {

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
