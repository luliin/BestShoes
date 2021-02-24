package model.daos;

import model.IDao;
import model.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 11:56
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class GradeDao  implements IDao<Grade> {

    private Repository repository = new Repository();

    @Override
    public Optional<Grade> get(long id) {
        return getAll().stream()
                .filter(grade -> grade.getId() == id)
                .findFirst();
    }
    public Optional<Grade> get(Integer id) {
        return getAll().stream()
                .filter(grade -> grade.getId().equals(id))
                .findFirst();
    }
    public Optional<Grade> get(long id, List<Grade> grades) {
        return grades.stream()
                .filter(grade -> grade.getId() == id)
                .findFirst();
    }

    @Override
    public List<Grade> getAll() {
        List<Grade> grades = new ArrayList<>();
        String query = "select * from grades";

        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("grade_id");
                String grade = resultSet.getString("grade");
                Integer score = resultSet.getInt("score");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");
                grades.add(new Grade(id, grade, score, created, updated));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return grades;
    }

    @Override
    public void save(Grade grade) {
        String sql = "call insert_grade(?, ?)";
        int score = grade.getScore()!=null ? grade.getScore() : 0;
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, grade.getGrade());
            statement.setInt(2, score);
            statement.executeQuery();
            System.out.println("Grade inserted into db");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(Grade grade, String[] oldValueColumnNewValue) {
        String sql = "update grades set "+ oldValueColumnNewValue[1] +" = ? where "+ oldValueColumnNewValue[1] + " = ?";
        Dao.update(oldValueColumnNewValue, sql, repository);
    }

    @Override
    public void delete(Grade grade) {
        String sql = "delete from grades where grade_id = ?";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, grade.getId());
            statement.executeUpdate();
            System.out.println("Grade deleted");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Optional<Grade> getFromName(String grade) {
        return getAll().stream()
                .filter(c -> c.getGrade().equalsIgnoreCase(grade))
                .findFirst();
    }
}
