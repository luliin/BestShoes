package model;

import java.sql.Timestamp;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:35
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class LineItem {

    private Integer id;
    private Order order;
    private Item item;
    private Integer quantity;
    private float line_cost;
    protected Timestamp created;
    protected Timestamp updated;

    public LineItem(Integer id, Order order, Item item, Integer quantity, float line_cost) {
        this.id = id;
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.line_cost = line_cost;
    }

    public LineItem(Integer id, Order order, Item item, Integer quantity, float line_cost, Timestamp created, Timestamp updated) {
        this.id = id;
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.line_cost = line_cost;
        this.created = created;
        this.updated = updated;
    }

    public LineItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public float getLine_cost() {
        return line_cost;
    }

    public void setLine_cost(float line_cost) {
        this.line_cost = line_cost;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return String.format(item + " %15d %15.2f", quantity, line_cost);
    }
}
