package sound;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SeaRoar {
    private static boolean initialized = false;

    public static void play() {
        // JavaFX 초기화 (최초 1회만)
        if (!initialized) {
            new JFXPanel();
            initialized = true;
        }

        try {
            URL resource = SeaRoar.class.getResource("/sounds/seaRoar.wav");
            if (resource == null) {
                System.out.println("❌ seaRoar.wav 파일이 존재하지 않습니다.");
                return;
            }

            Media media = new Media(resource.toString());
            MediaPlayer player = new MediaPlayer(media);
            player.play();

            // 2초 후 자동 정지
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    player.stop();
                } catch (InterruptedException e) {
                    System.out.println("⚠️ 사운드 정지 실패: " + e.getMessage());
                }
            }).start();

        } catch (Exception e) {
            System.out.println("⚠️ 사운드 재생 실패: " + e.getMessage());
        }
    }
}
