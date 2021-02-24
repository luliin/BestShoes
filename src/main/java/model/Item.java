package model;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:25
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Item extends Product {
    private Integer itemId;
    private String size;
    private Color color;
    private Float price;
    private Integer quantity;
    protected Timestamp createdItem;
    protected Timestamp updatedItem;

    public Item() {
    }

    public Item(Integer itemId, String size, Color color, Float price, Integer quantity, Timestamp createdItem, Timestamp updatedItem) {
        this.itemId = itemId;
        this.size = size;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
        this.createdItem = createdItem;
        this.updatedItem = updatedItem;
    }

    public Item(Integer productId, String name, Brand brand, ProductCategory productCategory, Integer itemId, String size, Color color, Float price, Integer quantity, Timestamp createdItem, Timestamp updatedItem) {
        super(productId, name, brand, productCategory);
        this.itemId = itemId;
        this.size = size;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
        this.createdItem = createdItem;
        this.updatedItem = updatedItem;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedItem() {
        return createdItem;
    }

    public void setCreatedItem(Timestamp createdItem) {
        this.createdItem = createdItem;
    }

    public Timestamp getUpdatedItem() {
        return updatedItem;
    }

    public void setUpdatedItem(Timestamp updatedItem) {
        this.updatedItem = updatedItem;
    }

    @Override
    public String toString() {
        return String.format("%15s %15s %15s: %s %15s %15.2f kronor", name, brand.getName(),"Storlek: ",
                size, color, price);
    }

    public String getNameAndBrand() {
        return String.format("%s, %s", brand.getName(), name);
    }
}
