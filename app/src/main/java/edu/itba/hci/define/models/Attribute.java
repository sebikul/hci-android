package edu.itba.hci.define.models;

import java.util.Arrays;

public class Attribute {

    private int id;
    private String name;
    private String[] values;

    public Attribute(int id, String name, String[] values) {

        this.id = id;
        this.name = name;
        this.values = values;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getValues() {
        return values;
    }
}
