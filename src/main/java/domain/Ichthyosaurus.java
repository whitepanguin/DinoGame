package domain;

import java.util.Random;

public class Ichthyosaurus extends Dino {
    public Ichthyosaurus(int id) {
        super(id, "이크티오사우루스", 5, 10, 185, 2, "해", 4500);
    }

    @Override
    public void attack() {
        System.out.println(name + " 물어 뜯기!!");
    }

    @Override
    public int getAttackPower() {
        return power + new Random().nextInt(20);
    }

    @Override
    public String getSkillName() {
        return "마구 물어 뜯기";
    }

    @Override
    public void useSkill(Dino enemy) {
        if (canUseSkill()) {
            skillCount--;
            int damage = (power * 2) + new Random().nextInt(20);
            System.out.println("마구 물어 뜯기! (일반 데미지 " + damage + ")");
            enemy.takeDamage(damage);
        } else {
            System.out.println("더 이상 스킬을 사용할 수 없습니다!");
        }
    }
}
