package model.daos;

import model.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 11:11
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class ColorDaoTest {
ColorDao colorDao = new ColorDao();

    @Test
    public final void saveTest() {
        Color color = new Color();
        color.setColor("Cyan");
        colorDao.save(color);
    }

    @Test
    public final void updateTest() {
        List<Color> colors = colorDao.getAll();
        colorDao.update(colors.get(colors.size() - 1), new String[]{"Cyan", "color", "Petrol"});
        assertEquals("Petrol", colorDao.getAll().get(colors.size() - 1).getColor());
    }

    @Test
    public final void getAllTest() {
        assertEquals(colorDao.getAll().size(), 19);
    }


    @Test
    public final void getTest() {
        assertSame(colorDao.getAll().get(0), colorDao.get(1).get());
    }

    @Test
    public final void getFromNameTest() {
        List<Color> colors = colorDao.getAll();
        assertSame(colorDao.getAll().get(colors.size()-1), colorDao.getFromName("Petrol").get());
    }

    @Test
    public final void deleteTest() {
        int size = colorDao.getAll().size();
        colorDao.delete(colorDao.getFromName("Petrol").get());
        assertEquals(size - 1, colorDao.getAll().size());
    }

}
