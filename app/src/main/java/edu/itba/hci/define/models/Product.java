package edu.itba.hci.define.models;

/**
 * Created by Diego on 19/06/2015.
 */
public class Product extends ApiResponse {
    private int id;
    private String name;
    private int price;
    private String[] imageUrl;
    private Category category;
    private Category subcategory;

    public Product(int id, String name, int price, String[] imageUrl, Category category, Category subcategory) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.subcategory = subcategory;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String[] getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public Category getSubcategory() {
        return subcategory;
    }
}
