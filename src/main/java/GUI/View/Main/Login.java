package GUI.View.Main;

import Data.MainObjectData.Affiliate;
import Data.Utils.AnimationUtils;
import GUI.Controller.Main.LoginCtrl;
import GUI.View.Misc.GUIUtils;
import GUI.View.Misc.MinimalisticButton;
import com.github.weisj.darklaf.components.loading.LoadingIndicator;
import com.zero.animation.BaseAnimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JPanel {
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel picLabel;
    private JPanel content;
    private JPanel indicatorPanel;
    private final LoadingIndicator indicator = new LoadingIndicator();

    private final JFrame owner;

    public Login(JFrame owner) {
        this(owner, true);
    }

    public Login(JFrame owner, boolean animate) {
        super(new GridLayout());

        Affiliate.current = null;

        this.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        this.setOpaque(false);
        add(content);
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {}
        });

        indicatorPanel.setLayout(new GridLayout());
        indicator.setRunning(true);
        indicator.setVisible(false);
        indicatorPanel.add(indicator);

        this.owner = owner;

        GUIUtils.defaultText(userTextField, "e-mail");
        GUIUtils.defaultText(passwordField, "Contraseña");

        picLabel.setIcon(GUIUtils.loadIcon("user.svg", 200, 200));

        new LoginCtrl(this);

        owner.setGlassPane(this);
        owner.getRootPane().setDefaultButton(loginButton);

        if (animate) {
            AnimationUtils.getController().add(new BaseAnimation(200) {
                @Override
                protected void onProgress(float pProgress) {
                    setLocation(0, Math.round(-getHeight()*(1 - pProgress)));
                }
            });
        }

        this.setVisible(true);
    }

    public JTextField getUserTextField() {
        return userTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JLabel getPicLabel() {
        return picLabel;
    }

    public JFrame getOwner() {
        return owner;
    }

    public void setIndicatorVisible(boolean b) {
        indicator.setVisible(b);
    }

    private void createUIComponents() {
        loginButton = new MinimalisticButton();
    }
}
