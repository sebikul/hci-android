package edu.itba.hci.define.models;

import java.util.List;

/**
 * Created by Diego on 19/06/2015.
 */
public class ProductList {

    private List<Product> products;

    public ProductList(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}
