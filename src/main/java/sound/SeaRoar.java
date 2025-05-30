package sound;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioClip;

import java.net.URL;

public class SeaRoar {
    private static boolean initialized = false;

    public static void play() {
        // JavaFX 플랫폼 초기화 (단 1회만)
        if (!initialized) {
            new JFXPanel(); // JavaFX 초기화 트리거
            initialized = true;
        }

        try {
            URL resource = SeaRoar.class.getResource("/sounds/seaRoar.wav");
            if (resource == null) {
                System.out.println("❌ seaRoar.wav 파일이 존재하지 않습니다.");
                return;
            }

            AudioClip clip = new AudioClip(resource.toString());
            clip.play();

        } catch (Exception e) {
            System.out.println("⚠️ 사운드 재생 실패: " + e.getMessage());
        }
    }
}
