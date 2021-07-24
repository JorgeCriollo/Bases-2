package GUI.Controller.Main;

import Data.MainObjectData.Affiliate;
import GUI.View.Main.MainWindow;
import GUI.View.Main.Pages.InfoPage;
import GUI.View.Main.Pages.MovementsPage;
import GUI.View.Main.Pages.OverviewPage;
import GUI.View.Main.Pages.Payment.PaymentPage;
import GUI.View.Main.Pages.TransferPage;
import GUI.View.Misc.GUIUtils;

import javax.swing.*;
import java.awt.*;

public class MainWindowsCtrl {
    private static MainWindowsCtrl instance;

    public static MainWindowsCtrl getInstance() {
        return instance;
    }

    private final MainWindow mw;
    private InfoPage currentPage;

    public  MainWindowsCtrl(MainWindow MW) {
        instance = this;

        mw = MW;

        JPopupMenu logoutMenu = new JPopupMenu();
        JMenuItem logout = new JMenuItem("Cerrar sesión");

        logout.addActionListener(e -> mw.lock());

        logoutMenu.add(logout);

        mw.getUserButton().addActionListener(e -> {
            Point button = mw.getUserButton().getLocationOnScreen();
            Point mouse = MouseInfo.getPointerInfo().getLocation();
            logoutMenu.show(mw.getUserButton(), mouse.x - button.x, mouse.y - button.y);
        });

        mw.getOverviewTabButton().addActionListener(e -> {
            if (currentPage instanceof OverviewPage)
                currentPage.updatePage();
            else {
                currentPage = new OverviewPage();
                mw.changeContent(currentPage);
            }
        });

        mw.getPaymentsTabButton().addActionListener(e -> {
            if (currentPage instanceof PaymentPage)
                currentPage.updatePage();
            else {
                currentPage = new PaymentPage();
                mw.changeContent(currentPage);
            }
        });

        mw.getTransfersTabButton().addActionListener(e -> {
            if (currentPage instanceof TransferPage)
                currentPage.updatePage();
            else {
                currentPage = new TransferPage();
                mw.changeContent(currentPage);
            }
        });

        mw.getMovementsTabButton().addActionListener(e -> {
            if (currentPage instanceof MovementsPage)
                currentPage.updatePage();
            else {
                currentPage = new MovementsPage();
                mw.changeContent(currentPage);
            }
        });
    }

    public void init() {
        mw.setUserIcon(Affiliate.current.getPicture());
        currentPage = new OverviewPage();
        mw.changeContent(currentPage);
    }
}
