package edu.itba.hci.define.models;

import java.util.List;


public class ProductList extends ApiResponse {

    private List<Product> products;
    private int total;

    public ProductList(List<Product> products, int total) {
        this.products = products;
        this.total = total;
    }

    @Override
    public String toString() {
        return "ProductList{" +
                "products=" + products +
                ", total=" + total +
                '}';
    }

    public List<Product> getProducts() {

        return products;
    }

    public int getTotal() {
        return total;
    }
}
