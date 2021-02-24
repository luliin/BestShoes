package model.daos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-23
 * Time: 01:31
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class SubcategoryDaoTest {

    SubcategoryDao subcategoryDao = new SubcategoryDao();

    @Test
    public void getAccessoriesCategoryTest(){
        int size = subcategoryDao.getAccessoriesCategories().size();
        assertEquals(3, size);
        assertEquals("Skosnören", subcategoryDao.getAccessoriesCategories().get(size-1).getType());
    }

    @Test
    public void getWomensCategoriesTest(){
        int size = subcategoryDao.getWomensCategories().size();
        assertEquals(11, size);
        assertEquals("REA", subcategoryDao.getWomensCategories().get(size-1).getType());
    }

    @Test
    public void getMensCategoriesTest(){
        int size = subcategoryDao.getMensCategories().size();
        assertEquals(10, size);
        assertEquals("REA", subcategoryDao.getMensCategories().get(size-1).getType());
    }

    @Test
    public void getKidsCategoriesTest(){
        int size = subcategoryDao.getKidsCategories().size();
        assertEquals(10, size);
        assertEquals("REA", subcategoryDao.getKidsCategories().get(size-1).getType());
    }

    //TODO: implement standard tests
}
