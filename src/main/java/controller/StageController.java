package controller;

import Ui.Battleimage;
import domain.Dino;
import repository.DinoRepository;
import sound.FlyingRoar;
import sound.Roar;
import sound.SeaRoar;

import java.net.URL;
import java.util.*;

public class StageController {
    private final DinoRepository dinoRepo = new DinoRepository();
    private final Scanner sc = new Scanner(System.in);

    public void runStage(int stageNumber, List<Dino> playerDinos) {
        System.out.println("\n🚩 스테이지 " + stageNumber + " 시작!");

        List<Dino> enemies = generateEnemiesByStage(stageNumber);
        List<Dino> selected = selectPlayerDinos(playerDinos);

        int playerIdx = 0;
        int enemyIdx = 0;

        String bgPath = getBackgroundPath(stageNumber);
        Battleimage.showBattle(selected.get(playerIdx), enemies.get(enemyIdx), bgPath);
        playSound(selected.get(playerIdx).getType());

        while (true) {
            Dino player = selected.get(playerIdx);
            Dino enemy = enemies.get(enemyIdx);

            printStatus(player, enemy);
            System.out.println("\n[당신의 턴]");
            System.out.println("1. 일반 공격");
            System.out.println("2. 스킬 사용");
            System.out.println("3. 아이템 사용");
            System.out.println("4. 공룡 교체");
            System.out.print("선택 >> ");
            int action = sc.nextInt();

            switch (action) {
                case 1 -> {
                    player.attack(enemy);
                    player.playSound();  // 🦖 일반 공격 시 사운드 출력
                }
                case 2 -> {
                    player.useSkill(enemy);
                    player.playSound();  // 🦖 스킬 사용 시 사운드 출력
                }
                case 3 -> {
                    System.out.println("⚠️ 아이템 사용 기능은 구현 예정입니다.");
                }
                case 4 -> {
                    playerIdx = chooseAnotherDino(selected);
                    Battleimage.updateBattle(selected.get(playerIdx), enemy);
                    selected.get(playerIdx).playSound();  // 교체 후 사운드 출력
                    continue;
                }
                default -> System.out.println("❌ 잘못된 입력입니다.");
            }

            if (!enemy.isAlive()) {
                System.out.println("✅ 적 " + enemy.getName() + " 쓰러짐!");
                enemyIdx++;
                if (enemyIdx >= enemies.size()) {
                    System.out.println("🎉 당신이 이겼습니다!");
                    Battleimage.closeBattle();
                    return;
                }
                Battleimage.updateBattle(selected.get(playerIdx), enemies.get(enemyIdx));
                playSound(enemies.get(enemyIdx).getType());
            }

            // 적의 턴
            Dino currEnemy = enemies.get(enemyIdx);
            Dino currPlayer = selected.get(playerIdx);
            System.out.println("\n[👾 적의 턴]");
            currEnemy.attack(currPlayer);  // 👉 적의 공격도 attack() 사용
            currEnemy.playSound();         // 👉 적 공격 시 사운드 출력

            if (!currPlayer.isAlive()) {
                System.out.println("☠️ 당신의 " + currPlayer.getName() + "이 쓰러졌습니다!");
                if (selected.stream().noneMatch(Dino::isAlive)) {
                    System.out.println("💀 모든 공룡이 쓰러졌습니다. 패배...");
                    Battleimage.closeBattle();
                    return;
                } else {
                    playerIdx = chooseAnotherDino(selected);
                    Battleimage.updateBattle(selected.get(playerIdx), currEnemy);
                    playSound(selected.get(playerIdx).getType());
                }
            }
        }
    }

    private List<Dino> selectPlayerDinos(List<Dino> all) {
        List<Dino> selected = new ArrayList<>();
        System.out.println("보유한 공룡 리스트:");
        for (int i = 0; i < all.size(); i++) {
            Dino d = all.get(i);
            System.out.println("[" + i + "] " + d.getName() + " (HP: " + d.getHp() + ")");
        }

        while (selected.size() < 3) {
            System.out.print("선택할 공룡 번호: ");
            int idx = sc.nextInt();
            if (idx >= 0 && idx < all.size() && !selected.contains(all.get(idx))) {
                selected.add(all.get(idx));
            } else {
                System.out.println("⚠️ 이미 선택되었거나 잘못된 번호입니다.");
            }
        }
        return selected;
    }

    private int chooseAnotherDino(List<Dino> team) {
        System.out.println("교체할 공룡 번호 입력:");
        for (int i = 0; i < team.size(); i++) {
            Dino d = team.get(i);
            if (d.isAlive()) {
                System.out.println("[" + i + "] " + d.getName() + " (HP: " + d.getHp() + ")");
            }
        }
        int idx = sc.nextInt();
        if (idx >= 0 && idx < team.size() && team.get(idx).isAlive()) {
            return idx;
        }
        System.out.println("❌ 교체 실패. 다시 시도하세요.");
        return chooseAnotherDino(team);
    }

    private List<Dino> generateEnemiesByStage(int stageNumber) {
        return switch (stageNumber) {
            case 1 -> dinoRepo.findRandomDinosByTear(5, 3);
            case 2 -> dinoRepo.findRandomDinosByTypeAndTear("육", 4, 5, 3);
            case 3 -> dinoRepo.findRandomDinosByTypeAndTear("해", 3, 4, 3);
            case 4 -> dinoRepo.findRandomDinosByTypeAndTear("공", 2, 3, 3);
            case 5 -> {
                List<Dino> result = new ArrayList<>();
                result.addAll(dinoRepo.findRandomDinosByTypeAndTear("육", 1, 1, 1));
                result.addAll(dinoRepo.findRandomDinosByTypeAndTear("해", 1, 1, 1));
                result.addAll(dinoRepo.findRandomDinosByTypeAndTear("공", 1, 1, 1));
                yield result;
            }
            default -> {
                System.out.println("⚠️ 존재하지 않는 스테이지입니다.");
                yield new ArrayList<>();
            }
        };
    }

    private void printStatus(Dino player, Dino enemy) {
        System.out.println("\n=== 현재 상태 ===");
        System.out.println("🧡 내 공룡: " + player.getName() + " (HP: " + player.getHp() + ")");
        System.out.println("💢 적 공룡: " + enemy.getName() + " (HP: " + enemy.getHp() + ")");
    }

    private void playSound(String type) {
        switch (type) {
            case "육" -> Roar.play();
            case "해" -> SeaRoar.play();
            case "공" -> FlyingRoar.play();
            default -> System.out.println("[사운드 없음] 알 수 없는 타입: " + type);
        }
    }

    private String getBackgroundPath(int stageNumber) {
        String filename = switch (stageNumber) {
            case 1 -> "/image/bg.png";
            case 2 -> "/image/bg_land.png";
            case 3 -> "/image/bg_sea.png";
            case 4 -> "/image/bg_air.png";
            case 5 -> "/image/bg_final.png";
            default -> "/image/bg.png";
        };

        URL resource = getClass().getResource(filename);
        if (resource == null) {
            System.out.println("❌ 배경 이미지 파일을 찾을 수 없습니다: " + filename);
            return "";
        }
        return resource.toExternalForm();
    }
}
