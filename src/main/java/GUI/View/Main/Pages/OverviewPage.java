package GUI.View.Main.Pages;

import Data.MainObjectData.AccountData.Account;
import Data.MainObjectData.Affiliate;
import Data.Utils.DateUtils;
import Data.Utils.ImageUtils;
import Data.Utils.StringUtils;
import GUI.View.Misc.GUIUtils;
import GUI.View.Misc.RingGraph;
import com.github.weisj.darklaf.components.OverlayScrollPane;
import com.github.weisj.darklaf.icons.DarkSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OverviewPage extends InfoPage {
    private JPanel content;
    private JLabel nameLabel;
    private JLabel extraLabel;
    private JPanel RingGraphPanel;
    private JPanel tablePanel;
    private JTable accountTable;
    private JLabel sinceLabel;

    private final DefaultTableModel model = new DefaultTableModel();;
    private final OverlayScrollPane scrollPane;

    public OverviewPage() {
        RingGraphPanel.setLayout(new GridLayout());

        scrollPane = GUIUtils.wrapOverlayScrollPane(tablePanel, accountTable);
        accountTable.setModel(model);
        accountTable.setDefaultEditor(Object.class, null);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountTable.getTableHeader().setFont(accountTable.getTableHeader().getFont().deriveFont((float) 20));
        GUIUtils.formatTable(accountTable);

        model.addColumn("");
        model.addColumn("Número de cuenta");
        model.addColumn("Tipo de cuenta");
        model.addColumn("Balance");

        accountTable.getColumnModel().getColumn(0).setMaxWidth(25);

        updatePage();
        add(content);
    }

    @Override
    public void updatePage() {
        Affiliate aff = Affiliate.current;

        nameLabel.setText(aff.getFullName());
        extraLabel.setText("(" + aff.getEmail() + ")");

        double[] balances = new double[aff.getAccounts().length];

        for (int i = 0; i < balances.length; i++) {
            balances[i] = aff.getAccounts()[i].getBalance();
        }

        Image picture;

        if ((picture = aff.getPicture()) == null)
            picture = ((DarkSVGIcon) GUIUtils.loadIcon("user.svg")).createImage(400, 400);

        RingGraph graph = new RingGraph(balances, picture, 25);

        RingGraphPanel.removeAll();
        RingGraphPanel.add(graph);
        RingGraphPanel.revalidate();
        RingGraphPanel.repaint();

        Color[] colors = graph.getColors();

        model.setRowCount(0);

        for (int i = 0; i < aff.getAccounts().length; i++) {
            Account ac = aff.getAccounts()[i];
            model.addRow(
                    new Object[]{
                            colors[i],
                            ac.getAccountNumber(),
                            ac.getType().toString(),
                            StringUtils.formatCurrency(ac.getBalance())
                    }
            );
        }

        /* scrollPane.setPreferredSize(new Dimension(-1,
                (model.getRowCount() + 1) * (accountTable.getRowHeight() + 3)));*/

        sinceLabel.setText("Miembro desde: " + DateUtils.formatDate(aff.getMemberSince()));
        sinceLabel.setEnabled(false);
    }

    private void createUIComponents() {
        content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int size = Math.min(getWidth()/3, getHeight()/3);

                Image background = ImageUtils.fade(
                        GUIUtils.loadImageFromSVG("Pages/Overview/background.svg", size, size, true),
                        0.2f
                );

                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(background, getWidth() - size, getHeight() - size, size, size, null);
            }
        };
    }
}
