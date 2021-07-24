package GUI.Controller.Pages.Payment;

import Data.SecondaryObjectData.Service;
import Data.Utils.AnimationUtils;
import GUI.View.Main.MainWindow;
import GUI.View.Main.Pages.Payment.PaymentData;
import GUI.View.Main.Pages.Payment.PaymentPage;
import com.zero.animation.BaseAnimation;

public class PaymentPageCtrl {
    private final PaymentPage pp;

    public PaymentPageCtrl(PaymentPage PP) {
        pp = PP;

        pp.getElectricityButton().addActionListener(e -> new FadeAnimator(Service.power));

        pp.getWaterButton().addActionListener(e -> new FadeAnimator(Service.water));

        pp.getPhoneButton().addActionListener(e -> new FadeAnimator(Service.telephone));

        pp.getInternetButton().addActionListener(e -> new FadeAnimator(Service.internet));
    }

    private class FadeAnimator extends BaseAnimation {
        Service service;

        FadeAnimator(Service service) {
            super(150);
            this.service = service;
            AnimationUtils.getController().add(this);
        }

        @Override
        protected void onProgress(float pProgress) {
            pp.setAlpha(1 - pProgress);
        }

        @Override
        protected void onFinish() {
            MainWindow.getInstance().changeContent(new PaymentData(pp, service));
            pp.setAlpha(1.0f);
        }
    }
}
