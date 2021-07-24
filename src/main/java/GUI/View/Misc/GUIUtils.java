package GUI.View.Misc;

import Data.Utils.AudioUtils;
import Data.Utils.StringUtils;
import GUI.View.Main.MainWindow;
import com.github.weisj.darklaf.components.OverlayScrollPane;
import com.github.weisj.darklaf.icons.DarkSVGIcon;
import com.github.weisj.darklaf.icons.IconLoader;
import com.github.weisj.darklaf.ui.spinner.DarkSpinnerUI;
import com.github.weisj.darklaf.ui.table.DarkTableUI;
import com.github.weisj.darklaf.ui.text.DarkPasswordFieldUI;
import com.github.weisj.darklaf.ui.text.DarkTextAreaUI;
import com.github.weisj.darklaf.ui.text.DarkTextFieldUI;
import com.github.weisj.darklaf.ui.text.DarkTextUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIUtils {
    private static final String ICON_PATH = "Icons/";
    private static final IconLoader ICON_LOADER = IconLoader.get(GUIUtils.class);

    public static final String MANDATORY_FIELD = "<Obligatorio>";

    public static void searchBar(JTextField textField) {
        textField.putClientProperty(DarkTextFieldUI.KEY_VARIANT, DarkTextFieldUI.VARIANT_SEARCH);
        defaultText(textField, "Buscar...");
    }

    /**
     * Adds greyed out text to a text field containing a message. This message will only be visible when the user hasn't
     * inputted any character in it.
     * @param component Text component field that will display the message
     * @param msg Message to be displayed
     */
    public static void defaultText(JTextComponent component, String msg) {
        if (component instanceof JTextField)
            if (component instanceof JPasswordField)
                formatPasswordField((JPasswordField) component, msg);
            else
                formatTextField((JTextField) component, msg);
        else if (component instanceof JTextArea)
            formatTextArea((JTextArea) component, msg);
    }

    private static void formatTextField(JTextField textField, String msg) {
        textField.putClientProperty(DarkTextFieldUI.KEY_DEFAULT_TEXT, msg);
        textField.putClientProperty(DarkTextFieldUI.KEY_SHOW_CLEAR, true);

    }

    private static void formatTextArea(JTextArea textArea, String msg) {
        textArea.putClientProperty(DarkTextAreaUI.KEY_DEFAULT_TEXT, msg);
    }

    private static void formatPasswordField(JPasswordField passwordField, String msg) {
        passwordField.putClientProperty(DarkPasswordFieldUI.KEY_DEFAULT_TEXT, msg);
        passwordField.putClientProperty(DarkPasswordFieldUI.KEY_SHOW_VIEW_BUTTON, true);
    }

    public static void formatTable(JTable table) {
        table.putClientProperty(DarkTableUI.KEY_ALTERNATE_ROW_COLOR, true);
    }

    public static void formatSpinner(JSpinner spinner) {
        spinner.putClientProperty(DarkSpinnerUI.KEY_VARIANT, DarkSpinnerUI.VARIANT_PLUS_MINUS);
    }

    public static OverlayScrollPane wrapOverlayScrollPane(JPanel container, JComponent c) {
        return wrapOverlayScrollPane(container, c, 10);
    }

    public static OverlayScrollPane wrapOverlayScrollPane(JPanel container, JComponent c, int increment) {
        return wrapOverlayScrollPane(container, c, increment, true);
    }

    public static OverlayScrollPane wrapOverlayScrollPane(JPanel container, JComponent c, int increment, boolean opaque) {
        container.setLayout(new GridLayout());
        container.removeAll();

        OverlayScrollPane overlayScrollPane = new OverlayScrollPane(c);
        overlayScrollPane.getVerticalScrollBar().setUnitIncrement(increment);
        overlayScrollPane.setOpaque(opaque);
        overlayScrollPane.getScrollPane().setOpaque(opaque);
        overlayScrollPane.getScrollPane().getViewport().setOpaque(opaque);

        container.add(overlayScrollPane);

        return overlayScrollPane;
    }

    public static Icon loadIcon(String path) {
        return ICON_LOADER.getIcon(ICON_PATH + path);
    }

    public static Icon loadIcon(String path, int size) {
        return ICON_LOADER.getIcon(ICON_PATH + path, size, size);
    }

    public static Icon loadIcon(String path, boolean themed) {
        return ICON_LOADER.getIcon(ICON_PATH + path, themed);
    }

    public static Icon loadIcon(String path, int width, int height) {
        return ICON_LOADER.getIcon(ICON_PATH + path, width, height);
    }

    public static Icon loadIcon(String path, int width, int height, boolean themed) {
        return ICON_LOADER.getIcon(ICON_PATH + path, width, height, themed);
    }

    public static Image loadImageFromSVG(String path, int width, int height) {
        return loadImageFromSVG(path, width, height, false);
    }

    public static Image loadImageFromSVG(String path, int width, int height, boolean themed) {
        return ((DarkSVGIcon) loadIcon(path, width, height, themed)).createImage(width, height);
    }

    private static final HashMap<Component, JPopupMenu> popups = new HashMap<>();

    /**
     * Shows a popup belonging a given component on the lower left corner of its owner. If it doesn't exist, creates it.
     * @param owner Owner component
     * @param contents Content components
     */
    public static void showPopup(Component owner, Component... contents) {
        JPopupMenu popupMenu;

        if ((popupMenu = popups.get(owner)) == null) {
            popupMenu = new JPopupMenu();

            for (Component c: contents)
                popupMenu.add(c);

            popups.put(owner, popupMenu);
        }

        popupMenu.show(owner, 0, owner.getHeight());
    }

    /**
     * Sets a text component to have visual indication that it must be filled
     * @param component Component to be formatted
     */
    public static void formatMandatoryField(JTextComponent component) {
        defaultText(component, MANDATORY_FIELD);
        component.addFocusListener(new FilledListener(component));
    }

    /**
     * Sets an array of text components to have visual indication that it must be filled
     * @param components Components to be formatted
     */
    public static void formatMandatoryField(JTextComponent... components) {
        for (JTextComponent c: components)
            formatMandatoryField(c);
    }

    public static void textComponentError(JTextComponent component, boolean b) {
        component.putClientProperty(DarkTextUI.KEY_HAS_ERROR, b);
    }

    /**
     * Sets a text field to display the error outline if the content doesn't match a standard e-mail regex
     * @param textField Text field to apply the formatting to
     * @param msg Default message for the text field
     */
    public static void formatEmailField(JTextField textField, String msg) {
        defaultText(textField, msg);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkEmail();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkEmail();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkEmail();
            }

            private void checkEmail() {
                textField.putClientProperty(DarkTextFieldUI.KEY_HAS_ERROR,
                        !StringUtils.validEmail(textField.getText()) && !StringUtils.isBlank(textField.getText()));
            }
        });
    }

    /**
     * Sets an accelerator key for a button. This can be a key alone or along with the default system shortcut key as
     * modifier
     * @param button Instance of the button to which the shortcut will be mapped
     * @param keyCode Key identifier
     * @param defaultShortcutMask If it's true, the default system shortcut key will be used as a modifier
     */
    public static void setAccelerator(JButton button, int keyCode, boolean defaultShortcutMask) {
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionListener[] listeners = button.getActionListeners();

                if (listeners.length > 0)
                    listeners[0].actionPerformed(e);
            }
        };

        KeyStroke keyStroke;

        if (defaultShortcutMask)
            keyStroke = KeyStroke.getKeyStroke(keyCode, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        else
            keyStroke = KeyStroke.getKeyStroke(keyCode, 0);

        action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        button.getActionMap().put(action.getValue(Action.ACCELERATOR_KEY), action);
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, action.getValue(Action.ACCELERATOR_KEY));
    }

    /**
     * Shows a yes/no dialog and returns the value of the selected option
     * @param msg Message to show in the dialog
     * @param title Title of the dialog window
     * @return true if the selected option was yes, false otherwise
     */
    public static boolean yesNoDialog(String msg, String title) {
        return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION;
    }

    public static void errorMsg(String msg) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    public static void errorMsg(Exception e) {
        errorMsg(e.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace();
    }

    public static void infoMsg(String title, String msg) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE)
        );
    }

    public static void infoNotification(String msg) {
        notification(msg, NotificationPanel.Type.info);
        AudioUtils.information();
    }

    public static void confirmNotification(String msg) {
        notification(msg, NotificationPanel.Type.confirm);
        AudioUtils.confirmation();
    }

    public static void errorNotification(String msg) {
        notification(msg, NotificationPanel.Type.error);
        AudioUtils.error();
    }

    private static void notification(String msg, NotificationPanel.Type type) {
        SwingUtilities.invokeLater(() -> {
            MainWindow.getInstance().getFrame().getGlassPane().setVisible(false);
            NotificationPanel notification = new NotificationPanel(type, msg);
            MainWindow.getInstance().getFrame().setGlassPane(notification);
            notification.setVisible(true);
            notification.repaint();
        });
    }

    public static void setNumbersOnly(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(numberFilter);
    }

    private static final DocumentFilter numberFilter = new DocumentFilter() {
        final Pattern regEx = Pattern.compile("\\d*");

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            Matcher matcher = regEx.matcher(text);
            if(!matcher.matches()) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.replace(fb, offset, length, text, attrs);
        }
    };

    /**
     * Formats a text component to display the error outline if it loses focus while being blank and enabled
     */
    private static class FilledListener implements FocusListener {
        JTextComponent component;

        public FilledListener(JTextComponent component) {
            this.component = component;
        }

        @Override
        public void focusGained(FocusEvent e) {
            component.putClientProperty(DarkTextUI.KEY_HAS_ERROR, false);
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (component.isEnabled() && StringUtils.isBlank(component.getText()))
                component.putClientProperty(DarkTextUI.KEY_HAS_ERROR, true);
        }
    }
}
