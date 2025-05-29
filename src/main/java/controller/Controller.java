package controller;

import dto.DinoDTO;
import service.*;
import domain.*;
import Ui.Battleimage;
import java.util.*;

public class Controller {
    private final Service dinoService = new Service();
    private final BattleService battleService = new BattleService();
    private final Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("🦕 스테이지 시작!");
            playStage();

            System.out.print("➡️ 다음 스테이지로 진행할까요? (1: 예, 2: 아니오): ");
            int next = sc.nextInt();
            if (next != 1) {
                System.out.println("👋 게임을 종료합니다.");
                break;
            }
        }
    }

    private void playStage() {
        Dino[] playerTeam = choosePlayerDinos();
        Dino[] enemyTeam = generateEnemyTeam();

        int playerIdx = 0;
        int enemyIdx = 0;

        // ✅ 전투 이미지 첫 출력
        Battleimage.showBattle(playerTeam[playerIdx], enemyTeam[enemyIdx]);

        while (true) {
            Dino player = playerTeam[playerIdx];
            Dino enemy = enemyTeam[enemyIdx];

            printStatus(player, enemy);
            System.out.println("\n[당신의 턴]");
            System.out.println("1. 일반 공격");
            System.out.println("2. 스킬 사용");
            System.out.println("3. 공룡 교체");
            System.out.print("선택 >> ");
            int action = sc.nextInt();

            if (action == 1) {
                battleService.normalAttack(player, enemy);
            } else if (action == 2) {
                battleService.useSkill(player, enemy);
            } else if (action == 3) {
                playerIdx = chooseAnotherDino(playerTeam);
                // ✅ 플레이어 공룡 교체 시 이미지 갱신
                Battleimage.updateBattle(playerTeam[playerIdx], enemyTeam[enemyIdx]);
                continue;
            }

            if (!enemy.isAlive()) {
                System.out.println("✅ 적 " + enemy.name + " 쓰러짐!");
                enemyIdx++;
                if (enemyIdx >= 3) {
                    System.out.println("🎉 당신이 이겼습니다!");
                    // ✅ 전투 이미지 닫기
                    Battleimage.closeBattle();
                    return;
                } else {
                    // ✅ 적 공룡이 바뀌었을 때 이미지 갱신
                    Battleimage.updateBattle(playerTeam[playerIdx], enemyTeam[enemyIdx]);
                }
            }

            Dino currEnemy = enemyTeam[enemyIdx];
            Dino currPlayer = playerTeam[playerIdx];
            System.out.println("\n[👾 적의 턴]");
            handleEnemyTurn(currEnemy, currPlayer);

            if (!currPlayer.isAlive()) {
                System.out.println("☠️ 당신의 " + currPlayer.name + "이 쓰러졌습니다!");
                if (Arrays.stream(playerTeam).noneMatch(Dino::isAlive)) {
                    System.out.println("💀 모든 공룡이 쓰러졌습니다. 패배...");
                    // ✅ 전투 이미지 닫기
                    Battleimage.closeBattle();
                    return;
                } else {
                    playerIdx = chooseAnotherDino(playerTeam);
                    // ✅ 플레이어 공룡 쓰러졌을 때 교체 후 이미지 갱신
                    Battleimage.updateBattle(playerTeam[playerIdx], currEnemy);
                }
            }
        }
    }

    private void handleEnemyTurn(Dino enemy, Dino player) {
        if (enemy.hp < 60 && enemy.canUseSkill() && new Random().nextInt(100) < 60) {
            enemy.useSkill(player);
        } else {
            battleService.normalAttack(enemy, player);
        }
    }

    private Dino[] choosePlayerDinos() {
        List<DinoDTO> all = dinoService.getAllDinoDTOs();
        Dino[] selected = new Dino[3];

        for (int i = 0; i < 3; i++) {
            System.out.println("공룡 선택 (" + (i + 1) + "/3)");
            all.forEach(System.out::println);
            System.out.print("공룡 ID 선택 >> ");
            int id = sc.nextInt();
            selected[i] = dinoService.getDinoById(id);
        }
        return selected;
    }

    private int chooseAnotherDino(Dino[] team) {
        System.out.println("교체할 공룡을 선택하세요:");
        for (int i = 0; i < team.length; i++) {
            if (team[i].isAlive()) {
                System.out.println(i + 1 + ". " + team[i].name + " (HP: " + team[i].hp + ")");
            }
        }
        int choice;
        while (true) {
            System.out.print("번호 입력 >> ");
            choice = sc.nextInt() - 1;
            if (choice >= 0 && choice < team.length && team[choice].isAlive()) break;
            System.out.println("올바른 번호를 입력하세요.");
        }
        return choice;
    }

    private Dino[] generateEnemyTeam() {
        Dino[] all = { new Parasaurolophus(), new Ichthyosaurus(), new Dimorphodon() };
        Dino[] team = new Dino[3];
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            team[i] = copyDino(all[rand.nextInt(all.length)]);
        }
        return team;
    }

    private Dino copyDino(Dino d) {
        if (d instanceof Parasaurolophus) return new Parasaurolophus();
        if (d instanceof Ichthyosaurus) return new Ichthyosaurus();
        return new Dimorphodon();
    }

    private void printStatus(Dino d1, Dino d2) {
        System.out.println("\n🦖 현재 전투 상황");
        d1.printStatus();
        d2.printStatus();
    }
}
