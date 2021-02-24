package model.daos;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:32
 * Project: ShoesJavaFx
 * Copyright: MIT
 */

public class Repository {

    public String CONNECTION_STRING;
    public String USERNAME;
    public String PASSWORD;

    public Repository() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/Settings.properties"));
            CONNECTION_STRING = properties.getProperty("connectionString");
            USERNAME = properties.getProperty("name");
            PASSWORD = properties.getProperty("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}