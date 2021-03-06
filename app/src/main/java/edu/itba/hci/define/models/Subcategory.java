package edu.itba.hci.define.models;

import java.util.Arrays;

public class Subcategory implements CategoryInterface{

    private int id;
    private String name;
    private Category category;
    private Attribute[] attributes;

    public Subcategory(int id, String name, Category category, Attribute[] attributes) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Subcategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }

    public int getId() {

        return id;
    }

    @Override
    public boolean isCategory() {
        return false;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }
}
