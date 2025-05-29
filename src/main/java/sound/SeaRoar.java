package sound;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;

public class SeaRoar extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("JavaFX 이미지 + 오디오");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 🎵 오디오 로딩 비동기
        Task<MediaPlayer> audioTask = new Task<>() {
            @Override
            protected MediaPlayer call() {
                URL resource = getClass().getResource("/sounds/seaRoar.wav");
                if (resource == null) {
                    System.out.println("❌ 오디오 파일이 존재하지 않습니다.");
                    return null;
                }

                Media media = new Media(resource.toExternalForm());
                return new MediaPlayer(media);
            }
        };


        audioTask.setOnSucceeded(event -> {
            MediaPlayer mediaPlayer = audioTask.getValue();
            if (mediaPlayer != null) {
                mediaPlayer.setOnReady(() -> {
                    System.out.println("▶ 오디오 준비 완료");
                    mediaPlayer.play();
                });

                mediaPlayer.setOnEndOfMedia(() -> {
                    System.out.println("✔ 오디오 재생 완료");
                    mediaPlayer.dispose();
                });

                mediaPlayer.setOnError(() -> {
                    System.out.println("⚠️ 오디오 오류: " + mediaPlayer.getError().getMessage());
                });
            }
        });

        audioTask.setOnFailed(event -> {
            System.out.println("⚠️ 오디오 로딩 실패");
        });

        // 🖼 이미지 로딩 비동기
        Task<Image> imageTask = new Task<>() {
            @Override
            protected Image call() {
                URL imageUrl = getClass().getResource("../book.png");
                if (imageUrl == null) {
                    System.out.println("❌ 이미지 파일이 존재하지 않습니다.");
                    return null;
                }

                return new Image(imageUrl.toExternalForm(), true); // true: 백그라운드 로딩
            }
        };


        imageTask.setOnSucceeded(event -> {
            Image image = imageTask.getValue();
            if (image != null) {
                Platform.runLater(() -> {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(400);
                    imageView.setPreserveRatio(true);
                    root.getChildren().add(imageView);
                });
            }
        });

        imageTask.setOnFailed(event -> {
            System.out.println("⚠️ 이미지 로딩 실패");
        });

        // ✅ 비동기 실행 시작
        new Thread(audioTask).start();
        new Thread(imageTask).start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
