package model.daos;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 19:47
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class ItemDaoTest {

    ItemDao itemDao = new ItemDao();

    @Test
    public final void getItemFromNameTest() {
        int size = itemDao.getAll().size();
        int first = itemDao.get(2001).get().getItemId();
        itemDao.getItemListFromName("Chuck Taylor All Star").forEach(e -> System.out.println(e.getItemId()));
        assertEquals(88, size);
        assertEquals(first, 2001);
    }
}
