-- 테이블 생성
CREATE DATABASE IF NOT EXISTS testdbconn CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE testdbconn;

-- 아이템 테이블
CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price INT NOT NULL,
    effect_description TEXT,
    effect_value DECIMAL(5,2),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );

-- 유저 테이블
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    points INT DEFAULT 0,
    max_stage INT DEFAULT 1,
    current_stage INT DEFAULT 1,
    item_ids TEXT,     -- 아이템 ID 리스트 (예: "1,1,2")
    dino_ids TEXT,     -- 공룡 ID 리스트 (예: "3,5")
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 공룡 테이블
CREATE TABLE dinos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    tear INT NOT NULL,
    power INT NOT NULL,
    hp INT NOT NULL,
    skill_count INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    price INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

