package GUI.View.Main.Pages;

import GUI.View.Main.MainWindow;

import javax.swing.*;

public abstract class Subpage extends InfoPage {
    private final InfoPage previous;

    protected Subpage(InfoPage prev) {
        previous = prev;
    }

    public void back() {
        if (previous != null) {
            SwingUtilities.invokeLater(() -> MainWindow.getInstance().changeContent(previous));
        }
    }
}
