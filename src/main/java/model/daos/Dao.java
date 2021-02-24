package model.daos;

import model.Brand;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-23
 * Time: 21:32
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Dao {
    public static BrandDao brand = new BrandDao();
    public static ColorDao color = new ColorDao();
    public static CustomerDao customer = new CustomerDao();
    public static GradeDao grade = new GradeDao();
    public static ItemDao item = new ItemDao();
    public static LineItemDao lineItem = new LineItemDao();
    public static OrderDao order = new OrderDao();
    public static ProductCategoryDao productCategory = new ProductCategoryDao();
    public static ReviewDao review = new ReviewDao();
    public static SubcategoryDao subcategory = new SubcategoryDao();

    public static void update(String[] oldValueColumnNewValue, String sql, Repository repository) {
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, oldValueColumnNewValue[2]);
            statement.setString(2, oldValueColumnNewValue[0]);
            statement.executeUpdate();
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void update(String[] oldValueColumnNewValue, String sql, Repository repository, boolean isIntegerValue) {
        if(isIntegerValue) {
            update(oldValueColumnNewValue, sql, repository);
        } else {
            try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(oldValueColumnNewValue[2]));
                statement.setInt(2, Integer.parseInt(oldValueColumnNewValue[0]));
                statement.executeUpdate();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
