package GUI.View.Main.Pages.Payment;

import Data.DB;
import Data.MainObjectData.AccountData.Account;
import Data.MainObjectData.Affiliate;
import Data.SecondaryObjectData.Bill;
import Data.SecondaryObjectData.Provider;
import Data.SecondaryObjectData.Service;
import Data.Utils.AnimationUtils;
import Data.Utils.StringUtils;
import GUI.Controller.Pages.Payment.PaymentDataCtrl;
import GUI.View.Main.MainWindow;
import GUI.View.Main.Pages.Subpage;
import GUI.View.Misc.DarkTableCellRendererAlign;
import GUI.View.Misc.GUIUtils;
import GUI.View.Misc.MinimalisticButton;
import com.github.weisj.darklaf.components.loading.LoadingIndicator;
import com.zero.animation.BaseAnimation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PaymentData extends Subpage {
    private JButton backButton;
    private JLabel titleLabel;
    private JComboBox<String> providerComboBox;
    private JTextField contractTextField;
    private JComboBox<String> accountComboBox;
    private JTable totalTable;
    private JButton payButton;
    private JButton refreshButton;
    private JPanel content;
    private JLabel totalValueLabel;
    private JPanel paymentDetailsPanel;
    private JLabel infoLabel;
    private JPanel indicatorPanel;

    private final DefaultTableModel model = new DefaultTableModel();
    private final LoadingIndicator indicator = new LoadingIndicator();

    private final Provider[] providers;
    private final Service service;
    private volatile Bill[] bills;
    private double sum;

    public PaymentData(PaymentPage prev, Service service) {
        super(prev);
        backButton.addActionListener(e -> back());
        backButton.setIcon(GUIUtils.loadIcon("Misc/back.svg", 17, 30, true));
        refreshButton.setIcon(GUIUtils.loadIcon("Misc/refresh.svg", 30, 30, true));

        titleLabel.setText(service.toString());

        providers = DB.getProviders(service);
        for (Provider p: providers)
            providerComboBox.addItem(p.getName());

        this.service = service;

        GUIUtils.defaultText(contractTextField, "");
        GUIUtils.setNumbersOnly(contractTextField);

        for (Account ac: Affiliate.current.getAccounts())
            accountComboBox.addItem(ac.toString());

        totalTable.setModel(model);
        model.addColumn("Descripción");
        model.addColumn("Valor");
        DarkTableCellRendererAlign cellRenderer = new DarkTableCellRendererAlign(SwingConstants.RIGHT);
        TableColumn column = totalTable.getColumnModel().getColumn(1);
        column.setCellRenderer(cellRenderer);
        column.setPreferredWidth(200);

        cellRenderer = new DarkTableCellRendererAlign(SwingConstants.LEFT);
        totalTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        totalTable.setDefaultEditor(Object.class, null);

        MainWindow.getInstance().setDefaultButton(payButton);

        indicatorPanel.setLayout(new GridLayout());
        indicatorPanel.add(indicator);
        indicator.setRunning(true);
        indicator.setVisible(false);

        new PaymentDataCtrl(this);

        add(content);

        AnimationUtils.getController().add(new Animator());
    }

    private final AtomicBoolean updating = new AtomicBoolean(false);

    @Override
    public void updatePage() {
        if (!updating.get()) {
            new Thread(() -> {
                updating.set(true);
                bills = DB.getBills(providers[providerComboBox.getSelectedIndex()], service,
                        contractTextField.getText());

                if (bills == null)
                    GUIUtils.infoNotification("No existe ningún contrato con este número.");
                else {
                    SwingUtilities.invokeLater(() -> {
                        model.setRowCount(0);
                        sum = 0;

                        for (Bill b : bills) {
                            model.addRow(new Object[]{b.getDescription(), StringUtils.formatCurrency(b.getValue())});
                            sum += b.getValue();
                        }

                        if (bills.length > 0) {
                            paymentDetailsPanel.setVisible(true);
                            totalValueLabel.setText("Total a pagar: " + StringUtils.formatCurrency(sum));
                        } else {
                            paymentDetailsPanel.setVisible(false);
                            GUIUtils.infoNotification("Parece que sus pagos están al día.");
                        }
                    });
                }

                updating.set(false);
            }).start();
        }
    }

    public JComboBox<String> getProviderComboBox() {
        return providerComboBox;
    }

    public JTextField getContractTextField() {
        return contractTextField;
    }

    public JComboBox<String> getAccountComboBox() {
        return accountComboBox;
    }

    public JButton getPayButton() {
        return payButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JLabel getInfoLabel() {
        return infoLabel;
    }

    public Provider[] getProviders() {
        return providers;
    }

    public Service getService() {
        return service;
    }

    public double getSum() {
        return sum;
    }

    public void hideDetails() {
        paymentDetailsPanel.setVisible(false);
    }

    public void setLoading(boolean b) {
        indicator.setVisible(b);
    }

    public void setComponentsEnabled(boolean b) {
        backButton.setEnabled(b);
        refreshButton.setEnabled(b);
        providerComboBox.setEnabled(b);
        contractTextField.setEnabled(b);
        accountComboBox.setEnabled(b);
        payButton.setEnabled(b);
        MainWindow.getInstance().setTabsEnabled(b);
    }

    private volatile float alpha = 1.0f;

    private void createUIComponents() {
        content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

                ((Graphics2D) g).setComposite(ac);

                super.paintComponent(g);
            }
        };

        payButton = new MinimalisticButton();
    }

    private class Animator extends BaseAnimation {
        Animator() {
            super(150);
        }

        @Override
        protected void onProgress(float pProgress) {
            alpha = pProgress;
            repaint();
        }
    }
}
