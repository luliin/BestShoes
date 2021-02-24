package model;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:36
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Color {
    private Integer id;
    private String color;
    private Timestamp created;
    private Timestamp updated;

    public Color() {
    }

    public Color(Integer id, String color, Timestamp created, Timestamp updated) {
        this.id = id;
        this.color = color;
        this.created = created;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color1 = (Color) o;
        return Objects.equals(color, color1.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
