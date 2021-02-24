package model.daos;

import model.Brand;
import model.IDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 21:17
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class BrandDao implements IDao<Brand> {
    private final Repository repository = new Repository();

    @Override
    public Optional<Brand> get(long id) {
       return getAll().stream()
                .filter(brand -> brand.getId() == id)
                .findFirst();
    }
    public Optional<Brand> get(Integer id) {
        return getAll().stream()
                .filter(brand -> brand.getId().equals(id))
                .findFirst();
    }
    public Optional<Brand> get(Integer id, List<Brand> brands) {
        return brands.stream()
                .filter(brand -> brand.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Brand> getAll() {
        List<Brand> brands = new ArrayList<>();
        String query = "select * from brands";


        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("brand_id");
                String name = resultSet.getString("brand_name");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");
                brands.add(new Brand(id, name, created, updated));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return brands;
    }

    @Override
    public void save(Brand brand) {
        String sql = "call insert_brand(?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, brand.getName());
            statement.executeQuery();
            System.out.println("Brand inserted into db");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(Brand brand, String[] brandNameColumnNewValue) {
        String sql = "update brands set "+ brandNameColumnNewValue[1] +" = ? where "+ brandNameColumnNewValue[1] + " = ?";
        Dao.update(brandNameColumnNewValue, sql, repository);
    }

    @Override
    public void delete(Brand brand) {
        String sql = "delete from brands where brand_id = ?";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, brand.getId());
            statement.executeUpdate();
            System.out.println("Brand deleted");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Optional<Brand> getFromName(String brandName) {
        return getAll().stream()
                .filter(brand -> brand.getName().equalsIgnoreCase(brandName))
                .findFirst();
    }

    public static CustomerDao getInstance() {
        return new CustomerDao();
    }
}
