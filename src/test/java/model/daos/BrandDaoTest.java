package model.daos;

import model.Brand;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 22:00
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class BrandDaoTest {

    private static BrandDao brandDao = new BrandDao();

    @Test
    public final void getAllTest() {
        assertEquals(brandDao.getAll().size(), 20);
    }

    @Test
    public final void saveTest() {
        Brand brand = new Brand();
        brand.setName("Gummistövlar");
        brandDao.save(brand);
    }

    @Test
    public final void getTest() {
        assertSame(brandDao.getAll().get(1), brandDao.get(2).get());
    }
    @Test
    public final void getFromNameTest() {
        assertSame(brandDao.getAll().get(0), brandDao.getFromName("Ecco").get());
    }

    @Test
    public final void deleteTest() {
        int size = brandDao.getAll().size();
        brandDao.delete(brandDao.get(size-1).get());
        assertEquals(size-1, brandDao.getAll().size());
    }

    @Test
    public final void updateTest() {
        List<Brand> brands = brandDao.getAll();
        brandDao.update(brands.get(brands.size()-1), new String[] {"Träskor", "brand_name", "Foppatofflor"});
        assertEquals("Foppatofflor", brandDao.getAll().get(brands.size()-1).getName());
    }

    @Test
    public final void getTest2() {
        var list = brandDao.getAll();
        Integer id = null;
        assertEquals(brandDao.get(id), Optional.empty());
    }





}
