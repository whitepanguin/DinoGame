package service;

import domain.Dino;
import domain.Item;

public class BattleService {
    public void normalAttack(Dino attacker, Dino defender) {
        attacker.attack(defender);
    }

    public void useSkill(Dino attacker, Dino defender) {
        attacker.useSkill(defender);
    }

    public void useItem(Dino dino, Item item) {
        dino.applyItem(item);
    }
}