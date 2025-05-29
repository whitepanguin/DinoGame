package domain;

import java.util.Random;

public class Dimorphodon extends Dino {
    public Dimorphodon(int id) {
        super(id, "디모르포돈", 5, 10, 150, 3, "공", 4000);
    }

    @Override
    public void attack() {
        System.out.println(name + " 돌 던지기!!");
    }

    @Override
    public int getAttackPower() {
        return power + new Random().nextInt(20);
    }

    @Override
    public String getSkillName() {
        return "바위 던지기";
    }

    @Override
    public void useSkill(Dino enemy) {
        if (canUseSkill()) {
            skillCount--;
            int damage = (power * 2) + new Random().nextInt(20);
            System.out.println("바위 던지기! (일반 데미지 " + damage + ")");
            enemy.takeDamage(damage);
        } else {
            System.out.println("더 이상 스킬을 사용할 수 없습니다!");
        }
    }
}
