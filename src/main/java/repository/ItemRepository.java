package repository;

import config.DBConfig;
import domain.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
    }

    public void save(Item item) {
        String sql = "INSERT INTO items (name, type, price, effect_description, effect_value, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.name);
            stmt.setString(2, item.type);
            stmt.setInt(3, item.price);
            stmt.setString(4, item.effectDescription);
            stmt.setDouble(5, item.effectValue);
            stmt.setTimestamp(6, Timestamp.valueOf(item.createdAt));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("price"),
                        rs.getString("effect_description"),
                        rs.getDouble("effect_value"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}
