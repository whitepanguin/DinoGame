package dto;

public class DinoDTO {
    public int id;
    public String name;
//    public int power;
    public int hp;
    public int maxHp;
    public int skillCount;
    public int maxSkillCount;

    public DinoDTO(int id, String name, int hp, int maxHp, int skillCount, int maxSkillCount) {
        this.id = id;
        this.name = name;
//        public int power;
        this.hp = hp;
        this.maxHp = maxHp;
        this.skillCount = skillCount;
        this.maxSkillCount = maxSkillCount;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " | 체력: " + hp + "/" + maxHp + " | 스킬: " + skillCount + "/" + maxSkillCount;
    }
}
