package model.daos;

import model.IDao;
import model.Subcategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 13:34
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class SubcategoryDao implements IDao<Subcategory> {

    Repository repository = new Repository();

    @Override
    public Optional<Subcategory> get(long id) {
        return getAll().stream()
                .filter(subcategory -> subcategory.getId() == id)
                .findFirst();
    }
    public Optional<Subcategory> get(Integer id) {
        return getAll().stream()
                .filter(subcategory -> subcategory.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Subcategory> getAll() {
        String query = "select * from subcategories";
        List<Subcategory> subcategories = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                Integer subcategoryId = resultSet.getInt("subcategory_id");
                String type = resultSet.getString("type");
                Integer flag = resultSet.getInt("flag");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");
                subcategories.add(new Subcategory(subcategoryId, type, flag, created, updated));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return subcategories;
    }

    @Override
    public void save(Subcategory subcategory) {
        String sql = "call insert_subcategory(?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, subcategory.getType());
            statement.executeQuery();
            System.out.println("Subcategory inserted into db");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(Subcategory subcategory, String[] oldValueColumnNewValue) {
        String sql = "update subcategories set "+ oldValueColumnNewValue[1] +" = ? where "+ oldValueColumnNewValue[1] + " = ?";
        Dao.update(oldValueColumnNewValue, sql, repository);

    }

    @Override
    public void delete(Subcategory subcategory) {
        String sql = "delete from subcategories where subcategory_id = ?";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, subcategory.getId());
            statement.executeUpdate();
            System.out.println("Subcategory deleted");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<Subcategory> getWomensCategories() {
        return getAll().stream()
                .filter(subcategory -> subcategory.getFlag() <= 1)
                .collect(Collectors.toList());
    }

    public List<Subcategory> getMensCategories() {
        return getAll().stream()
                .filter(subcategory -> subcategory.getFlag() == 2 || subcategory.getFlag() == 0)
                .collect(Collectors.toList());
    }

    public List<Subcategory> getKidsCategories() {
        return getAll().stream()
                .filter(subcategory -> subcategory.getFlag() == 3 || subcategory.getFlag() == 0)
                .collect(Collectors.toList());
    }

    public List<Subcategory> getAccessoriesCategories() {
        return getAll().stream()
                .filter(subcategory -> subcategory.getFlag() == 4)
                .collect(Collectors.toList());
    }



}
