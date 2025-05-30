package sound;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioClip;

import java.net.URL;

public class FlyingRoar {
    private static boolean initialized = false;

    public static void play() {
        // JavaFX 플랫폼 초기화 (한 번만 실행)
        if (!initialized) {
            new JFXPanel(); // JavaFX 환경을 초기화
            initialized = true;
        }

        try {
            URL resource = FlyingRoar.class.getResource("/sounds/flyingRoar.wav");
            if (resource == null) {
                System.out.println("❌ flyingRoar.wav 파일이 존재하지 않습니다.");
                return;
            }

            AudioClip clip = new AudioClip(resource.toString());
            clip.play(); // 사운드 재생

        } catch (Exception e) {
            System.out.println("⚠️ 사운드 재생 실패: " + e.getMessage());
        }
    }
}
