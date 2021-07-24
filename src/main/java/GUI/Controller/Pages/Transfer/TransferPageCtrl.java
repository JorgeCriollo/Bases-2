package GUI.Controller.Pages.Transfer;

import Data.DB;
import Data.MainObjectData.AccountData.Account;
import Data.MainObjectData.Affiliate;
import GUI.View.Main.MainWindow;
import GUI.View.Main.Pages.TransferPage;
import GUI.View.Misc.GUIUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TransferPageCtrl {
    private final TransferPage tp;

    public TransferPageCtrl(TransferPage TP) {
        tp = TP;

        tp.getAccountTextField().getDocument().addDocumentListener(new DocumentListener() {
            private final Timer timer = new Timer(500, e -> update());

            {
                timer.setRepeats(false);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                initUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                initUpdate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                initUpdate();
            }

            private void initUpdate() {
                tp.hideDetails();
                timer.restart();
            }
        });

        tp.getAccountComboBox().addActionListener(e -> update());
        tp.getAmountSpinner().addChangeListener(e -> tp.updateAmount());

        tp.getTransferButton().addActionListener(e -> {
            tp.setComponentsEnabled(false);
            tp.setLoading(true);

            Account affAccount = Affiliate.current.getAccounts()[tp.getAccountComboBox().getSelectedIndex()];

            long from = affAccount.getAccountNumber(), to = tp.getRecipient().getAccounts()[0].getAccountNumber();

            double amount = (double) tp.getAmountSpinner().getValue();

            if (amount > affAccount.getBalance()) {
                GUIUtils.errorNotification("Saldo insuficiente.");
                return;
            }

            new Thread(() -> {
                if (DB.transfer(from, to, amount)) {
                    GUIUtils.confirmNotification("Transferencia realizada.");
                    Affiliate.current.updateAccounts();
                    SwingUtilities.invokeLater(() -> MainWindow.getInstance().changeContent(new TransferPage()));
                } else
                    SwingUtilities.invokeLater(() -> tp.setLoading(false));

                tp.setComponentsEnabled(true);
            }).start();
        });
    }

    private void update() {
        tp.hideDetails();
        tp.updatePage();
    }
}
