package edu.itba.hci.define.models;

public class CreditCard extends ApiResponse {

    private int id;
    private String number;
    private String expirationDate;
    private String securityCode;

    public CreditCard(int id, String number, String expirationDate, String securityCode) {

        this.id = id;
        this.number = number;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", securityCode='" + securityCode + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }
}
