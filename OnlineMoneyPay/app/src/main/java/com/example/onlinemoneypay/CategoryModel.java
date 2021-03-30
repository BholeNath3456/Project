package com.example.onlinemoneypay;

public class CategoryModel {
    private String categoryIconLink;
    private String categoryname;

    public CategoryModel(String categoryIconLink, String categoryname) {
        this.categoryIconLink = categoryIconLink;
        this.categoryname = categoryname;
    }

    public String getCategoryIconLink() {
        return categoryIconLink;
    }

    public void setCategoryIconLink(String categoryIconLink) {
        this.categoryIconLink = categoryIconLink;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public CategoryModel() {
    }
}
