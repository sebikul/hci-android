package edu.itba.hci.define.models;

public class Order extends ApiResponse {

    private int id;
    private Address address;
    private CreditCard creditCard;
    private OrderStatus status;
    private String receivedDate;
    private String processedDate;
    private String shippedDate;
    private String deliveredDate;
    private int latitude;
    private int longitude;

    public Order(int id, Address address, CreditCard creditCard, OrderStatus status, String receivedDate, String processedDate, String shippedDate, String deliveredDate, int latitude, int longitude) {
        this.id = id;
        this.address = address;
        this.creditCard = creditCard;
        this.status = status;
        this.receivedDate = receivedDate;
        this.processedDate = processedDate;
        this.shippedDate = shippedDate;
        this.deliveredDate = deliveredDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Order() {

    }

    @Override
    public String toString() {
        return "Order{" +
                "error=" + error +
                ", id=" + id +
                ", address=" + address +
                ", creditCard=" + creditCard +
                ", status=" + status +
                ", receivedDate='" + receivedDate + '\'' +
                ", processedDate='" + processedDate + '\'' +
                ", shippedDate='" + shippedDate + '\'' +
                ", deliveredDate='" + deliveredDate + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }
}
