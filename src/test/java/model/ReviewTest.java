package model;

import model.daos.ReviewDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-23
 * Time: 09:20
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
class ReviewTest {

    private ReviewDao reviewDao;

    @BeforeEach
    void setUp() {
        reviewDao = new ReviewDao();
    }
    @Test
    void getAllTest() {
       List<Review> reviews =  reviewDao.getAll();
       assertEquals(0, reviews.size());
    }
}