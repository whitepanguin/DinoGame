package sound;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;

public class FlyingRoar extends Application {

    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("JavaFX 오디오 10초 재생");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 👇 창 숨기기 (선택)
        // primaryStage.setOpacity(0);      // 완전 투명
        // primaryStage.setIconified(true); // 최소화
        // primaryStage.hide();             // 아예 숨김

        // 🎵 오디오 재생 쓰레드
        Thread audioThread = new Thread(() -> {
            try {
                URL resource = getClass().getResource("/sounds/flyingRoar.wav");
                if (resource == null) {
                    System.out.println("❌ 오디오 파일이 존재하지 않습니다.");
                    return;
                }

                Media media = new Media(resource.toExternalForm());
                mediaPlayer = new MediaPlayer(media);

                mediaPlayer.setOnReady(() -> {
                    System.out.println("▶ 오디오 준비 완료");
                    mediaPlayer.play();

                    // 🔟 10초 후 자동 종료
                    new Thread(() -> {
                        try {
                            Thread.sleep(10_000); // 10초
                            Platform.runLater(() -> {
                                if (mediaPlayer != null) {
                                    mediaPlayer.stop();
                                    mediaPlayer.dispose();
                                    System.out.println("🛑 종료합니다");
                                }
                                Platform.exit(); // JavaFX 앱 종료
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                });

                mediaPlayer.setOnError(() -> {
                    System.out.println("⚠️ 오디오 오류: " + mediaPlayer.getError().getMessage());
                });

            } catch (Exception e) {
                System.out.println("⚠️ 오디오 로딩 예외: " + e.getMessage());
            }
        });

        audioThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
