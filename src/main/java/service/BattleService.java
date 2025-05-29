package service;

import domain.Dino;

public class BattleService {
    public void normalAttack(Dino attacker, Dino defender) {
        attacker.attack();
        int damage = attacker.getAttackPower();
        defender.takeDamage(damage);
        System.out.println(attacker.name + "의 공격! " + damage + " 데미지!");
    }

    public void useSkill(Dino attacker, Dino defender) {
        attacker.useSkill(defender);
    }
}