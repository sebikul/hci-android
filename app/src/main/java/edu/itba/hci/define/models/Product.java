package edu.itba.hci.define.models;


import java.util.Arrays;

public class Product extends ApiResponse {
    private int id;
    private String name;
    private int price;
    private String[] imageUrl;
    private Category category;
    private Category subcategory;
    private Attribute[] attributes;
    private String brand;


    public Product(int id, String name, int price, String[] imageUrl, Category category, Category subcategory, Attribute[] attributes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.subcategory = subcategory;
        this.attributes = attributes;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl=" + Arrays.toString(imageUrl) +
                ", category=" + category +
                ", subcategory=" + subcategory +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
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

    public String getBrand(){
        if(brand!=null)
            return brand;
        for(Attribute a : attributes){
            if(a.getName().equals("Marca"))
                return brand=a.getValues()[0];
        }
        return "--";
    }
}
