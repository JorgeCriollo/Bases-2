package GUI.View.Main.Pages;

import Data.DB;
import Data.MainObjectData.AccountData.Account;
import Data.MainObjectData.Affiliate;
import Data.Utils.AnimationUtils;
import Data.Utils.ImageUtils;
import Data.Utils.StringUtils;
import GUI.Controller.Pages.Transfer.TransferPageCtrl;
import GUI.View.Main.MainWindow;
import GUI.View.Misc.GUIUtils;
import GUI.View.Misc.MinimalisticButton;
import com.github.weisj.darklaf.components.loading.LoadingIndicator;
import com.github.weisj.darklaf.icons.DarkSVGIcon;
import com.zero.animation.BaseAnimation;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransferPage extends InfoPage {
    private JButton transferButton;
    private JComboBox<String> accountComboBox;
    private JTextField accountTextField;
    private JLabel affPicture;
    private JLabel recipientPicture;
    private JLabel affNameLabel;
    private JLabel affAccountTypeLabel;
    private JLabel affAccountBalanceLabel;
    private JLabel deductedMoneyLabel;
    private JLabel recipientNameLabel;
    private JLabel recipientAccountTypeLabel;
    private JLabel recipientMoneyAddedLabel;
    private JLabel transferIcon;
    private JPanel transferDetailsPanel;
    private JPanel indicatorPanel;
    private JPanel content;
    private JSpinner amountSpinner;

    private final LoadingIndicator indicator = new LoadingIndicator();

    private static final Icon icon = GUIUtils.loadIcon("Pages/Transfer/arrowActive.svg", 30, 10);
    private static final Icon USER_ICON = GUIUtils.loadIcon("user.svg", 70);

    private Affiliate recipient;

    public TransferPage() {
        indicatorPanel.setLayout(new GridLayout());
        indicatorPanel.add(indicator);
        indicator.setRunning(true);
        indicator.setVisible(false);

        MainWindow.getInstance().setDefaultButton(transferButton);

        GUIUtils.setNumbersOnly(accountTextField);
        GUIUtils.defaultText(accountTextField, "Número de cuenta");

        Affiliate aff = Affiliate.current;

        accountComboBox.removeAllItems();
        for (Account ac : aff.getAccounts())
            accountComboBox.addItem(ac.toString());

        setUserIcon(affPicture, aff.getPicture());

        accountComboBox.addActionListener(e ->
                amountSpinner.setModel(new SpinnerNumberModel(
                        1d,
                        0.01d,
                        aff.getAccounts()[accountComboBox.getSelectedIndex()].getBalance(),
                        0.5d
                        )
                )
        );

        amountSpinner.setModel(new SpinnerNumberModel(1d, 0.01d,
                aff.getAccounts()[accountComboBox.getSelectedIndex()].getBalance(), 0.5d));

        amountSpinner.setEditor(new JSpinner.NumberEditor(amountSpinner));

        new TransferPageCtrl(this);

        add(content);
    }

    public void setUserIcon(JLabel iconLabel, Image img) {
        if (img != null)
            iconLabel.setIcon(new ImageIcon(ImageUtils.circleImage(img, 70, true)));
        else
            iconLabel.setIcon(USER_ICON);
    }

    private final AtomicBoolean updatable = new AtomicBoolean(true);

    @Override
    public void updatePage() {
        if (updatable.get()) {
            updatable.set(false);
            new Thread(() -> {
                Affiliate aff = Affiliate.current;
                Account affAccount = aff.getAccounts()[accountComboBox.getSelectedIndex()];
                String accountNumber = accountTextField.getText();
                recipient = StringUtils.isBlank(accountNumber) ? null : DB.getRecipient(Long.parseLong(accountNumber));

                if (recipient != null) {
                    SwingUtilities.invokeLater(() -> {
                        affNameLabel.setText(aff.getFullName() + " - " + affAccount.getAccountNumber());
                        affAccountTypeLabel.setText(affAccount.getType().toString());
                        affAccountBalanceLabel.setText(StringUtils.formatCurrency(affAccount.getBalance()));

                        Account recipientAccount = recipient.getAccounts()[0];

                        setUserIcon(recipientPicture, recipient.getPicture());
                        recipientNameLabel.setText(recipient.getFullName() + " - " + recipientAccount.getAccountNumber());
                        recipientAccountTypeLabel.setText(recipientAccount.getType().toString());

                        updateAmount();

                        transferDetailsPanel.setVisible(true);
                    });
                }

                updatable.set(true);
            }).start();
        }
    }

    public void updateAmount() {
        double amount = (double) amountSpinner.getValue();

        deductedMoneyLabel.setText("- " + StringUtils.formatCurrency(amount));
        recipientMoneyAddedLabel.setText("+ " + StringUtils.formatCurrency(amount));
    }

    public JButton getTransferButton() {
        return transferButton;
    }

    public JComboBox<String> getAccountComboBox() {
        return accountComboBox;
    }

    public JTextField getAccountTextField() {
        return accountTextField;
    }

    public JSpinner getAmountSpinner() {
        return amountSpinner;
    }

    public Affiliate getRecipient() {
        return recipient;
    }

    public void hideDetails() {
        transferDetailsPanel.setVisible(false);
    }

    public void setComponentsEnabled(boolean b) {
        accountComboBox.setEnabled(b);
        accountTextField.setEnabled(b);
        amountSpinner.setEnabled(b);
        transferButton.setEnabled(b);
        MainWindow.getInstance().setTabsEnabled(b);
    }

    public void setLoading(boolean b) {
        indicator.setVisible(b);
    }

    private boolean animate = true;

    private volatile float progress;

    private void createUIComponents() {
        transferButton = new MinimalisticButton();

        content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (animate) {
                    AnimationUtils.getController().add(new Animator());
                    animate = false;
                }
            }
        };

        transferIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth(), height;

                width *= (1 - (progress/3));
                height = width/3;

                Image img = ((DarkSVGIcon) icon).createImage(width, height);
                int y1 = Math.round((getHeight() * progress) / 3);
                g.drawImage(img, (getWidth() - width)/2, y1, null);

                width = getWidth();
                width *= (2/3f - (progress/3));
                height = width/3;


                img = ((DarkSVGIcon) icon).createImage(width, height);
                int y2 = getHeight() / 3 + y1;
                g.drawImage(img, (getWidth() - width) / 2, y2, null);

                width = getWidth();
                width *= (1/3f - (progress/3));
                height = width/3;

                if (width > 0 && height > 0) {
                    img = ((DarkSVGIcon) icon).createImage(width, height);
                    g.drawImage(img, (getWidth() - width) / 2, getHeight() / 3 + y2, null);
                }
            }
        };
    }

    private class Animator extends BaseAnimation {
        public Animator() {
            super(600);
            loopInfinite();
        }

        @Override
        protected void onProgress(float pProgress) {
            progress = pProgress;
            transferIcon.repaint();

            if (!isVisible()) {
                AnimationUtils.getController().cancel(this);
            }
        }
    }
}
