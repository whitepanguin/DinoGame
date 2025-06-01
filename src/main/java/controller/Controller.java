package controller;

import dto.DinoDTO;
import service.*;
import domain.*;
import Ui.Battleimage;
import java.util.*;
import java.net.URL;
import shop.Shop;
import repository.*;
import sound.FlyingRoar;
import sound.Roar;
import sound.SeaRoar;

public class Controller {
    private final StageController stageController = new StageController();
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

            List<Integer> starterIds = createStarterDinos();
            user.dinoIds = starterIds.stream()
                    .map(String::valueOf)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");

            userRepo.save(user);
        } else {
            System.out.println("✅ 기존 유저 불러오기 완료.");
        }
        user.currentStage = 1;

        while (user.currentStage <= 5) {
            System.out.println("\n🦕 스테이지 " + user.currentStage + " 시작!");

            // 1. 플레이어 공룡 선택
            Dino[] playerTeam = choosePlayerDinos();
            if (playerTeam.length < 1) {
                System.out.println("⚠️ 플레이할 공룡이 없습니다.");
                break;
            }

            // 2. 적 공룡 생성 및 배경 준비
            Dino[] enemyTeam = generateEnemiesByStage(user.currentStage).toArray(new Dino[0]);
            String backgroundPath = getBackgroundPath(user.currentStage);



            // 3. 첫 공룡 인덱스 초기화
            int playerIdx = 0;
            int enemyIdx = 0;

            // ✅ 4. 전투 시작 화면 출력 (여기가 정확한 위치)
//            Battleimage.showBattle(playerTeam[playerIdx], enemyTeam[enemyIdx], backgroundPath);
            if (user.currentStage == 1) {
                Battleimage.showBattle(playerTeam[playerIdx], enemyTeam[enemyIdx], backgroundPath);
            } else {
                Battleimage.updateBackground(backgroundPath);
                Battleimage.updateBattle(playerTeam[playerIdx], enemyTeam[enemyIdx]);
            }
            // 5. 전투 루프
            boolean cleared = false;
            while (true) {
                Dino player = playerTeam[playerIdx];
                Dino enemy = enemyTeam[enemyIdx];

                printStatus(player, enemy);
                System.out.println("\n[당신의 턴]");
                System.out.println("1. 일반 공격");
                System.out.println("2. 스킬 사용");
                System.out.println("3. 아이템 사용");
                System.out.println("4. 공룡 교체");
                System.out.print("선택 >> ");
                int action = sc.nextInt();

                if (action == 1) {
                    battleService.normalAttack(player, enemy);
                } else if (action == 2) {
                    battleService.useSkill(player, enemy);
                } else if (action == 3) {
                    List<Item> items = userRepo.getItems(user.itemIds);
                    if (items.isEmpty()) {
                        System.out.println("⚠️ 사용 가능한 아이템이 없습니다.");
                    } else {
                        System.out.println("🧪 사용 가능한 아이템 목록:");
                        for (int i = 0; i < items.size(); i++) {
                            Item item = items.get(i);
                            System.out.println((i + 1) + ". " + item.name + " - " + item.effectDescription);
                        }
                        System.out.print("사용할 아이템 번호 (0 입력 시 취소) >> ");
                        int choice = sc.nextInt();
                        if (choice >= 1 && choice <= items.size()) {
                            Item selectedItem = items.get(choice - 1);
                            battleService.useItem(player, selectedItem);
                            userRepo.removeItem(user, selectedItem);
                        } else {
                            System.out.println("🚫 아이템 사용 취소됨");
                        }
                    }                } else if (action == 4) {
                    playerIdx = chooseAnotherDino(playerTeam);
                    Battleimage.updateBattle(playerTeam[playerIdx], enemyTeam[enemyIdx]);
                    continue;
                }

                // 적 쓰러짐 체크
                if (!enemy.isAlive()) {
                    System.out.println("✅ 적 " + enemy.name + " 쓰러짐!");
                    enemyIdx++;
                    if (enemyIdx >= enemyTeam.length) {
                        System.out.println("🎉 당신이 이겼습니다!");
                        cleared = true;
//                        Battleimage.closeBattle();
                        break;
                    } else {
                        Battleimage.updateBattle(playerTeam[playerIdx], enemyTeam[enemyIdx]);
                    }
                }

                // 적의 턴
                Dino currEnemy = enemyTeam[enemyIdx];
                Dino currPlayer = playerTeam[playerIdx];
                System.out.println("\n[👾 적의 턴]");
                handleEnemyTurn(currEnemy, currPlayer);

                // 플레이어 쓰러짐 체크
                if (!currPlayer.isAlive()) {
                    System.out.println("☠️ 당신의 " + currPlayer.name + "이 쓰러졌습니다!");
                    if (Arrays.stream(playerTeam).noneMatch(Dino::isAlive)) {
                        System.out.println("💀 모든 공룡이 쓰러졌습니다. 패배...");
//                        Battleimage.closeBattle();
                        cleared = false;
                        break;
                    } else {
                        playerIdx = chooseAnotherDino(playerTeam);
                        Battleimage.updateBattle(playerTeam[playerIdx], currEnemy);
                    }
                }
            }

            // 승리 후 처리
            if (cleared) {
                int reward = 200 * user.currentStage;
                user.points += reward;
                System.out.println("💰 스테이지 보상: " + reward + "포인트 지급! (총 보유: " + user.points + ")");

                System.out.print("🛍 상점에 들르시겠습니까? (1: 예, 2: 아니오): ");
                int goShop = sc.nextInt();
                if (goShop == 1) {
                    boolean inShop = true;
                    while (inShop) {
                        inShop = shop.open(user); // 상점 반복 입장
                    }
                }else {
                    System.out.print("➡️ 다음 스테이지로 진행할까요? (1: 예, 2: 아니오): ");
                    int goNext = sc.nextInt();
                    if(goNext == 1) {
                        continue;
                    }else if (goNext == 2){
                        break;
                    }else {
                        System.out.println("잘못 입력하셨습니다!");
                        break;
                    }
                }

                if (user.currentStage == 5) {
                    System.out.println("\n🎊 모든 스테이지를 클리어했습니다! 게임 종료");
                    break;
                }

                System.out.print("➡️ 다음 스테이지로 진행할까요? (1: 예, 2: 아니오): ");
                int goNext = sc.nextInt();
                if (goNext != 1) {
                    boolean inShop = true;
                    while (inShop) {
                        inShop = shop.open(user);  // ✅ '아니오' 선택 시 상점 루프
                    }
                    continue; // 다시 스테이지 클리어 이후 루프로
                }

                // 다음 스테이지로 진행
                user.currentStage++;
            }else {
                System.out.println("💡 다시 도전하려면 스테이지 1부터 시작합니다.");
                user.currentStage = 1;
            }

        }


        userRepo.update(user);
        System.out.println("\n📦 게임 저장 완료. 수고하셨습니다!");
        System.out.println("🧑 유저 ID: " + user.id);
        System.out.println("📈 최종 스테이지: " + user.currentStage);
        System.out.println("🦕 보유 공룡: " + (user.dinoIds == null || user.dinoIds.isBlank() ? "없음" : user.dinoIds));
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

    private List<Dino> generateEnemiesByStage(int stageNumber) {
        DinoRepository dinoRepo = new DinoRepository();
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
                System.out.println((i + 1) + ". " + team[i].name + " (HP: " + team[i].hp + ")");
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

    private void handleEnemyTurn(Dino enemy, Dino player) {
        if (enemy.hp < 60 && enemy.canUseSkill() && new Random().nextInt(100) < 60) {
            enemy.useSkill(player);
        } else {
            battleService.normalAttack(enemy, player);
        }
    }

    private List<Integer> createStarterDinos() {
        DinoRepository dinoRepo = new DinoRepository();
        List<Integer> starterIds = new ArrayList<>();
        List<Dino> randomDinos = dinoRepo.findRandomDinosByTear(5, 3);
        for (Dino dino : randomDinos) {
            starterIds.add(dino.id);
        }
        return starterIds;
    }

    private DinoDTO cloneDTO(DinoDTO dto) {
        return new DinoDTO(dto.id, dto.name, dto.hp, dto.maxHp, dto.skillCount, dto.maxSkillCount);
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

    private void playSound(String type) {
        switch (type) {
            case "육" -> Roar.play();
            case "해" -> SeaRoar.play();
            case "공" -> FlyingRoar.play();
            default -> System.out.println("[사운드 없음] 알 수 없는 타입: " + type);
        }
    }
}
