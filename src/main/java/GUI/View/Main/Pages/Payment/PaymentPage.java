package GUI.View.Main.Pages.Payment;

import Data.Utils.AnimationUtils;
import GUI.Controller.Pages.Payment.PaymentPageCtrl;
import GUI.View.Main.Pages.InfoPage;
import GUI.View.Misc.GUIUtils;
import GUI.View.Misc.MinimalisticButton;
import com.zero.animation.AnimationFinishedListener;
import com.zero.animation.BaseAnimation;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PaymentPage extends InfoPage {
    private JButton electricityButton;
    private JButton waterButton;
    private JButton phoneButton;
    private JButton internetButton;
    private JPanel content;
    private JPanel buttonPanel;

    private ButtonAnimator[] animators = new ButtonAnimator[4];

    public PaymentPage() {
        electricityButton.setIcon(GUIUtils.loadIcon("Pages/Payment/electricity.svg", 50, 50, true));
        waterButton.setIcon(GUIUtils.loadIcon("Pages/Payment/water.svg", 50, 50, true));
        phoneButton.setIcon(GUIUtils.loadIcon("Pages/Payment/phone.svg", 50, 50, true));
        internetButton.setIcon(GUIUtils.loadIcon("Pages/Payment/internet.svg", 50, 50, true));

        new PaymentPageCtrl(this);

        add(content);
    }

    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile boolean relocate = true;

    @Override
    public void updatePage() {
        if (!running.get()) {
            running.set(true);
            relocate = true;

            ButtonAnimator[] animators = new ButtonAnimator[]{
                    new ButtonAnimator(electricityButton){
                        @Override
                        protected void onProgress(float pProgress) {
                            super.onProgress(pProgress);

                            if (relocate) {
                                //electricityButton.setLocation(electricityButton.getX(), content.getHeight() + 5);
                                waterButton.setLocation(waterButton.getX(), content.getHeight() + 5);
                                phoneButton.setLocation(phoneButton.getX(), content.getHeight() + 5);
                                internetButton.setLocation(internetButton.getX(), content.getHeight() + 5);
                            }
                        }
                    },
                    new ButtonAnimator(waterButton) {
                        @Override
                        protected void onStart() {
                            relocate = false;
                        }
                    },
                    new ButtonAnimator(phoneButton),
                    new ButtonAnimator(internetButton, pAnimation -> running.set(false))
            };

            AnimationUtils.getController().add(animators[0]);
            AnimationUtils.getController().add(150, animators[1]);
            AnimationUtils.getController().add(300, animators[2]);
            AnimationUtils.getController().add(450, animators[3]);
        }
    }

    private volatile float alpha = 1.0f;

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    public JButton getElectricityButton() {
        return electricityButton;
    }

    public JButton getWaterButton() {
        return waterButton;
    }

    public JButton getPhoneButton() {
        return phoneButton;
    }

    public JButton getInternetButton() {
        return internetButton;
    }

    private void createUIComponents() {
        electricityButton = new MinimalisticButton();
        waterButton = new MinimalisticButton();
        phoneButton = new MinimalisticButton();
        internetButton = new MinimalisticButton();

        buttonPanel = new JPanel() {
            private boolean animate = true;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (animate) {
                    updatePage();
                    animate = false;
                }
            }
        };

        content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                ((Graphics2D) g).setComposite(ac);
                super.paintComponent(g);
            }
        };
    }

    private class ButtonAnimator extends BaseAnimation {
        private final JButton button;
        private final int y;

        public ButtonAnimator(JButton button, AnimationFinishedListener l) {
            super(500);
            this.button = button;
            y = this.button.getLocation().y;

            if (l != null)
                this.addFinishedListener(l);
        }

        public ButtonAnimator(JButton button) {
            this(button, null);
        }

        @Override
        protected void onProgress(float pProgress) {
            button.setLocation(button.getLocation().x, y + (int) Math.round(content.getHeight() * fx(pProgress)));
        }

        private double fx(float x) {
            return Math.abs(Math.pow(Math.log(x + 1), 0.1) - 0.964);
        }
    }
}
