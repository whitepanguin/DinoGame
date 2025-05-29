package domain;

public abstract class Dino {
    public final int id;
    public final String name;
    public final int tear;
    public final int power;
    public final int maxHp;
    public int hp;
    public final int maxSkillCount;
    public int skillCount;
    public final String type;
    public final int price;

    public Dino(int id, String name, int tear, int power, int hp, int skillCount, String type, int price) {
        this.id = id;
        this.name = name;
        this.tear = tear;
        this.power = power;
        this.maxHp = hp;
        this.hp = maxHp;
        this.maxSkillCount = skillCount;
        this.skillCount = maxSkillCount;
        this.type = type;
        this.price = price;
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
    @Override // 새로 추가
    public String toString() {
        return "[" + id + "] " + name + " 티어 " + tear + " 공격력: " + power;
    }
}
