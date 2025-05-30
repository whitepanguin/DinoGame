package repository;

import config.DBConfig;
import domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
    }

    public void save(User user) {
        String sql = "INSERT INTO users (points, item_ids, dino_ids, max_stage, current_stage, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.points);
            stmt.setString(2, user.itemIds);
            stmt.setString(3, user.dinoIds);
            stmt.setInt(4, user.maxStage);
            stmt.setInt(5, user.currentStage);
            stmt.setTimestamp(6, Timestamp.valueOf(user.createdAt));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET points = ?, item_ids = ?, dino_ids = ?, max_stage = ?, current_stage = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.points);
            stmt.setString(2, user.itemIds);
            stmt.setString(3, user.dinoIds);
            stmt.setInt(4, user.maxStage);
            stmt.setInt(5, user.currentStage);
            stmt.setInt(6, user.id);

            stmt.executeUpdate();
            System.out.println("✅ 사용자 정보 업데이트 완료 (id=" + user.id + ")");
        } catch (SQLException e) {
            System.out.println("❌ 사용자 업데이트 실패:");
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getInt("points"),
                        rs.getString("item_ids"),
                        rs.getString("dino_ids"),
                        rs.getInt("max_stage"),
                        rs.getInt("current_stage"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getInt("points"),
                        rs.getString("item_ids"),
                        rs.getString("dino_ids"),
                        rs.getInt("max_stage"),
                        rs.getInt("current_stage"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            } else {
                System.out.println("❌ 해당 ID의 사용자를 찾을 수 없습니다. (id=" + id + ")");
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
