package edu.itba.hci.define.models;

import java.util.List;


public class ProductList extends ApiResponse {

    private List<Product> products;

    public ProductList(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductList{" +
                "products=" + products +
                '}';
    }

    public List<Product> getProducts() {
        return products;
    }
}
