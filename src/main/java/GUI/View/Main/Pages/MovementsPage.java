package GUI.View.Main.Pages;

import Data.DB;
import Data.MainObjectData.AccountData.Account;
import Data.MainObjectData.AccountData.AccountRegistry;
import Data.MainObjectData.AccountData.TransactionType;
import Data.MainObjectData.Affiliate;
import Data.Utils.DateUtils;
import Data.Utils.StringUtils;
import GUI.Controller.Pages.Movements.MovementsPageCtrl;
import GUI.View.Misc.GUIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class MovementsPage extends InfoPage {
    private JSpinner startSpinner;
    private JSpinner endSpinner;
    private JComboBox<String> transactionComboBox;
    private JPanel tablePanel;
    private JTable registriesTable;
    private JCheckBox undefStartCheckBox;
    private JCheckBox undefEndCheckBox;
    private JComboBox<String> accountComboBox;
    private JPanel content;

    private final DefaultTableModel model;

    public MovementsPage() {
        Affiliate.current.updateAccounts();

        for (Account ac: Affiliate.current.getAccounts())
            accountComboBox.addItem(ac.toString());

        startSpinner.setModel(new SpinnerDateModel(new Date(), null, new Date(), Calendar.MONTH));
        endSpinner.setModel(new SpinnerDateModel(new Date(), null, new Date(), Calendar.MONTH));

        startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "MM/yyyy"));
        endSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "MM/yyyy"));

        transactionComboBox.addItem("Todos");

        for (TransactionType type: TransactionType.values())
            transactionComboBox.addItem(type.toString());

        GUIUtils.wrapOverlayScrollPane(tablePanel, registriesTable);

        model = new DefaultTableModel();

        registriesTable.setModel(model);
        model.addColumn("Fecha");
        model.addColumn("Descripción");
        model.addColumn("Transacción");
        model.addColumn("Saldo anterior");
        model.addColumn("Monto");
        model.addColumn("Nuevo saldo");

        registriesTable.getColumnModel().getColumn(1).setPreferredWidth(280);

        new MovementsPageCtrl(this);

        updatePage();

        add(content);
    }

    private final AtomicBoolean updatable = new AtomicBoolean(true);

    @Override
    public void updatePage() {
        if (updatable.get()) {
            new Thread(() -> {
                updatable.set(false);

                Account ac = Affiliate.current.getAccounts()[accountComboBox.getSelectedIndex()];
                AccountRegistry[] registries;

                Date start, end;

                if (undefStartCheckBox.isSelected())
                    start = null;
                else
                    start = (Date) startSpinner.getValue();

                if (undefEndCheckBox.isSelected())
                    end = null;
                else
                    end = (Date) endSpinner.getValue();

                registries = DB.getRegistries(ac, start, end,
                        TransactionType.valueOf(transactionComboBox.getSelectedIndex() - 1));

                if (registries != null) {
                    SwingUtilities.invokeLater(() -> {
                        model.setRowCount(0);

                        for (AccountRegistry reg: registries) {
                            double delta = reg.getDelta();
                            String sign;

                            if (delta < 0) {
                                delta = -delta;
                                sign = "- ";
                            } else
                                sign = "+ ";

                            model.addRow(new Object[]{DateUtils.formatDate(reg.getDate()), reg.getDescription(), reg.getTransactionType(),
                                    StringUtils.formatCurrency(reg.getPreviousBalance()),
                                    sign + StringUtils.formatCurrency(delta),
                                    StringUtils.formatCurrency(reg.getNewBalance())});
                        }
                    });
                }

                updatable.set(true);
            }).start();
        }
    }

    public JSpinner getStartSpinner() {
        return startSpinner;
    }

    public JSpinner getEndSpinner() {
        return endSpinner;
    }

    public JComboBox<String> getTransactionComboBox() {
        return transactionComboBox;
    }

    public JCheckBox getUndefStartCheckBox() {
        return undefStartCheckBox;
    }

    public JCheckBox getUndefEndCheckBox() {
        return undefEndCheckBox;
    }

    public JComboBox<String> getAccountComboBox() {
        return accountComboBox;
    }
}
