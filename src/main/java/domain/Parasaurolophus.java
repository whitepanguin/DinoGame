package domain;

import java.util.Random;

// 육 - 파라사우롤로푸스 / Parasaurolophus
public class Parasaurolophus extends Dino {
    public Parasaurolophus() {
        super("파라사우롤로푸스", 15, 150, 2);
    }

    @Override
    public void attack() {
        System.out.println(name + " 박치기");
    }

    @Override
    public int getAttackPower() {
        return power + new Random().nextInt(20); // 15~34
    }

    @Override
    public String getSkillName() {
        return "머리 강타";
    }

    @Override
    public void useSkill(Dino enemy) {
        if (canUseSkill()) {
            skillCount--;
            int damage = (power * 2) + new Random().nextInt(20); // 30~49
            System.out.println("머리 강타! (일반 데미지 " + damage + ")");
            enemy.takeDamage(damage);
        } else {
            System.out.println("더 이상 스킬을 사용할 수 없습니다!");
        }
    }
}
