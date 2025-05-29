package domain;

import java.util.Random;

// 해 - 이크티오사우루스 / Ichthyosaurus
public class Ichthyosaurus extends Dino {
    public Ichthyosaurus() {
        super("이크티오사우루스", 10, 185, 2);
    }

    @Override
    public void attack() {
        System.out.println(name + " 물어 뜯기!!");
    }

    @Override
    public int getAttackPower() {
        return power + new Random().nextInt(20); // 10~29
    }

    @Override
    public String getSkillName() {
        return "마구 물어 뜯기";
    }

    @Override
    public void useSkill(Dino enemy) {
        if (canUseSkill()) {
            skillCount--;
            int damage = (power * 2) + new Random().nextInt(20); // 20~39
            System.out.println("마구 물어 뜯기! (일반 데미지 " + damage + ")");
            enemy.takeDamage(damage);
        } else {
            System.out.println("더 이상 스킬을 사용할 수 없습니다!");
        }
    }
}
