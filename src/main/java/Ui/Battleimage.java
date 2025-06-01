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
import sound.FlyingRoar;
import sound.Roar;
import sound.SeaRoar;

public class Battleimage {

    private static final Map<String, String> IMAGE_MAP = Map.of(
            "육", "file:src/image/Parasaurolophus.png",
            "공", "file:src/image/Dimorphodon.png",
            "해", "file:src/image/Ichthyosaurus.png"
    );

    static {
        new JFXPanel(); // JavaFX 초기화
    }

    private static ImageView playerView;
    private static ImageView enemyView;
    private static Stage stage;
    private static Pane root;

    public static void showBattle(Dino player, Dino enemy, String backgroundPath) {
        new Thread(() -> Platform.runLater(() -> {
            try {
                // root와 stage가 null일 때만 초기화
                if (stage == null) {
                    root = new Pane();

                    playerView = new ImageView();
                    playerView.setFitWidth(220);
                    playerView.setPreserveRatio(true);
                    playerView.setLayoutX(50);
                    playerView.setLayoutY(280);

                    enemyView = new ImageView();
                    enemyView.setFitWidth(220);
                    enemyView.setPreserveRatio(true);
                    enemyView.setLayoutX(500);
                    enemyView.setLayoutY(280);

                    root.getChildren().addAll(playerView, enemyView);

                    Scene scene = new Scene(root, 800, 500);
                    stage = new Stage();
                    stage.setAlwaysOnTop(true);
                    stage.setTitle("🦖 전투 무대!");
                    stage.setScene(scene);
                    stage.show();
                }

                // ✅ 스테이지 변경에 따라 배경 업데이트
                updateBackground(backgroundPath);
                updateBattle(player, enemy);

            } catch (Exception e) {
                System.err.println("전투 이미지 창 오류: " + e.getMessage());
                e.printStackTrace();
            }
        })).start();
    }

    public static void updateBackground(String backgroundPath) {
        if (root == null) return;
        Platform.runLater(() -> {
            try {
                Image bgImage = new Image(backgroundPath);


                BackgroundSize bgSize = new BackgroundSize(800, 500, false, false, false, false);
                BackgroundImage bg = new BackgroundImage(
                        bgImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        bgSize
                );
                root.setBackground(new Background(bg));
            } catch (Exception e) {
                System.out.println("❌ 배경 이미지 로딩 실패: " + backgroundPath);
                e.printStackTrace();
            }
        });
    }


    public static void updateBattle(Dino player, Dino enemy) {
        Platform.runLater(() -> {
            if (playerView != null && player != null) {
                String playerKey = player.type;
                String playerPath = IMAGE_MAP.getOrDefault(playerKey, IMAGE_MAP.get("공"));
                playerView.setImage(new Image(playerPath));
                playerView.setVisible(player.isAlive());

                playSound(playerKey);
            }

            if (enemyView != null && enemy != null) {
                String enemyKey = enemy.type;
                String enemyPath = IMAGE_MAP.getOrDefault(enemyKey, IMAGE_MAP.get("공"));
                enemyView.setImage(new Image(enemyPath));
                enemyView.setVisible(enemy.isAlive());

                playSound(enemyKey);
            }
        });
    }

    public static void playSound(String type) {
        switch (type) {
            case "육" -> Roar.play();
            case "해" -> SeaRoar.play();
            case "공" -> FlyingRoar.play();
            default -> System.out.println("[사운드 없음] 알 수 없는 타입: " + type);
        }
    }


    public static void closeBattle() {
        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();
                stage = null;
                root = null;
            }
        });
    }
}
