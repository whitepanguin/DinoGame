package domain;

import java.util.Random;

public class DinoData extends Dino {
    public DinoData(int id, String name, int tear, int power, int hp, int skillCount, String type, int price) {
        super(id, name, tear, power, hp, skillCount, type, price);
    }

    @Override
    public void attack() {
        System.out.println(name + "이(가) 일반 공격!");
    }

    @Override
    public int getAttackPower() {
        return power + new Random().nextInt(10); // 랜덤 보정
    }

    @Override
    public String getSkillName() {
        return "기본 스킬";
    }

    @Override
    public void useSkill(Dino enemy) {
        if (canUseSkill()) {
            int damage = power * 2 + new Random().nextInt(20);
            System.out.println(name + "이(가) " + getSkillName() + "을(를) 사용! → 데미지: " + damage);
            enemy.takeDamage(damage);
            skillCount--;
        } else {
            System.out.println("스킬 사용 불가! (잔여 횟수 없음)");
        }
    }
}
