
import java.util.*;

public class Stage {
    private int stageNumber;
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();

    public Stage(int stageNumber) {
        this.stageNumber = stageNumber;
    }

    public boolean play(Player player) {
        List<Dano> playerTeam = player.getMyDinosaurs();
        List<Dano> enemyTeam = generateEnemyTeam();

        int playerIndex = 0;
        int enemyIndex = 0;

        while (playerIndex < playerTeam.size() && enemyIndex < enemyTeam.size()) {
            Dano playerDino = playerTeam.get(playerIndex);
            Dano enemyDino = enemyTeam.get(enemyIndex);

            SoundPlayer.play(playerDino.name);
            SoundPlayer.play(enemyDino.name);

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
                Shop shop = new Shop();
                shop.open(player);
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

    private void battle(Player player, Dano playerDino, Dano enemyDino) {
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
                        Dano d = player.getMyDinosaurs().get(i);
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

    private List<Dano> generateEnemyTeam() {
        return DBLoader.getEnemyTeamByStage(stageNumber); // DB 기반 로딩
    }
}
