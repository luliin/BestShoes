package model;

import java.sql.Timestamp;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:36
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Review {
    private Integer id;
    private Customer customer;
    private Product product;
    private Grade grade;
    private String comment;
    private Timestamp created;
    private Timestamp updated;

    public Review(Integer id, Customer customer, Product product, Grade grade,String comment, Timestamp created, Timestamp updated) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.grade = grade;
        this.comment = comment;
        this.created = created;
        this.updated = updated;
    }

    public Review() {}

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
