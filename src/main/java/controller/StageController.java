package controller;

import java.util.*;
import domain.*;

public class StageController {
    private int stageNumber;
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();

    public StageController(int stageNumber) {
        this.stageNumber = stageNumber;
    }

    public boolean play(PlayerController player) {
        List<Dino> playerTeam = player.getMyDinosaurs();
        List<Dino> enemyTeam = generateEnemyTeam();

        int playerIndex = 0;
        int enemyIndex = 0;

        while (playerIndex < playerTeam.size() && enemyIndex < enemyTeam.size()) {
            Dino playerDino = playerTeam.get(playerIndex);
            Dino enemyDino = enemyTeam.get(enemyIndex);

//            SoundPlayer.play(playerDino.name);
//            SoundPlayer.play(enemyDino.name);

            System.out.println("\n⚔️ 전투 시작: " + playerDino.name + " vs " + enemyDino.name);
            battle(player, playerDino, enemyDino);

            if (!playerDino.isAlive()) playerIndex++;
            if (!enemyDino.isAlive()) enemyIndex++;
        }

        if (playerIndex < playerTeam.size()) {
            int reward = stageNumber * 100;
            player.addPoints(reward);
            System.out.println("\n🎉 스테이지 클리어! 포인트 +" + reward);

            System.out.println("상점에 들어가시겠습니까? [1] 예 / [2] 아니오");
            int choice = scanner.nextInt();
            if (choice == 1) {
//                Shop shop = new Shop();
//                shop.open(player);
            }

            System.out.println("▶️ 다음 스테이지로 진행하려면 엔터를 누르세요...");
            scanner.nextLine(); // consume newline
            scanner.nextLine(); // wait for enter
            return true;
        } else {
            System.out.println("\n💀 패배. 게임 종료!");
            return false;
        }
    }

    private void battle(PlayerController player, domain.Dino playerDino, domain.Dino enemyDino) {
        while (playerDino.isAlive() && enemyDino.isAlive()) {
            System.out.println("\n🧍‍♂️ 당신의 턴: [1] 공격 [2] 스킬 [3] 아이템 [4] 교체");
            int input = scanner.nextInt();

            switch (input) {
                case 1 -> {
                    playerDino.attack();
                    int damage = playerDino.getAttackPower();
                    enemyDino.takeDamage(damage);
                    System.out.println("💥 적에게 " + damage + " 데미지!");
                }
                case 2 -> {
                    if (playerDino.canUseSkill()) {
                        playerDino.useSkill(enemyDino);
                    } else {
                        System.out.println("⚠️ 스킬 사용 불가. 일반 공격 진행.");
                        int damage = playerDino.getAttackPower();
                        enemyDino.takeDamage(damage);
                    }
                }
                case 3 -> player.useItem(playerDino);
                case 4 -> {
                    System.out.println("🔄 교체할 공룡 번호 입력:");
                    for (int i = 0; i < player.getMyDinosaurs().size(); i++) {
                        domain.Dino d = player.getMyDinosaurs().get(i);
                        if (d.isAlive()) {
                            System.out.println("[" + i + "] " + d.name + " (HP: " + d.hp + ")");
                        }
                    }
                    int swap = scanner.nextInt();
                    if (player.switchDino(swap)) {
                        System.out.println("✅ 공룡 교체 완료!");
                        return;
                    } else {
                        System.out.println("❌ 교체 실패. 다시 시도하세요.");
                    }
                }
                default -> System.out.println("❌ 잘못된 입력입니다.");
            }

            if (!enemyDino.isAlive()) break;

            System.out.println("\n🤖 적의 턴:");
            boolean useSkill = random.nextBoolean();
            if (useSkill && enemyDino.canUseSkill()) {
                enemyDino.useSkill(playerDino);
            } else {
                enemyDino.attack();
                int damage = enemyDino.getAttackPower();
                playerDino.takeDamage(damage);
                System.out.println("🤖 적의 공격! " + damage + " 데미지!");
            }
        }
    }

//    protected List<domain.Dino> generateEnemyTeam() {
//        if (stageNumber == 1) {
//            return DBLoader.getDinosByTier(5, 3);
//        } else if (stageNumber == 2) {
//            return DBLoader.getDinosByTypeAndTier("육", 4, 5, 3);
//        } else if (stageNumber == 3) {
//            return DBLoader.getDinosByTypeAndTier("해", 3, 4, 3);
//        } else if (stageNumber == 4) {
//            return DBLoader.getDinosByTypeAndTier("공", 2, 3, 3);
//        } else if (stageNumber == 5) {
//            List<domain.Dino> team = new ArrayList<>();
//            team.add(DBLoader.getRandomDinoByTypeAndTier("육", 1, 1));
//            team.add(DBLoader.getRandomDinoByTypeAndTier("해", 1, 1));
//            team.add(DBLoader.getRandomDinoByTypeAndTier("공", 1, 1));
//
//        } else {
//            throw new IllegalArgumentException("잘못된 스테이지 번호");
//        }
//        return team;
//    }if (stageNumber == 1) {
//            return DBLoader.getDinosByTier(5, 3);
//        } else if (stageNumber == 2) {
//            return DBLoader.getDinosByTypeAndTier("육", 4, 5, 3);
//        } else if (stageNumber == 3) {
//            return DBLoader.getDinosByTypeAndTier("해", 3, 4, 3);
//        } else if (stageNumber == 4) {
//            return DBLoader.getDinosByTypeAndTier("공", 2, 3, 3);
//        } else if (stageNumber == 5) {
//            List<domain.Dino> team = new ArrayList<>();
//            team.add(DBLoader.getRandomDinoByTypeAndTier("육", 1, 1));
//            team.add(DBLoader.getRandomDinoByTypeAndTier("해", 1, 1));
//            team.add(DBLoader.getRandomDinoByTypeAndTier("공", 1, 1));
//
//        } else {
//            throw new IllegalArgumentException("잘못된 스테이지 번호");
//        }
//        return team;
//    }
protected List<Dino> generateEnemyTeam() {
    List<Dino> team = new ArrayList<>();
    Random rand = new Random();

    switch (stageNumber) {
        case 1 -> {
            // 5티어 공룡 (연습용: Parasaurolophus)
            for (int i = 0; i < 3; i++) {
                team.add(new Parasaurolophus()); // 기본 공룡
            }
        }
        case 2 -> {
            // 육 공룡 (Parasaurolophus)
            for (int i = 0; i < 3; i++) {
                team.add(new Parasaurolophus());
            }
        }
        case 3 -> {
            // 해 공룡 (Ichthyosaurus)
            for (int i = 0; i < 3; i++) {
                team.add(new Ichthyosaurus());
            }
        }
        case 4 -> {
            // 공 공룡 (Dimorphodon)
            for (int i = 0; i < 3; i++) {
                team.add(new Dimorphodon());
            }
        }
        case 5 -> {
            // 육/해/공 각각 한 마리
            team.add(new Parasaurolophus());
            team.add(new Ichthyosaurus());
            team.add(new Dimorphodon());
        }
        default -> throw new IllegalArgumentException("잘못된 스테이지 번호");
    }

    return team;
}

}
