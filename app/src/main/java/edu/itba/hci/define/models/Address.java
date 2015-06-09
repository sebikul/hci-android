package edu.itba.hci.define.models;

/**
 * Created by sebikul on 6/9/15.
 */
public class Address {

    private int id;
    private String name;

    public Address(int id, String name) {

        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
