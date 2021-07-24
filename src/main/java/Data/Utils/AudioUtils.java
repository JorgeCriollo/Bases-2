package Data.Utils;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;

public class AudioUtils {
    private static final String PATH = "GUI/Sounds/";

    private static Clip info;
    private static Clip confirmation;
    private static Clip error;

    public static void init(){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    AudioUtils.class.getClassLoader().getResourceAsStream(PATH + "info.wav"));
            info = AudioSystem.getClip();
            info.open(ais);
            ais.close();

            ais = AudioSystem.getAudioInputStream(
                    AudioUtils.class.getClassLoader().getResourceAsStream(PATH + "confirmation.wav"));
            confirmation = AudioSystem.getClip();
            confirmation.open(ais);
            ais.close();

            ais = AudioSystem.getAudioInputStream(
                    AudioUtils.class.getClassLoader().getResourceAsStream(PATH + "error.wav"));
            error = AudioSystem.getClip();
            error.open(ais);
            ais.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void information() {
        info.setFramePosition(0);
        info.start();
    }

    public static void confirmation() {
        confirmation.setFramePosition(0);
        confirmation.start();
    }

    public static void error() {
        error.setFramePosition(0);
        error.start();
    }
}
