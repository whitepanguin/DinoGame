package test;

import domain.Item;
import repository.ItemRepository;

public class ItemTest {
    public static void main(String[] args) {
        ItemRepository repo = new ItemRepository();

        // 저장 테스트
        Item healItem = new Item("체력 회복 아이템", "heal", 500, "체력 20% 회복", 20.0);
        repo.save(healItem);

        // 조회 테스트
        for (Item item : repo.findAll()) {
            System.out.printf("[%s] %s - %s (%d원)\n", item.type, item.name, item.effectDescription, item.price);
        }
    }
}

