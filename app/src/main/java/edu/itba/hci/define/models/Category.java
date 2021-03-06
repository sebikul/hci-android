package edu.itba.hci.define.models;


import java.util.Arrays;

public class Category implements CategoryInterface{
    private int id;
    private String name;
    private Attribute[] attributes;

    public Category(int id, String name, Attribute[] attributes) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }

    public Attribute[] getAttributes() {

        return attributes;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean isCategory() {
        return true;
    }


    public String getName() {
        return name;
    }

}
