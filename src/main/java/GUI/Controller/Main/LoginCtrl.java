package GUI.Controller.Main;

import Data.DB;
import Data.Utils.AnimationUtils;
import GUI.View.Main.Login;

import com.zero.animation.BaseAnimation;

import javax.swing.*;

public class LoginCtrl {
    private final Login login;

    public LoginCtrl(Login l) {
        login = l;

        login.getLoginButton().addActionListener(e -> {
            login.setIndicatorVisible(true);

            new Thread(() -> {
                if (DB.login(login.getUserTextField().getText(), login.getPasswordField().getPassword())) {
                    SwingUtilities.invokeLater(() -> {
                        MainWindowsCtrl.getInstance().init();
                        login.setIndicatorVisible(false);

                        AnimationUtils.getController().add(70, new BaseAnimation(500) {
                            @Override
                            protected void onProgress(float pProgress) {
                                login.setLocation(0, (int) (login.getHeight() * -fx(pProgress)));
                            }

                            @Override
                            protected void onFinish() {
                                login.setVisible(false);
                            }
                        });
                    });
                }
            }).start();
        });
    }

    private double fx(float x) {
        return 3.5 * Math.pow(x-0.3381, 2) - 0.4;
    }
}
