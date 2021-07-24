package GUI.Controller.Pages.Payment;

import Data.DB;
import Data.MainObjectData.AccountData.Account;
import Data.MainObjectData.Affiliate;
import Data.SecondaryObjectData.Provider;
import Data.SecondaryObjectData.Service;
import Data.Utils.StringUtils;
import GUI.View.Main.Pages.Payment.PaymentData;
import GUI.View.Misc.GUIUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PaymentDataCtrl {
    private final PaymentData pd;

    public PaymentDataCtrl(PaymentData PD) {
        pd = PD;

        pd.getRefreshButton().addActionListener(e -> pd.updatePage());
        pd.getContractTextField().getDocument().addDocumentListener(new DocumentListener() {
            private final Timer timer = new Timer(500, e -> pd.updatePage());

            {
               timer.setRepeats(false);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                if (!StringUtils.isBlank(pd.getContractTextField().getText())) {
                    pd.getInfoLabel().setVisible(false);
                    timer.restart();
                } else {
                    pd.getInfoLabel().setVisible(true);
                    pd.hideDetails();
                    timer.stop();
                }
            }
        });

        pd.getPayButton().addActionListener(e -> {
            Provider provider = pd.getProviders()[pd.getProviderComboBox().getSelectedIndex()];
            Service service = pd.getService();
            Account account = Affiliate.current.getAccounts()[pd.getAccountComboBox().getSelectedIndex()];

            pd.setLoading(true);
            pd.setComponentsEnabled(false);

            if (pd.getSum() > account.getBalance()) {
                GUIUtils.errorNotification("Saldo insuficiente.");
                return;
            }

            new Thread(() -> {
                if (DB.pay(provider, service, pd.getContractTextField().getText(), account)) {
                    GUIUtils.confirmNotification("Pago realizado con éxito.");
                    pd.back();
                } else {
                    SwingUtilities.invokeLater(() -> pd.setLoading(false));
                }

                pd.setComponentsEnabled(true);
            }).start();
        });
    }
}
