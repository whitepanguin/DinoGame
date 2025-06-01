// ============================
// 🦕 Battleimage.java (수정됨)
// - 스테이지 배경 이미지 동적 설정
// ============================

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

            "육", "file:src/image/Parasaurolophus.png",
            "공",     "file:src/image/Dimorphodon.png",
            "해",   "file:src/image/Ichthyosaurus.png"
    );

    static {
        new JFXPanel(); // JavaFX 환경 초기화
    }

    private static ImageView playerView;
    private static ImageView enemyView;
    private static Stage stage;

    // ✅ 배경 이미지 경로를 인자로 받도록 수정
    public static void showBattle(Dino player, Dino enemy, String backgroundPath) {
        new Thread(() -> Platform.runLater(() -> {
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
                Background background = new Background(bg);

                Pane root = new Pane();
                root.setBackground(background);

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

                updateBattle(player, enemy);

            } catch (Exception e) {
                System.err.println("전투 이미지 창 오류: " + e.getMessage());
                e.printStackTrace();
            }
        })).start();
    }

    public static void updateBattle(Dino player, Dino enemy) {
        Platform.runLater(() -> {
            if (playerView != null && player != null) {
                String playerKey = player.type;
                String playerPath = IMAGE_MAP.getOrDefault(playerKey, IMAGE_MAP.get("Dimorphodon"));
                playerView.setImage(new Image(playerPath));
                playerView.setVisible(player.isAlive());
            }

            if (enemyView != null && enemy != null) {
                String enemyKey = enemy.type;
                String enemyPath = IMAGE_MAP.getOrDefault(enemyKey, IMAGE_MAP.get("Dimorphodon"));
                enemyView.setImage(new Image(enemyPath));
                enemyView.setVisible(enemy.isAlive());
            }
        });
    }

    public static void closeBattle() {
        Platform.runLater(() -> {
            if (stage != null) stage.close();
        });
    }
}
