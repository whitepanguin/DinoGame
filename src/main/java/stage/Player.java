
import java.util.*;

public class Player {
    private final List<Dano> myDinosaurs = new ArrayList<>();
    private final List<Item> myItems = new ArrayList<>();
    private int points;

    public Player() {
        this.points = 0;
    }

    public List<Dano> getMyDinosaurs() {
        return myDinosaurs;
    }

    public List<Item> getMyItems() {
        return myItems;
    }

    public void addDinosaur(Dano dino) {
        myDinosaurs.add(dino);
        System.out.println("🦖 새로운 공룡이 추가되었습니다: " + dino.name);
    }

    public void addItem(Item item) {
        myItems.add(item);
        System.out.println("🎁 아이템을 획득했습니다: " + item.getName());
    }

    public void addPoints(int amount) {
        points += amount;
        System.out.println("💰 포인트가 추가되었습니다. 현재 포인트: " + points);
    }

    public boolean subtractPoints(int amount) {
        if (points >= amount) {
            points -= amount;
            return true;
        } else {
            System.out.println("❌ 포인트가 부족합니다. 현재 포인트: " + points);
            return false;
        }
    }

    public void useItem(Dano targetDino) {
        if (myItems.isEmpty()) {
            System.out.println("❌ 사용 가능한 아이템이 없습니다.");
            return;
        }

        System.out.println("🧪 사용 가능한 아이템:");
        for (int i = 0; i < myItems.size(); i++) {
            Item item = myItems.get(i);
            System.out.println("[" + i + "] " + item.getName());
        }

        System.out.print("번호를 선택하세요: ");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();

        if (index >= 0 && index < myItems.size()) {
            Item item = myItems.remove(index);
            item.applyTo(targetDino);
        } else {
            System.out.println("❌ 잘못된 번호입니다.");
        }
    }

    public boolean switchDino(int index) {
        if (index >= 0 && index < myDinosaurs.size() && myDinosaurs.get(index).isAlive()) {
            Collections.swap(myDinosaurs, 0, index);
            return true;
        }
        return false;
    }

    public int getPoints() {
        return points;
    }
}
