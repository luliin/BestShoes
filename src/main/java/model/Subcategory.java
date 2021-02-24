package model;

import java.sql.Timestamp;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:26
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Subcategory {
    private Integer id;
    private String type;
    private Integer flag;
    protected Timestamp created;
    protected Timestamp updated;

    public Subcategory() {}

    public Subcategory(Integer id, String type, Timestamp created, Timestamp updated) {
        this.id = id;
        this.type = type;
        this.created = created;
        this.updated = updated;
    }

    public Subcategory(Integer id, String type, Integer flag, Timestamp created, Timestamp updated) {
        this.id = id;
        this.type = type;
        this.flag = flag;
        this.created = created;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
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
        return "Subcategory{" + type + "}";
    }
}
