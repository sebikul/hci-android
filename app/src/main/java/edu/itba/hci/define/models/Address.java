package edu.itba.hci.define.models;


public class Address {

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
