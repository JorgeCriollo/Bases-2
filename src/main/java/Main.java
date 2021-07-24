import Data.DB;
import Data.Utils.AudioUtils;
import GUI.View.Main.MainWindow;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        LafManager.install(new DarculaTheme());
        AudioUtils.init();

        DB.connect();

        SwingUtilities.invokeLater(MainWindow::new);
    }
}
