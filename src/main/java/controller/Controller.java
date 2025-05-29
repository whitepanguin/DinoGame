package controller;


import domain.*;
import service.*;

import java.util.Scanner;

public class Controller {
    private final Service dinoService = new Service();
    private final BattleService battleService = new BattleService();
    private final Scanner sc = new Scanner(System.in);
    private final PlayerController player = new PlayerController();

    public void start() {
        System.out.println("🦕 게임을 시작합니다!");
        player.addPoints(50); // 기본 포인트 제공

        // 초기 공룡 선택
        System.out.println("\n🦖 초기 공룡 3마리를 선택하세요.");
        for (int i = 0; i < 3; i++) {
//            Dino d = dinoService.getRandomDino();
//            player.addDinosaur(d);
        }

        int stageNum = 1;
        while (stageNum <= 5) {
            System.out.println("\n========================");
            System.out.println("🚩 스테이지 " + stageNum + " 시작!");
            System.out.println("========================");

            StageController stage = new StageController(stageNum);
            boolean cleared = stage.play(player);

            if (!cleared) {
                System.out.println("\n💀 스테이지 실패! 1스테이지부터 다시 시작합니다.");
                stageNum = 1; // 처음부터 시작하되, 공룡과 포인트는 유지
            } else {
                stageNum++;
            }
        }

        System.out.println("\n🏆 모든 스테이지 클리어! 축하합니다!");
    }
}
