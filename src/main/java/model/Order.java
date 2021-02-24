package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:36
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Order {
    private Integer id = null;
    private Customer customer;
    private float totalCost;
    protected Timestamp created;
    protected Timestamp updated;
    List<LineItem> lineItems = new ArrayList<>();

    public Order() {}

    public Order(Integer id, Customer customer, float totalCost, Timestamp created, Timestamp updated) {
        this.id = id;
        this.customer = customer;
        this.totalCost = totalCost;
        this.created = created;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
            this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
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

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public void addToCart(Item item) {

    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", totalCost=" + totalCost +
                ", created=" + created +
                '}';
    }

    public static Order create(Customer customer) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setCreated(Timestamp.from(Instant.now()));
        return order;
    }
}
