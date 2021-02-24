package model.daos;


import model.Grade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 12:12
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class GradeDaoTest {
    Repository repository = new Repository();
    GradeDao gradeDao = new GradeDao();

    @Test
    public final void saveTest() {
        Grade grade = new Grade();
        grade.setGrade("Uruselt");
        gradeDao.save(grade);
    }

    @Test
    public final void updateTest() {
        List<Grade> grades = gradeDao.getAll();
        gradeDao.update(grades.get(grades.size() - 1), new String[]{"Uruselt", "grade", "Miserabelt"});
        assertEquals("Miserabelt", gradeDao.getAll().get(grades.size() - 1).getGrade());
    }

    @Test
    public final void getAllTest() {
        assertEquals(gradeDao.getAll().size(), 5);
    }


    @Test
    public final void getTest() {
        assertSame(gradeDao.getAll().get(1), gradeDao.get(2).get());
    }

    @Test
    public final void getFromNameTest() {
        List<Grade> grades = gradeDao.getAll();
        assertSame(gradeDao.getAll().get(grades.size()-1), gradeDao.getFromName("Miserabelt").get());
    }

    @Test
    public final void deleteTest() {
        int size = gradeDao.getAll().size();
        gradeDao.delete(gradeDao.getFromName("Miserabelt").get());
        assertEquals(size - 1, gradeDao.getAll().size());
    }


}
