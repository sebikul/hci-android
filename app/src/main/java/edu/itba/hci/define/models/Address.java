package edu.itba.hci.define.models;


import java.io.Serializable;

public class Address implements Serializable{

    private int id;
    private String name;

    public Address(int id, String name) {

        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
