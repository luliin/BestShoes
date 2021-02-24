package model.daos;

import model.Customer;
import model.IDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 11:31
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class CustomerDao implements IDao<Customer> {

    private final Repository repository = new Repository();


    @Override
    public Optional<Customer> get(long id) {
        return getAll().stream()
                .filter(brand -> brand.getId() == id)
                .findFirst();
    }
    public Optional<Customer> get(Integer id) {
        return getAll().stream()
                .filter(brand -> brand.getId().equals(id))
                .findFirst();
    }
    public Optional<Customer> get(long id, List<Customer> customers) {
        return customers.stream()
                .filter(brand -> brand.getId() == id)
                .findFirst();
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        String query = "select * from customers";

        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("customer_id");
                String shortName = resultSet.getString("short_name");
                String fullName = resultSet.getString("full_name");
                String personalNumber = resultSet.getString("personal_number");
                String phoneNumber = resultSet.getString("phone_number");
                String email = resultSet.getString("email");
                String streetAddress = resultSet.getString("street_address");
                String zipCode = resultSet.getString("zip_code");
                String city = resultSet.getString("city");
                String password = resultSet.getString("password");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");
                customers.add(new Customer(id, shortName, fullName, personalNumber, phoneNumber, email
                        , streetAddress, zipCode, city, password, created, updated));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return customers;
    }

    @Override
    public void save(Customer customer) {
        String sql = "call insert_customer(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {

            if (customer.getShortName() != null) {
                statement.setString(1, customer.getShortName());
            } else {
                statement.setNull(1, Types.VARCHAR);
            }
            statement.setString(2, customer.getFullName());
            if (customer.getPersonalNumber() != null) {
                statement.setString(3, customer.getPersonalNumber());
            } else {
                statement.setNull(3, Types.VARCHAR);
            }
            if (customer.getPhoneNumber() != null) {
                statement.setString(4, customer.getPhoneNumber());
            } else {
                statement.setNull(4, Types.VARCHAR);
            }
                statement.setString(5, customer.getEmail());

            if (customer.getStreetAddress() != null) {
                statement.setString(6, customer.getStreetAddress());
            } else {
                statement.setNull(6, Types.VARCHAR);
            }
            if (customer.getZipCode() != null) {
                statement.setString(7, customer.getZipCode());
            } else {
                statement.setNull(7, Types.VARCHAR);
            }
            if (customer.getCity() != null) {
                statement.setString(8, customer.getCity());
            } else {
                statement.setNull(8, Types.VARCHAR);
            }
            statement.setString(9, customer.getPassword());
            statement.executeQuery();
            System.out.println("Customer inserted into db");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String saveWithMessage(Customer customer) {
        String sql = "call insert_customer(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, customer.getShortName());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getPersonalNumber());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getEmail());
            statement.setString(6, customer.getStreetAddress());
            statement.setString(7, customer.getZipCode());
            statement.setString(8, customer.getCity());
            statement.setString(9, customer.getPassword());
            statement.executeQuery();
            return "Customer inserted into db";
        } catch (SQLIntegrityConstraintViolationException constraintViolationException) {
           return constraintViolationException.getMessage();
        } catch (Exception exception) {
            exception.printStackTrace();
            return exception.getMessage();
        }
    }

    @Override
    public void update(Customer customer, String[] oldValueColumnNewValue) {
        String sql = "update customers set "+ oldValueColumnNewValue[1] +" = ? where "+ oldValueColumnNewValue[1] + " = ?";
        Dao.update(oldValueColumnNewValue, sql, repository);
    }

    @Override
    public void delete(Customer customer) {
        String sql = "delete from customers where customer_id = ?";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customer.getId());
            statement.executeUpdate();
            System.out.println("Customer deleted");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Optional<Customer> getFromEmail(String email) {
        return getAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}
