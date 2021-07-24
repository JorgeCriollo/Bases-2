package Data.Utils;

import com.zero.animation.AniController;

public class AnimationUtils {
    private static final AniController controller = new AniController(144, 2*60_000);

    public static AniController getController() {
        return controller;
    }
}
