package edu.itba.hci.define.models;

import java.util.Arrays;

public class Order extends ApiResponse {

    private int id;
    private AddressItem address;
    private CreditCardItem creditCard;
    private OrderStatus status;
    private String receivedDate;
    private String processedDate;
    private String shippedDate;
    private String deliveredDate;
    private int latitude;
    private int longitude;
    private boolean notifications = false;
    private OrderItem[] items;

    public Order(int id, AddressItem address, CreditCardItem creditCard, OrderStatus status, String receivedDate, String processedDate, String shippedDate, String deliveredDate, int latitude, int longitude, OrderItem[] items) {
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
        this.items = items;
    }

    public Order() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id == order.id;

    }

    public boolean hasNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", address=" + address +
                ", creditCard=" + creditCard +
                ", status=" + status +
                ", receivedDate='" + receivedDate + '\'' +
                ", processedDate='" + processedDate + '\'' +
                ", shippedDate='" + shippedDate + '\'' +
                ", deliveredDate='" + deliveredDate + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", notifications=" + notifications +
                ", items=" + Arrays.toString(items) +
                '}';
    }

    public int getId() {
        return id;
    }

    public AddressItem getAddress() {
        return address;
    }

    public CreditCardItem getCreditCard() {
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

    public int getSubtotal() {

        int subtotal = 0;

        for (int i = 0; i < items.length; i++) {
            OrderItem item = items[i];

            subtotal += item.getPrice();

        }

        return subtotal;
    }
}
