package test;

import config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {
    public static void main(String[] args) {
        System.out.println("🔧 Testing .env DB connection...");

        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD)) {
            System.out.println("✅ DB 연결 성공!");
        } catch (SQLException e) {
            System.out.println("❌ DB 연결 실패:");
            e.printStackTrace();
        }

        // 환경변수 값도 확인
        System.out.println("URL: " + DBConfig.URL);
        System.out.println("USER: " + DBConfig.USER);
    }
}
