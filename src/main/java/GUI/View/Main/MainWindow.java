package GUI.View.Main;

import Data.Utils.ImageUtils;
import GUI.Controller.Main.MainWindowsCtrl;
import GUI.View.Misc.GUIUtils;
import GUI.View.Misc.TabButton;
import GUI.View.Misc.TabPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    private JPanel content;
    private JPanel container;
    private JPanel scrollable;
    private JPanel tabPanel;
    private JButton userButton;

    private TabButton overviewTabButton;
    private TabButton paymentsTabButton;
    private TabButton transfersTabButton;
    private TabButton movementsTabButton;

    private final JFrame frame;
    private static final Icon USER_ICON = GUIUtils.loadIcon("user.svg", 70);

    private static MainWindow instance;

    public static MainWindow getInstance() {
        return instance;
    }

    public MainWindow() {
        scrollable.setLayout(new GridLayout());
        GUIUtils.wrapOverlayScrollPane(container, scrollable);

        userButton.setIcon(USER_ICON);

        new MainWindowsCtrl(this);
        instance = this;

        frame = new JFrame();
        frame.setIconImage(GUIUtils.loadImageFromSVG("MainWindow/appIcon.svg", 200, 200));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(content);
        frame.pack();
        new Login(frame, false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        overviewTabButton = new TabButton(null, "Resumen");
        paymentsTabButton = new TabButton(null, "Pagos");
        transfersTabButton = new TabButton(null, "Transferencias");
        movementsTabButton = new TabButton(null, "Movimientos");

        tabPanel = new TabPanel(
                overviewTabButton,
                paymentsTabButton,
                transfersTabButton,
                movementsTabButton
        );
    }

    public void lock() {
        new Login(frame);
        scrollable.removeAll();
    }

    public void setDefaultButton(JButton button) {
        frame.getRootPane().setDefaultButton(button);
    }

    public void changeContent(JPanel newContent) {
        for (Component c: scrollable.getComponents())
            c.setVisible(false);

        scrollable.removeAll();
        scrollable.add(newContent);
        newContent.setVisible(true);
        scrollable.revalidate();
        scrollable.repaint();
    }

    public JButton getUserButton() {
        return userButton;
    }

    public TabButton getOverviewTabButton() {
        return overviewTabButton;
    }

    public TabButton getPaymentsTabButton() {
        return paymentsTabButton;
    }

    public TabButton getTransfersTabButton() {
        return transfersTabButton;
    }

    public TabButton getMovementsTabButton() {
        return movementsTabButton;
    }

    public void setTabsEnabled(boolean b) {
        tabPanel.setEnabled(b);
    }

    public void setUserIcon(Image img) {
        if (img != null)
            userButton.setIcon(new ImageIcon(ImageUtils.circleImage(img, 70, true)));
        else
            userButton.setIcon(USER_ICON);
    }

    public JFrame getFrame() {
        return frame;
    }
}
