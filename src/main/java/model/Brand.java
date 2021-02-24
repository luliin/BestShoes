package model;

import java.sql.Timestamp;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:27
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Brand {
    private Integer id;
    private String name;
    private Timestamp created;
    private Timestamp updated;

    public Brand() {}

    public Brand(Integer id, String name, Timestamp created, Timestamp updated) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Brand{" + name +"}";
    }
}
