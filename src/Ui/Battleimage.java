package Ui;

import domain.Dino;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;

public class Battleimage {

    // 공룡 클래스 이름과 이미지 경로 매핑
    private static final Map<String, String> IMAGE_MAP = Map.of(
            "Quetzalcoatlus",  "file:src/image/Quetzalcoatlus.png",
            "Giganotosaurus",  "file:src/image/giganotosaurus.png",
            "Mosasaurus",      "file:src/image/Mosasaurus.png",
            "Parasaurolophus", "file:src/image/Parasaurolophus.png",
            "Dimorphodon",     "file:src/image/Dimorphodon.png",
            "Ichthyosaurus",   "file:src/image/Ichthyosaurus.png"
    );

    static {
        new JFXPanel(); // JavaFX 환경 초기화 (JFXPanel을 통해 런타임 준비)
    }

    private static ImageView playerView; // 플레이어 공룡 이미지뷰
    private static ImageView enemyView;  // 적 공룡 이미지뷰
    private static Stage stage;          // 전투 창(Stage)

    // 전투 창을 띄우고 초기 공룡 이미지 설정
    public static void showBattle(Dino player, Dino enemy) {
        new Thread(() -> Platform.runLater(() -> {
            try {
                String backgroundPath = "file:src/image/bg.png"; // 배경 이미지 경로

                // 배경 이미지 설정
                Image bgImage = new Image(backgroundPath);
                BackgroundSize bgSize = new BackgroundSize(800, 500, false, false, false, false);
                BackgroundImage bg = new BackgroundImage(
                        bgImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        bgSize
                );
                Background background = new Background(bg);

                Pane root = new Pane();
                root.setBackground(background);

                // 플레이어 이미지뷰 초기화 및 위치 설정
                playerView = new ImageView();
                playerView.setFitWidth(220);
                playerView.setPreserveRatio(true);
                playerView.setLayoutX(50);
                playerView.setLayoutY(280);

                // 적 이미지뷰 초기화 및 위치 설정
                enemyView = new ImageView();
                enemyView.setFitWidth(220);
                enemyView.setPreserveRatio(true);
                enemyView.setLayoutX(500);
                enemyView.setLayoutY(280);

                // 이미지뷰들을 Pane에 추가
                root.getChildren().addAll(playerView, enemyView);

                // Scene, Stage 구성
                Scene scene = new Scene(root, 800, 500);
                stage = new Stage();
                stage.setAlwaysOnTop(true); // 항상 위에 띄우기
                stage.setTitle("🦖 전투 무대!");
                stage.setScene(scene);
                stage.show();

                updateBattle(player, enemy); // 초기 공룡 이미지 설정

            } catch (Exception e) {
                System.err.println("전투 이미지 창 오류: " + e.getMessage());
                e.printStackTrace();
            }
        })).start();
    }

    // 이미지뷰를 새로운 공룡으로 교체하거나 숨김
    public static void updateBattle(Dino player, Dino enemy) {
        Platform.runLater(() -> {
            if (playerView != null && player != null) {
                String playerKey = player.getClass().getSimpleName();
                String playerPath = IMAGE_MAP.getOrDefault(playerKey, IMAGE_MAP.get("Dimorphodon"));
                playerView.setImage(new Image(playerPath));
                playerView.setVisible(player.isAlive()); // 죽었으면 숨김
            }

            if (enemyView != null && enemy != null) {
                String enemyKey = enemy.getClass().getSimpleName();
                String enemyPath = IMAGE_MAP.getOrDefault(enemyKey, IMAGE_MAP.get("Dimorphodon"));
                enemyView.setImage(new Image(enemyPath));
                enemyView.setVisible(enemy.isAlive()); // 죽었으면 숨김
            }
        });
    }

    // 전투 창 닫기
    public static void closeBattle() {
        Platform.runLater(() -> {
            if (stage != null) stage.close();
        });
    }
}
