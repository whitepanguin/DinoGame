package domain;

import java.time.LocalDateTime;

public class User {
    public int id;
    public int points;
    public String itemIds;      // 예: "1,3,5"
    public String dinoIds;      // 예: "2,4"
    public int maxStage;
    public int currentStage;
    public LocalDateTime createdAt;

    public User(int id, int points, String itemIds, String dinoIds, int maxStage, int currentStage, LocalDateTime createdAt) {
        this.id = id;
        this.points = points;
        this.itemIds = itemIds;
        this.dinoIds = dinoIds;
        this.maxStage = maxStage;
        this.currentStage = currentStage;
        this.createdAt = createdAt;
    }

    public User() {
        this.points = 0;
        this.itemIds = "";
        this.dinoIds = "";
        this.maxStage = 1;
        this.currentStage = 1;
        this.createdAt = LocalDateTime.now();
    }
}
