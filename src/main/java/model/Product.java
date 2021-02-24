package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:25
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public abstract class Product {
    protected Integer productId;
    protected String name;
    protected Brand brand;
    protected ProductCategory productCategory;
    protected Timestamp createdProduct;
    protected Timestamp updatedProduct;
    protected List<Subcategory> subcategories = new ArrayList<>();

    public Product() {}

    public Product(Integer id, String name, Brand brand, ProductCategory productCategory) {
        this.productId = id;
        this.name = name;
        this.brand = brand;
        this.productCategory = productCategory;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Timestamp getCreatedProduct() {
        return createdProduct;
    }

    public void setCreatedProduct(Timestamp createdProduct) {
        this.createdProduct = createdProduct;
    }

    public Timestamp getUpdatedProduct() {
        return updatedProduct;
    }

    public void setUpdatedProduct(Timestamp updatedProduct) {
        this.updatedProduct = updatedProduct;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public void addSubcategory(Subcategory subcategory) {
        if(!subcategories.contains(subcategory)) {
            subcategories.add(subcategory);
        }
    }
}
