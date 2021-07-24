package GUI.View.Misc;

import javax.swing.*;
import java.awt.*;

public class MinimalisticButton extends JButton {
    public MinimalisticButton() {
        setContentAreaFilled(false);
    }

    public MinimalisticButton(Icon icon) {
        super(icon);
        setContentAreaFilled(false);
    }

    public MinimalisticButton(String text) {
        super(text);
        setContentAreaFilled(false);
    }

    public MinimalisticButton(Action a) {
        super(a);
        setContentAreaFilled(false);
    }

    public MinimalisticButton(String text, Icon icon) {
        super(text, icon);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color borderlessHover;
        Color borderlessClick;

        Graphics2D g2d = (Graphics2D) g;

        if (isDefaultButton()) {
            borderlessHover = UIManager.getColor("Button.defaultFillColorRollOver");
            borderlessClick = UIManager.getColor("Button.defaultFillColorClick");
            drawRoundRects(UIManager.getColor("Button.defaultFillColor"), g2d);
        } else {
            borderlessHover = UIManager.getColor("Button.borderless.hover");
            borderlessClick = UIManager.getColor("Button.borderless.click");
        }

        if (this.getModel().isRollover()) {
            drawRoundRects(borderlessHover, g2d);
        }

        if (this.getModel().isPressed()) {
            drawRoundRects(borderlessClick, g2d);
        }

        super.paintComponent(g);
    }

    private void drawRoundRects(Color fill, Graphics2D g2d) {
        int arc = UIManager.getInt("Button.arc") + 4;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Insets insets = getInsets();

        g2d.setColor(fill);
        g2d.fillRoundRect(insets.left,
                insets.top,
                getWidth() - insets.right - 2,
                getHeight() - insets.bottom - 2, arc, arc);
    }
}
