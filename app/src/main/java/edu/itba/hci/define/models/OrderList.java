package edu.itba.hci.define.models;

import java.util.List;

public class OrderList extends ApiResponse {

    private List<Order> orders;

    public OrderList(List<Order> orders) {

        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderList{" +
                "orders=" + orders +
                '}';
    }

    public List<Order> getOrders() {
        return orders;
    }

}
