package sound;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioClip;

import java.net.URL;

public class Roar {
    private static boolean initialized = false;

    public static void play() {
        // JavaFX 플랫폼 초기화 (딱 한 번만 실행)
        if (!initialized) {
            new JFXPanel(); // JavaFX media 시스템 초기화
            initialized = true;
        }

        try {
            URL resource = Roar.class.getResource("/sounds/Roar.wav");
            if (resource == null) {
                System.out.println("❌ Roar.wav 파일이 존재하지 않습니다.");
                return;
            }

            AudioClip clip = new AudioClip(resource.toString());
            clip.play(); // 즉시 재생

        } catch (Exception e) {
            System.out.println("⚠️ 사운드 재생 실패: " + e.getMessage());
        }
    }
}
