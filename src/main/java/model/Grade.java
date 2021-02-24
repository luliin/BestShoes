package model;

import java.sql.Timestamp;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:36
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Grade {
    private Integer id;
    private String grade;
    private Integer score;

    private Timestamp created;
    private Timestamp updated;


    public Grade() {}

    public Grade(Integer id, String grade, Integer score) {
        this.id = id;
        this.grade = grade;
        this.score = score;
    }

    public Grade(Integer id, String grade, Integer score, Timestamp created, Timestamp updated) {
        this.id = id;
        this.grade = grade;
        this.score = score;
        this.created = created;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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


}
