package domain;

import java.time.LocalDateTime;

public class Item {
    public int id;
    public String name;
    public String type;
    public int price;
    public String effectDescription;
    public double effectValue;
    public LocalDateTime createdAt;

    public Item(int id, String name, String type, int price, String effectDescription, double effectValue, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.effectDescription = effectDescription;
        this.effectValue = effectValue;
        this.createdAt = createdAt;
    }

    public Item(String name, String type, int price, String effectDescription, double effectValue) {
        this(0, name, type, price, effectDescription, effectValue, LocalDateTime.now());
    }
}
