//package repository;
//
//import domain.Dino;
//
//import java.util.*;
//
//public class Repository {
//    private final List<Dino> dinoList = new ArrayList<>();
//
//    public void save(Dino d) { dinoList.add(d); }
//    public List<Dino> findAll() { return dinoList; }
//}
//
package repository;

import domain.Dino;
import domain.DinoData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final String url = "jdbc:mysql://localhost:3306/dino_db";
    private final String user = "apple";
    private final String password = "1234"; // 실제 비밀번호 입력

    public List<Dino> findAll() {
        List<Dino> list = new ArrayList<>();
        String sql = "SELECT * FROM dinosaur";

        try (
                Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                Dino dino = new DinoData(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("tear"),
                        rs.getInt("power"),
                        rs.getInt("hp"),
                        rs.getInt("skill_count"),
                        rs.getString("type"),
                        rs.getInt("price")
                );
                list.add(dino);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ 추가: id로 1마리 공룡 조회
    public Dino findById(int id) {
        String sql = "SELECT * FROM dinosaur WHERE id = ?";
        try (
                Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new DinoData(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("tear"),
                        rs.getInt("power"),
                        rs.getInt("hp"),
                        rs.getInt("skill_count"),
                        rs.getString("type"),
                        rs.getInt("price")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // 공룡이 없을 경우
    }
}


