package edu.itba.hci.define.models;

import java.util.List;

public class SubcategoryList extends ApiResponse {

    private List<Subcategory> subcategories;

    public SubcategoryList(List<Subcategory> subcategories) {

        this.subcategories = subcategories;
    }

    @Override
    public String toString() {
        return "SubcategoryList{" +
                "subcategories=" + subcategories +
                '}';
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }
}
