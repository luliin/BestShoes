package model.daos;

import model.Color;
import model.IDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 10:41
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class ColorDao implements IDao<Color> {
    private final Repository repository = new Repository();


    @Override
    public Optional<Color> get(long id) {
        return getAll().stream()
                .filter(color -> color.getId() == id)
                .findFirst();
    }

    public Optional<Color> get(Integer id) {
        return getAll().stream()
                .filter(color -> color.getId().equals(id))
                .findFirst();
    }

    public Optional<Color> get(Integer id, List<Color> colors) {
        return colors.stream()
                .filter(color -> color.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Color> getAll() {
        List<Color> colors = new ArrayList<>();
        String query = "select * from colors";

        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("color_id");
                String color = resultSet.getString("color");
                Timestamp created = resultSet.getTimestamp("created");
                Timestamp updated = resultSet.getTimestamp("updated");
                colors.add(new Color(id, color, created, updated));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return colors;
    }

    @Override
    public void save(Color color) {
        String sql = "call insert_color(?)";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, color.getColor());
            statement.executeQuery();
            System.out.println("Color inserted into db");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void update(Color color, String[] colorNameColumnNewValue) {
        String sql = "update colors set "+ colorNameColumnNewValue[1] +" = ? where "+ colorNameColumnNewValue[1] + " = ?";
        Dao.update(colorNameColumnNewValue, sql, repository);
    }



    @Override
    public void delete(Color color) {
        String sql = "delete from colors where color = ?";
        try (Connection connection = DriverManager.getConnection(repository.CONNECTION_STRING, repository.USERNAME, repository.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, color.getColor());
            statement.executeUpdate();
            System.out.println("Color deleted");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Optional<Color> getFromName(String color) {
        return getAll().stream()
                .filter(c -> c.getColor().equalsIgnoreCase(color))
                .findFirst();
    }
}
