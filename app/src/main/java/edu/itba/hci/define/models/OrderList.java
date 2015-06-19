package edu.itba.hci.define.models;

import java.util.Iterator;
import java.util.List;

public class OrderList extends ApiResponse {

    private List<Order> orders;

    public OrderList(List<Order> orderList) {

        Iterator<Order> orderIterator = orderList.iterator();

        while (orderIterator.hasNext()) {

            Order order = orderIterator.next();

            if (order.getStatus() == OrderStatus.CREATED) {
                orderIterator.remove();
            }

        }

        this.orders = orderList;
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
