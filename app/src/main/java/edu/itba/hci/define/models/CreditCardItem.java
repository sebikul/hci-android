package edu.itba.hci.define.models;


import java.io.Serializable;

public class CreditCardItem implements Serializable {

    private int id;
    private String number;

    public CreditCardItem(int id, String name) {

        this.id = id;
        this.number = name;
    }

    @Override
    public String toString() {
        return "CreditCardItem{" +
                "id=" + id +
                ", name='" + number + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}
