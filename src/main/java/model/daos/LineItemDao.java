package model.daos;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 13:32
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class LineItemDao implements IDao<LineItem> {

    Repository repository = new Repository();
    @Override
    public Optional<LineItem> get(long id) {
        return getAll().stream()
                .filter(lineItem -> lineItem.getId() == id)
                .findFirst();
    }


    @Override
    public List<LineItem> getAll() {
        List<LineItem> lineItems = new ArrayList<>();
        String query = "select * from line_items";
        List<Order> orders = Dao.order.getAll();
        List<Item> items = Dao.item.getAll();

        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery(query)) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("line_item_id");
                Integer orderId = resultSet.getInt("order_id");
                int itemModelId = resultSet.getInt("item_model_id");
                Integer quantity = resultSet.getInt("quantity");
                float lineCost = resultSet.getFloat("line_cost");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");
                Order order = Dao.order.get(orderId, orders).get();
                Item item = Dao.item.get(itemModelId, items).get();
                LineItem lineItem = new LineItem(id, order,
                        item, quantity, lineCost, created, updated);
                lineItems.add(lineItem);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return lineItems;
    }

    @Override
    public void save(LineItem lineItem) {
        //TODO: implement

    }

    @Override
    public void update(LineItem lineItem, String[] parameters) {
        //TODO: implement

    }

    @Override
    public void delete(LineItem lineItem) {
        //TODO: implement
    }

    public List<LineItem> getLineItemsFromOrderId(Integer orderId) {
        return getAll().stream()
                .filter(lineItem -> lineItem.getOrder().getId().equals(orderId))
                .collect(Collectors.toList());
    }

    public List<LineItem> getLineItemsFromOrderId(Integer orderId, List<LineItem> lineItems) {
        return lineItems.stream()
                .filter(lineItem -> lineItem.getOrder().getId().equals(orderId))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        LineItemDao line = new LineItemDao();
        System.out.println(line.getAll().size());

//        var test = line.getLineItemsFromOrderId(1);
    }
}
