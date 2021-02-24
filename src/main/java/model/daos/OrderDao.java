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
public class OrderDao implements IDao<Order> {
    public static Repository repository = new Repository();
    @Override
    public Optional<Order> get(long id) {
        return getAll().stream()
                .filter(order -> order.getId() == id)
                .findFirst();
    }

    public Optional<Order> get(Integer id) {
        return getAll().stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    public Optional<Order> get(Integer id, List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        CustomerDao customerDao = new CustomerDao();
        String query = "select * from orders";

        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("order_id");
                int customerId = resultSet.getInt("customer_id");
                float totalCost = resultSet.getFloat("total_cost");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");

                Order order = new Order(id, customerDao.get(customerId).orElse(new Customer()),
                        totalCost, created, updated);
                orders.add(order);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return orders;
    }
//IN _customer_id int, IN _order_id int, IN _item_model_id int
    public static int addToCart(Customer customer, Order order, Item item) {

        String sql = "call add_to_cart(?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, customer.getId());
            if (order.getId()==null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, order.getId());
            }
            if (order.getId()==null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, order.getId());
            }
            statement.setInt(3, item.getItemId());
            statement.executeQuery();
            return statement.getInt(4);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return order.getId();
    }

    @Override
    public void save(Order order) {

        //TODO: implement

    }

    @Override
    public void update(Order order, String[] parameters) {
        //TODO: implement

    }

    @Override
    public void delete(Order order) {
        if (order.getId() != null) {
            String query = "delete from orders where order_id = ?";

            try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, order.getId());
                statement.execute();
                System.out.println("Borttagning lyckad.");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public List<Order> getAllOrdersFromCustomer(Integer customerId) {
        return getAll().stream()
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());
    }


}
