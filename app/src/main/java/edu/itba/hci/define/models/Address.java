package edu.itba.hci.define.models;

public class Address extends ApiResponse {

    private int id;
    private String name;
    private String street;
    private String number;
    private String floor;
    private String gate;
    private char province;
    private String city;
    private String zipCode;
    private String phoneNumber;

    public Address(int id, String name, String street, String number, String floor, String gate, char province, String city, String zipCode, String phoneNumber) {

        this.id = id;
        this.name = name;
        this.street = street;
        this.number = number;
        this.floor = floor;
        this.gate = gate;
        this.province = province;
        this.city = city;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", floor='" + floor + '\'' +
                ", gate='" + gate + '\'' +
                ", province=" + province +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getFloor() {
        return floor;
    }

    public String getGate() {
        return gate;
    }

    public char getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
