package model.daos;

import model.IDao;
import model.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 13:33
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class ProductCategoryDao implements IDao<ProductCategory> {

    Repository repository = new Repository();
    @Override
    public Optional<ProductCategory> get(long id) {
        return getAll().stream()
                .filter(color -> color.getId() == id)
                .findFirst();
    }
    public Optional<ProductCategory> get(Integer id) {
        return getAll().stream()
                .filter(color -> color.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ProductCategory> getAll() {
        String query = "select * from product_categories";
        List<ProductCategory> productCategories = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                Integer productCategoryId = resultSet.getInt("product_category_id");
                String type = resultSet.getString("type");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");
                productCategories.add(new ProductCategory(productCategoryId, type, created, updated));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return productCategories;
    }

    @Override
    public void save(ProductCategory productCategory) {
        String sql = "call insert_product_category(?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, productCategory.getType());
            statement.executeQuery();
            System.out.println("Product Category inserted into db");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(ProductCategory productCategory, String[] oldValueColumnNewValue) {
        String sql = "update product_categories set "+ oldValueColumnNewValue[1] +" = ? where "+ oldValueColumnNewValue[1] + " = ?";
        Dao.update(oldValueColumnNewValue, sql, repository);
    }

    @Override
    public void delete(ProductCategory productCategory) {
        String sql = "delete from product_categories where product_category_id = ?";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productCategory.getId());
            statement.executeUpdate();
            System.out.println("Product Category deleted");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
