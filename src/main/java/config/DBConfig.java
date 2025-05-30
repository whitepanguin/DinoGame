package config;

import io.github.cdimascio.dotenv.Dotenv;

public class DBConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String URL = dotenv.get("DB_URL");
    public static final String USER = dotenv.get("DB_USER");
    public static final String PASSWORD = dotenv.get("DB_PASSWORD");
}

