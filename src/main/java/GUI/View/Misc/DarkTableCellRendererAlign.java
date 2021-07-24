package GUI.View.Misc;

import com.github.weisj.darklaf.ui.table.renderer.DarkTableCellRenderer;
import com.github.weisj.darklaf.util.ColorUtil;

import javax.swing.*;
import java.awt.*;

public class DarkTableCellRendererAlign extends DarkTableCellRenderer {
    int alignment;

    public DarkTableCellRendererAlign(int alignment) {
        this.alignment = alignment;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setBackground(null);
        Object effectiveValue = value;
        if (value instanceof Color) {
            effectiveValue = "";
        }

        Component component = super.getTableCellRendererComponent(table, effectiveValue, isSelected, hasFocus, row, column);
        if (value instanceof Color) {
            component.setBackground(ColorUtil.stripUIResource((Color)value, false));
        }

        if (component instanceof JLabel) {
            ((JLabel)component).setHorizontalAlignment(alignment);
        } else if (component instanceof AbstractButton) {
            ((AbstractButton)component).setHorizontalAlignment(alignment);
        }
        setBorder(noFocusBorder);
        return component;
    }
}
