package model.daos;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 13:31
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class ItemDao implements IDao<Item> {
    private final Repository repository = new Repository();

    @Override
    public Optional<Item> get(long id) {
        return getAll().stream()
                .filter(grade -> grade.getItemId() == id)
                .findFirst();
    }

    public Optional<Item> get(long id, List<Item> items) {
        return items.stream()
                .filter(grade -> grade.getItemId() == id)
                .findFirst();
    }

    public Optional<Item> getFromProductId(long id) {
        return getAll().stream()
                .filter(item -> item.getProductId() == id)
                .findFirst();
    }

    public Optional<Item> getFromProductId(Integer id) {
        return getAll().stream()
                .filter(item -> item.getProductId().equals(id))
                .findFirst();
    }

    public Optional<Integer> getProductIdFromItemId(long id) {
        return getAll().stream()
                .filter(grade -> grade.getItemId() == id)
                .map(Item::getProductId)
                .findFirst();
    }

    @Override
    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        List<Color> colors = Dao.color.getAll();
        List<Brand> brands = Dao.brand.getAll();
//TODO: PRODUCTDAO

        ProductCategoryDao productCategoryDao = new ProductCategoryDao();

        String query = "select * from item_models" +
                " join items using (item_id)" +
                "join brands using (brand_id)" +
                "join product_categories using (product_category_id)" +
                "left join colors using (color_id)";


        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {

                Integer productId = resultSet.getInt("item_id");
                String itemName = resultSet.getString("item_name");
                Integer brandId = resultSet.getInt("brand_id");
                Integer productCategoryId = resultSet.getInt("product_category_id");
                Integer itemModelId = resultSet.getInt("item_model_id");
                String size = resultSet.getString("size");
                Integer colorId = resultSet.getInt("color_id");
                Float price = resultSet.getFloat("price");
                Integer quantity = resultSet.getInt("quantity");
                Timestamp created = resultSet.getTimestamp("item_models.created");
                Timestamp updated = resultSet.getTimestamp("item_models.updated");

                Brand brand = Dao.brand.get(brandId, brands).orElse(new Brand());
                Color color = Dao.color.get(colorId, colors).orElse(null);
                ProductCategory productCategory = productCategoryDao.get(productCategoryId).orElse(null);
                Item item = new Item(productId, itemName, brand, productCategory, itemModelId, size, color,
                        price, quantity, created, updated);
                items.add(item);
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return items;
    }

    public Float getAverageGrade(int productId) {
        String query = "{? = call average_grade(?)}";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(query)) {
            statement.registerOutParameter(1, Types.FLOAT);
            statement.setInt(2, productId);
            statement.execute();
            return statement.getFloat(1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0f;
    }

    public void rate(int customerId, int productId, int gradeId, String comment) {
        String query = "{ call rate(?, ?, ?, ?)}";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(query)) {
            statement.setInt(1, customerId);
            statement.setInt(2, productId);
            statement.setInt(3, gradeId);
            statement.setString(4, comment);
            statement.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void save(Item item) {
        String sql = "call insert_item_and_item_model(?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getBrand().getId());
            statement.setInt(3, item.getProductCategory().getId());
            statement.setString(4, item.getSize());
            statement.setInt(5, item.getColor().getId());
            statement.setFloat(6, item.getPrice());
            statement.executeQuery();
            System.out.println("Item inserted into db");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(Item item, String[] oldValueColumnNewValue) {
        String sql = "update item_models set " + oldValueColumnNewValue[1] + " = ? where " + oldValueColumnNewValue[1] + " = ?";
        Dao.update(oldValueColumnNewValue, sql, repository);
    }

    @Override
    public void delete(Item item) {
        throw new UnsupportedOperationException();
    }

    public void updateProduct(Item item, String[] oldValueColumnNewValue) {
        String sql = "update items set " + oldValueColumnNewValue[1] + " = ? where " + oldValueColumnNewValue[1] + " = ?";
        Dao.update(oldValueColumnNewValue, sql, repository);
    }


    public void delete(Item item, boolean isDeleteParent) {

        String sql = isDeleteParent ? "delete from items where item_id = ?"
                : "delete from item_models where item_model_id = ?";

        int id = isDeleteParent ? item.getProductId() : item.getItemId();

        String deleteMessage = isDeleteParent ? "Item deleted" : "Item model deleted";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println(deleteMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public Optional<Item> getFromProductName(String productName) {
        return getAll().stream()
                .filter(item -> item.getName().equalsIgnoreCase(productName))
                .findFirst();
    }

    public List<Item> getItemListFromName(String name) {
        return getAll().stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public Map<Color, List<Item>> getColorOptions(List<Item> items) {
        return items.stream()
                .collect(Collectors.groupingBy(Item::getColor));
    }

    public Map<Integer, Item> getIndexedMap(List<Item> items) {
        List<Item> filtered = items.stream()
                .filter(item -> item.getQuantity() > 0)
                .collect(Collectors.toList());

        return IntStream.range(0, filtered.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, filtered::get));

    }


}
