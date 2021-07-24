package GUI.View.Main.Pages;

import javax.swing.*;
import java.awt.*;

public abstract class InfoPage extends JPanel {
    protected InfoPage() {
        super(new GridLayout());
    }

    public abstract void updatePage();
}
