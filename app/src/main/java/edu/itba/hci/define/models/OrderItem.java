package edu.itba.hci.define.models;

import java.io.Serializable;


public class OrderItem implements Serializable {

    private int id;
    private ProductItem product;
    private int quantity;
    private int price;

    public OrderItem(int id, ProductItem product, int quantity, int price) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {

        return id;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product.toString() +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public ProductItem getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
