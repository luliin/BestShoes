package model.daos;

import model.*;

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
public class ReviewDao implements IDao<Review> {
    Repository repository = new Repository();

    @Override
    public Optional<Review> get(long id) {
        return getAll().stream()
                .filter(review -> review.getId() == id)
                .findFirst();
    }
    public Optional<Review> get(Integer id) {
        return getAll().stream()
                .filter(review -> review.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Review> getAll() {
        List<Review> reviews = new ArrayList<>();
        List<Customer> customers = Dao.customer.getAll();
        List<Item> items = Dao.item.getAll();
        List<Grade> grades = Dao.grade.getAll();
        String query = "select * from reviews";

        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("review_id");
                int customerId = resultSet.getInt("customer_id");
                int itemId = resultSet.getInt("item_id");
                int gradeId = resultSet.getInt("grade_id");
                String comment = resultSet.getString("comment");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");

                Customer customer = Dao.customer.get(customerId, customers).get();
                Grade grade = Dao.grade.get(gradeId, grades).get();
                Item item = Dao.item.get(itemId, items).get();
                Review review = new Review(id, customer, item, grade, comment, created, updated);
                reviews.add(review);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return reviews;
    }

    public List<String> getAllReviewsWithComments(int productId) {
        List<String> reviews = new ArrayList<>();
        String query = "select grade, comment from grades_with_comment where item_id = ? and comment is not null";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String grades = result.getString("grade");
                String comment = result.getString("comment");
                if (comment != null && !comment.isEmpty()) {
                    reviews.add("Betyg: "+grades + "\nKommentar: " + comment + "\n");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return reviews;
    }

    @Override
    public void save(Review review) {
        //TODO: add new stored proc and change call
        String sql = "call rate(?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, review.getCustomer().getId());
            statement.setInt(2, review.getCustomer().getId());
            statement.setInt(3, review.getId());
            statement.setInt(4, review.getId());
            statement.executeQuery();
            System.out.println("Review inserted into db");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(Review review, String[] oldValueColumnNewValue) {
        String sql = "update reviews set "+ oldValueColumnNewValue[1] +" = ? where "+ oldValueColumnNewValue[1] + " = ?";
        Dao.update(oldValueColumnNewValue, sql, repository);
    }

    @Override
    public void delete(Review review) {
        String sql = "delete from reviews where review_id = ?";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, review.getId());
            statement.executeUpdate();
            System.out.println("Review deleted");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
