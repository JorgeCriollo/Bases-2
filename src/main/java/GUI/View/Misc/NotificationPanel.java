package GUI.View.Misc;

import Data.Utils.AnimationUtils;
import com.zero.animation.BaseAnimation;

import javax.swing.*;
import java.awt.*;

public class NotificationPanel extends JPanel {
    private JLabel label;
    private JPanel content;
    private final Type type;

    public NotificationPanel(Type type, String msg) {
        super(new GridLayout());
        setOpaque(false);
        this.type = type;

        label.setIcon(type.icon);
        label.setText(msg);
        label.setForeground(type.foreground);

        add(content);
        revalidate();
    }

    private boolean animate = true;

    private void createUIComponents() {
        label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (animate) {
                    AnimationUtils.getController().add(new Animator());
                    animate = false;
                    return;
                }

                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(type.fill);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                g2d.setColor(type.outline);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                super.paintComponent(g);
            }
        };

        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public enum Type {
        info(new Color(100, 130, 250), new Color(200, 225, 250), new Color(100, 160, 255),
                GUIUtils.loadIcon("Misc/info.svg", 20, 20)),
        confirm(new Color(0, 129, 83), new Color(145, 215, 195), new Color(3, 148, 109),
                GUIUtils.loadIcon("Misc/check.svg", 20, 20)),
        error(new Color(250, 100, 100), new Color(250, 200, 200), new Color(250, 100, 100),
                GUIUtils.loadIcon("Misc/x.svg", 20, 20));

        Color outline;
        Color fill;
        Color foreground;
        Icon icon;

        Type(Color outline, Color fill, Color foreground, Icon icon) {
            this.outline = outline;
            this.fill = fill;
            this.foreground = foreground;
            this.icon = icon;
        }
    }

    private class Animator extends BaseAnimation {
        private Animator() {
            super(10000);
        }

        @Override
        protected void onProgress(float pProgress) {
            label.setLocation(0, (int) Math.round(fx(pProgress)));
        }

        private double fx(float x) {
            return label.getHeight() * Math.pow(2*x-1, 8);
        }

        @Override
        protected void onFinish() {
            setVisible(false);
        }
    }
}
