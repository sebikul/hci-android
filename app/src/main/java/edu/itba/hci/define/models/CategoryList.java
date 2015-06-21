package edu.itba.hci.define.models;

import java.util.List;

public class CategoryList extends ApiResponse{

    private List<Category> categories;

    public CategoryList(List<Category> categories) {

        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoryList{" +
                "categories=" + categories +
                '}';
    }

    public List<Category> getCategories() {
        return categories;
    }
}
