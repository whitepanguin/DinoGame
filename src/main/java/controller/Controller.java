package controller;

import dto.DinoDTO;
import service.*;
import domain.*;
import Ui.Battleimage;
import java.util.*;
import shop.Shop;
import repository.*;

public class Controller {
    private final Service dinoService = new Service();
    private final BattleService battleService = new BattleService();
    private final Shop shop = new Shop();
    private final Scanner sc = new Scanner(System.in);
    private final UserRepository userRepo = new UserRepository();
    private User user;

    public void start() {
        System.out.print("🧑 유저 ID를 입력하세요 (숫자): ");
        int userId = sc.nextInt();

        user = userRepo.findById(userId);
        if (user == null) {
            System.out.println("🔐 새로운 유저를 생성합니다.");
            user = new User();
            user.id = userId;
            user.points = 1000;

            // 기본 공룡 3종 등록
            List<Integer> starterIds = createStarterDinos();
            user.dinoIds = starterIds.stream()
                    .map(String::valueOf)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");

            userRepo.save(user);
        } else {
            System.out.println("✅ 기존 유저 불러오기 완료.");
        }

        while (true) {
            System.out.println("\n🦕 스테이지 " + user.currentStage + " 시작!");
            playStage();

            // 전투 후 상점
            shop.open(user);

            // 유저 상태 갱신
            user.currentStage++;
            user.maxStage = Math.max(user.maxStage, user.currentStage);
            userRepo.update(user);

            System.out.print("➡️ 다음 스테이지로 진행할까요? (1: 예, 2: 아니오): ");
            int next = sc.nextInt();
            if (next != 1) {
                System.out.println("👋 게임을 종료합니다.");

                // 유저 정보 저장
                userRepo.update(user);

                // 종료 메시지 출력
                System.out.println("\n📦 게임 저장이 완료되었습니다.");
                System.out.println("🧑 유저 ID: " + user.id);
                System.out.println("💰 남은 포인트: " + user.points);
                System.out.println("📈 최종 스테이지: " + user.currentStage + " (최고 스테이지: " + user.maxStage + ")");
                System.out.println("🦕 보유 공룡: " + (user.dinoIds == null || user.dinoIds.isBlank() ? "없음" : user.dinoIds));
                System.out.println("🎁 보유 아이템: " + (user.itemIds == null || user.itemIds.isBlank() ? "없음" : user.itemIds));
            }
        }
    }

    private void playStage() {
        Dino[] playerTeam = choosePlayerDinos(); // ✅ DinoDTO → Dino
//        System.out.println("디버깅: user.dinoIds = " + user.dinoIds);

        if (playerTeam.length < 1) {
            System.out.println("⚠️ 플레이할 공룡이 없습니다.");
            return;
        }

        // ✅ 스테이지 기반 tear 반영
        Dino[] enemyTeam = generateEnemyTeam(user.currentStage);

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
                Battleimage.updateBattle(playerTeam[playerIdx], enemyTeam[enemyIdx]);
                continue;
            }

            if (!enemy.isAlive()) {
                System.out.println("✅ 적 " + enemy.name + " 쓰러짐!");
                enemyIdx++;
                if (enemyIdx >= enemyTeam.length) {
                    System.out.println("🎉 당신이 이겼습니다!");
                    Battleimage.closeBattle();
                    return;
                } else {
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
                    Battleimage.closeBattle();
                    return;
                } else {
                    playerIdx = chooseAnotherDino(playerTeam);
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
        List<Integer> ownedIds = parseIds(user.dinoIds);
        List<Dino> selectedDinos = new ArrayList<>();
        for (int id : ownedIds) {
            Dino d = dinoService.getDinoById(id);
            if (d != null) selectedDinos.add(d);
        }

        Dino[] selected = new Dino[Math.min(3, selectedDinos.size())];
        for (int i = 0; i < selected.length; i++) {
            System.out.println("공룡 선택 (" + (i + 1) + "/" + selected.length + ")");
            selectedDinos.forEach(d -> System.out.println("[" + d.id + "] " + d.name + " (HP: " + d.hp + ")"));
            System.out.print("공룡 ID 선택 >> ");
            int id = sc.nextInt();

            Dino chosen = selectedDinos.stream().filter(d -> d.id == id).findFirst().orElse(null);
            if (chosen == null) {
                System.out.println("❌ 잘못된 ID입니다. 다시 선택하세요.");
                i--;
            } else {
                selected[i] = chosen;
            }
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

    private List<Integer> createStarterDinos() {
        DinoRepository dinoRepo = new DinoRepository();
        List<Integer> starterIds = new ArrayList<>();

        // tear가 5인 공룡 중 랜덤으로 3마리 선택
        List<Dino> randomDinos = dinoRepo.findRandomDinosByTear(5, 3);

        for (Dino dino : randomDinos) {
            // 선택된 공룡 ID만 추출
            starterIds.add(dino.id);
        }
        return starterIds;
    }


    private Dino[] generateEnemyTeam(int stage) {
        DinoRepository dinoRepo = new DinoRepository();
        // tear 값 결정 로직
        int tear = switch (stage) {
            case 1 -> 5;
            case 2 -> 4;
            case 3 -> 3;
            case 4 -> 2;
            default -> 1;
        };
        // 해당 티어의 공룡 중 랜덤하게 3마리 선택
        List<Dino> randomDinos = dinoRepo.findRandomDinosByTear(tear, 3);
        // 배열로 변환
        return randomDinos.toArray(new Dino[0]);
    }

    private DinoDTO cloneDTO(DinoDTO dto) {
        return new DinoDTO(dto.id, dto.name, dto.hp, dto.maxHp, dto.skillCount, dto.maxSkillCount); // ✅ power 제외
    }

    private List<Integer> parseIds(String csv) {
        List<Integer> result = new ArrayList<>();
        if (csv == null || csv.isBlank()) return result;
        for (String s : csv.split(",")) {
            try {
                result.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private void printStatus(Dino d1, Dino d2) {
        System.out.println("\n🦖 현재 전투 상황");
        d1.printStatus();
        d2.printStatus();
    }



}
