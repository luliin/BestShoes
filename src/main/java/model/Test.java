package model;

import model.daos.Dao;
import model.daos.LineItemDao;
import model.daos.OrderDao;
import model.daos.Repository;

import java.sql.*;
import java.util.List;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 22:18
 * Project: ShoesJavaFx
 * Copyright: MIT
 */

/*
in _brand varchar(50), in _item varchar(50), in _color varchar(50), in _price int,
    in _min_size int, in _max_size int, in _category varchar(50), out _item_id int)
 */

public class Test {
    public static void main(String[] args) {



//        String sql= "call add_item(?, ?, ?, ?, ?, ?, ?, ?)";
//        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
//             CallableStatement statement = connection.prepareCall(sql)) {
//            statement.setString(1, "Lingongrova");
//            statement.setString(2, "Blingblingsko");
//            statement.setString(3, "Flerfärgad");
//            statement.setFloat(4, 1199);
//            statement.setInt(5, 36);
//            statement.setInt(6, 42);
//            statement.setString(7, "Skor");
//            statement.registerOutParameter(8, Types.INTEGER);
//            statement.execute();
//        } catch (SQLException throwables) {
//            System.out.println(throwables.getSQLState() + throwables.getMessage());
//        }
//
//
//        sql = "call add_to_cart(?, ?, ?)";
//        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
//             CallableStatement statement = connection.prepareCall(sql)) {
//            statement.setInt(1, 1);
//            statement.setInt(2, 1);
//            statement.setInt(3, 1003);
//            statement.execute();
//        } catch (SQLException throwables) {
//            System.out.println(throwables.getSQLState() + " "+ throwables.getMessage());
//        }
        System.out.println(Dao.item.getItemListFromName("Ella"));

        System.out.println(Dao.item.getAverageGrade(1011));

    }
}