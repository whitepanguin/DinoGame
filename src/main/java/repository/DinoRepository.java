package repository;

import config.DBConfig;
import domain.Dino;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DinoRepository {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
    }

    public List<Dino> findRandomDinosByTear(int tear, int limit) {
        List<Dino> dinos = new ArrayList<>();
        String sql = "SELECT * FROM dinos WHERE tear = ? ORDER BY RAND() LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tear);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dino d = new Dino(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("tear"),
                            rs.getInt("power"),
                            rs.getInt("hp"),
                            rs.getInt("skill_count"),
                            rs.getString("type"),
                            rs.getInt("price"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    dinos.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dinos;
    }

    public List<Dino> findRandomDinosByTypeAndTear(String type, int minTear, int maxTear, int limit) {
        List<Dino> dinos = new ArrayList<>();
        String sql = "SELECT * FROM dinos WHERE type = ? AND tear BETWEEN ? AND ? ORDER BY RAND() LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            stmt.setInt(2, minTear);
            stmt.setInt(3, maxTear);
            stmt.setInt(4, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dino d = new Dino(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("tear"),
                            rs.getInt("power"),
                            rs.getInt("hp"),
                            rs.getInt("skill_count"),
                            rs.getString("type"),
                            rs.getInt("price"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    dinos.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dinos;
    }


    public int save(Dino dino) {
        String sql = "INSERT INTO dinos (name, tear, power, hp, skill_count, type, price, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             // 🔽 GENERATED_KEYS를 요청함
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, dino.name);
            stmt.setInt(2, dino.tear);
            stmt.setInt(3, dino.power);
            stmt.setInt(4, dino.hp);
            stmt.setInt(5, dino.skillCount);
            stmt.setString(6, dino.type);
            stmt.setInt(7, dino.price);
            stmt.setTimestamp(8, Timestamp.valueOf(dino.createdAt));

            stmt.executeUpdate();

            // 🔽 생성된 ID 반환
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 실패 시 -1 반환
    }


    public List<Dino> findAll() {
        List<Dino> dinos = new ArrayList<>();
        String sql = "SELECT * FROM dinos";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Dino d = new Dino(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("tear"),
                        rs.getInt("power"),
                        rs.getInt("hp"),
                        rs.getInt("skill_count"),
                        rs.getString("type"),
                        rs.getInt("price"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                dinos.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dinos;
    }

    public Dino findById(int id) {
        String sql = "SELECT * FROM dinos WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Dino(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("tear"),
                        rs.getInt("power"),
                        rs.getInt("hp"),
                        rs.getInt("skill_count"),
                        rs.getString("type"),
                        rs.getInt("price"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }





}
