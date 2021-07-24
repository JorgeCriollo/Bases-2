package GUI.View.Misc;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TabPanel extends JPanel{
    private JPanel content;
    private JPanel scrollable;
    private JPanel tabsPanel;

    private final ButtonGroup group;

    private final HashMap<String, TabButton> buttons = new HashMap<>();

    protected TabPanel() {
        GUIUtils.wrapOverlayScrollPane(content, scrollable);

        tabsPanel.setLayout(new GridLayout(0, 1));

        group = new ButtonGroup();

        setLayout(new GridLayout());
        add(content);
    }

    public TabPanel(Icon[] icons, String[] text) {
        this();
        boolean hasIcons = icons != null;

        if (text == null)
            throw new IllegalArgumentException("Text array cannot be null.");

        if (hasIcons && icons.length != text.length)
            throw new IllegalArgumentException("Icon array and text array must be the same length.");
        TabButton[] tabs = new TabButton[text.length];

        for (int i = 0; i < text.length; i++) {
            Icon icon = null;

            if (hasIcons)
                icon = icons[i];

            tabs[i] = new TabButton(icon, text[i]);
        }

        addButtons(tabs);
        tabs[0].setSelected(true);
    }

    public TabPanel(TabButton... tabs) {
        this();

        addButtons(tabs);
        tabs[0].setSelected(true);
    }

    public void addButtons(TabButton... tabs) {
        for (TabButton tab: tabs)
            addButton(tab);
    }

    public void addButton(TabButton tab) {
        if (buttons.putIfAbsent(tab.getText(), tab) != null)
            throw new IllegalStateException("Two buttons cannot share the same name");

        tabsPanel.add(tab);
        group.add(tab);
    }

    public void addButton(Icon icon, String text) {
        addButton(new TabButton(icon, text));
    }

    public void addButton(String text) {
        addButton(null, text);
    }

    public TabButton getButton(String text) {
        return buttons.get(text);
    }

    public void setFontSize(int size) {
        for (TabButton b: buttons.values()) {
            b.setFont(b.getFont().deriveFont((float) size));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for (TabButton button: buttons.values())
            button.setEnabled(enabled);
    }
}
