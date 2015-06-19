package edu.itba.hci.define.models;


public class CreditCard {

    private int id;
    private String name;

    public CreditCard(int id, String name) {

        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
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
