package GUI.View.Misc;

import javax.swing.*;
import java.awt.*;

public class TabButton extends JRadioButton {
    private final Font regular;

    public TabButton(Icon icon) {
        if (icon != null)
            setIcon(icon);
        else
            setIcon(GUIUtils.loadIcon("Misc/blank.png"));

        regular = getFont().deriveFont(20f);
        setFont(regular);

        setModel(new ToggleButtonModel() {
            @Override
            public void setSelected(boolean b) {
                super.setSelected(b);

                if (b) {
                    setFont(regular.deriveFont(Font.BOLD));
                } else
                    if (!this.isSelected())
                        setFont(regular);

            }
        });
    }

    public TabButton(Icon icon, String text) {
        this(icon);
        setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color borderlessHover = UIManager.getColor("Button.borderless.hover");
        Color borderlessClick = UIManager.getColor("Button.borderless.click");
        Color borderlessOutlineHover = UIManager.getColor("Button.borderless.outline.hover");
        Color borderlessOutlineClick = UIManager.getColor("Button.borderless.outline.click");
        Color selected = UIManager.getColor("borderFocus");

        boolean rollover = this.getModel().isRollover();
        boolean pressed = this.getModel().isPressed();

        if (rollover)
            drawFiller(borderlessHover, g);

        if (pressed)
            drawFiller(borderlessClick, g);

        if (isSelected()) {
            g.setColor(selected);
            g.fillRect(0, 0, 5, getHeight());
        }

        if (rollover)
            drawOutLine(borderlessOutlineHover, g);

        if (pressed)
            drawOutLine(borderlessOutlineClick, g);

        super.paintComponent(g);
    }

    private void drawFiller(Color fill, Graphics g) {
        g.setColor(fill);
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private void drawOutLine(Color outline, Graphics g) {
        g.setColor(outline);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
