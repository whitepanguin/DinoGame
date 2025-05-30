package service;

import domain.Dino;

public class BattleService {
    public void normalAttack(Dino attacker, Dino defender) {
        attacker.attack(defender);
    }


    public void useSkill(Dino attacker, Dino defender) {
        attacker.useSkill(defender);
    }
}