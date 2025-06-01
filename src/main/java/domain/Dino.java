package domain;

import sound.FlyingRoar;
import sound.Roar;
import sound.SeaRoar;

import java.time.LocalDateTime;

public class Dino {
    public int id;
    public String name;
    public int tear;
    public int power;
    public int hp;
    public int maxHp;
    public int skillCount;
    public int maxSkillCount;
    public String type;
    public int price;
    public LocalDateTime createdAt;

    public Dino(int id, String name, int tear, int power, int hp, int skillCount, String type, int price, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.tear = tear;
        this.power = power;
        this.hp = hp;
        this.skillCount = skillCount;
        this.maxHp = hp;
        this.maxSkillCount = skillCount;
        this.type = type;
        this.price = price;
        this.createdAt = createdAt;
    }

    public Dino(String name, int tear, int power, int hp, int skillCount, String type, int price) {
        this(0, name, tear, power, hp, skillCount, type, price, LocalDateTime.now());
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void printStatus() {
        System.out.println("[" + name + "] HP: " + hp + "/" + maxHp + " | 스킬: " + skillCount + "/" + maxSkillCount);
    }

    public boolean canUseSkill() {
        return skillCount > 0;
    }

    public void useSkill(Dino target) {
        if (!canUseSkill()) {
            System.out.println(this.name + "은(는) 스킬을 사용할 수 없습니다!");
            return;
        }
        int skillDamage = (int)(this.power * 1.5);
        System.out.println(this.name + "이(가) 스킬을 사용합니다! (" + skillDamage + " 데미지)");
        target.hp = Math.max(0, target.hp - skillDamage);
        skillCount--;
        System.out.println("남은 스킬 횟수: " + skillCount + "/" + maxSkillCount);

    }

    public int getAttackPower() {
        return this.power;
    }
    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage);
    }
    public void attack(Dino target) {
        int damage = getAttackPower();
        target.takeDamage(damage);
        System.out.println(this.name + "의 공격! " + damage + " 데미지!");
    }
    public String getType() {
        return this.type;
    }
    // Dino.java 내부에 아래 메서드 추가
    public String getName() {
        return this.name;
    }
    public int getHp() {
        return this.hp;
    }


    public void playSound() {
        switch (this.type) {
            case "육" -> Roar.play();
            case "해" -> SeaRoar.play();
            case "공" -> FlyingRoar.play();
            default -> System.out.println("[사운드 없음] 알 수 없는 타입: " + type);
        }
    }

    public void applyItem(Item item) {
        switch (item.type) {
            case "heal" -> {
                int healAmount = (int) (maxHp * (item.effectValue / 100));
                this.hp = Math.min(this.maxHp, this.hp + healAmount);
                System.out.println("✨ 체력 회복! " + healAmount + "만큼 회복되었습니다.");
            }
            case "attack" -> {
                int extraPower = (int) (this.power * (item.effectValue / 100));
                this.power += extraPower;
                System.out.println("💥 공격력 증가! 현재 공격력: " + this.power);
            }
            case "random" -> {
                double rand = Math.random();
                if (rand < item.effectValue) {
                    this.hp = this.maxHp;
                    System.out.println("🎯 대성공! 체력이 전부 회복되었습니다!");
                } else {
                    int damage = this.maxHp / 2;
                    this.takeDamage(damage);
                    System.out.println("💣 실패! 절반의 피해를 입었습니다. (-" + damage + ")");
                }
            }
            default -> System.out.println("❌ 알 수 없는 아이템 타입입니다.");
        }
    }


}
