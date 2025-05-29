package domain;


public abstract class Dino {
    static int nextId = 1;
    public final int id;
    public final String name;
    public final int maxHp;
    public int hp;
    public final int power;
    public final int maxSkillCount;
    public int skillCount;

    public Dino(String name, int power, int maxHp, int maxSkillCount) {
        this.id = nextId++;
        this.name = name;
        this.power = power;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxSkillCount = maxSkillCount;
        this.skillCount = maxSkillCount;
    }

    public abstract void attack();
    public abstract int getAttackPower();
    public abstract String getSkillName();
    public abstract void useSkill(Dino enemy);

    public boolean isAlive() {
        return hp > 0;
    }
    public boolean canUseSkill() {
        return skillCount > 0;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }
    public void printStatus() {
        System.out.println(name + " 체력: " + hp + "/" + maxHp + " | 스킬: " + skillCount + "/" + maxSkillCount);
    }

}
