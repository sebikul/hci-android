package edu.itba.hci.define.models;


public class ApiProductFilter {

    private int id;
    private String value;

    public ApiProductFilter(int id, String value) {

        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
