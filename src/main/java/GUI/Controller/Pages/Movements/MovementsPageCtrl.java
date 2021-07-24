package GUI.Controller.Pages.Movements;

import GUI.View.Main.Pages.MovementsPage;

public class MovementsPageCtrl {
    private final MovementsPage mp;

    public MovementsPageCtrl(MovementsPage MP) {
        mp = MP;

        mp.getAccountComboBox().addActionListener(e -> update());

        mp.getStartSpinner().addChangeListener(e -> update());

        mp.getEndSpinner().addChangeListener(e -> update());

        mp.getTransactionComboBox().addActionListener(e -> update());

        mp.getUndefStartCheckBox().addActionListener(e -> update());

        mp.getUndefEndCheckBox().addActionListener(e -> update());
    }

    private void update() {
        mp.updatePage();
    }
}
