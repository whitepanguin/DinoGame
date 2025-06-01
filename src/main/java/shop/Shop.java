package shop;

import domain.*;
import repository.*;

import java.util.List;
import java.util.Scanner;

public class Shop {
    private final Scanner scanner = new Scanner(System.in);
    private final ItemRepository itemRepo = new ItemRepository();
    private final DinoRepository dinoRepo = new DinoRepository();
    private final UserRepository userRepo = new UserRepository();

    public boolean open(User user) {
        System.out.println("\n🏪 [상점이 열렸습니다]");
        System.out.println("현재 포인트: " + user.points);
        System.out.println("현재 스테이지: " + user.currentStage);
        System.out.println("보유 아이템: " + user.itemIds);
        System.out.println("보유 공룡: " + user.dinoIds);

        System.out.println("\n[1] 아이템 구매\n[2] 공룡 구매\n[0] 나가기");
        System.out.print("선택: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> buyItem(user);
            case 2 -> buyDino(user);
            case 0 -> {
                System.out.println("상점을 나갑니다.");
                return false;  // 나가기 선택 시 반복 종료
            }
            default -> System.out.println("❌ 잘못된 입력입니다.");
        }

        return true; // 아이템 또는 공룡 구매 후 계속 상점 유지
    }


    private void buyItem(User user) {
        List<Item> items = itemRepo.findAll();
        System.out.println("\n🎁 구매 가능한 아이템 목록:");
        for (Item item : items) {
            System.out.println(item.id + ". " + item.name + " - " + item.price + "P");
        }

        System.out.print("아이템 ID 입력: ");
        int id = scanner.nextInt();
        Item selected = items.stream().filter(i -> i.id == id).findFirst().orElse(null);

        if (selected == null || user.points < selected.price) {
            System.out.println("❌ 구매 실패: 존재하지 않거나 포인트 부족");
            return;
        }

        // 아이템 구매 처리
        user.points -= selected.price;
        user.itemIds = (user.itemIds == null || user.itemIds.isBlank()) ? String.valueOf(id) : user.itemIds + "," + id;
        userRepo.update(user);
        System.out.println("✅ 구매 완료: " + selected.name);
    }

    private void buyDino(User user) {
        List<Dino> dinos = dinoRepo.findAll();
        List<Integer> ownedIds = parseIds(user.dinoIds);

        System.out.println("\n🦖 구매 가능한 공룡 목록:");
        for (Dino d : dinos) {
            if (!ownedIds.contains(d.id)) {
                System.out.println(d.id + ". " + d.name + " - " + d.price + "P");
            }
        }

        System.out.print("공룡 ID 입력: ");
        int id = scanner.nextInt();
        Dino selected = dinos.stream().filter(d -> d.id == id).findFirst().orElse(null);

        if (selected == null || user.points < selected.price || ownedIds.contains(id)) {
            System.out.println("❌ 구매 실패: 조건 불충족");
            return;
        }

        user.points -= selected.price;
        user.dinoIds = user.dinoIds.isEmpty() ? String.valueOf(id) : user.dinoIds + "," + id;
        userRepo.update(user);
        System.out.println("✅ 공룡 구매 완료: " + selected.name);
    }

    private List<Integer> parseIds(String csv) {
        List<Integer> ids = new java.util.ArrayList<>();
        if (csv == null || csv.isEmpty()) return ids;
        for (String s : csv.split(",")) ids.add(Integer.parseInt(s.trim()));
        return ids;
    }
}
