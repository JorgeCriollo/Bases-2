import GUI.View.Main.Pages.Payment.PaymentPage;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;

import javax.swing.*;
import java.awt.*;

public class Test {

    public static void main(String[] args) {
        LafManager.install(new DarculaTheme());
        /*try {
            testComponent(new RingGraph(new double[]{1.5, 2.1, 3.7, 2.4, 4.6},
                    ImageIO.read(Objects.requireNonNull(Test.class.getClassLoader().getResource("test.jpg"))),
                    25));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        testComponent(new PaymentPage());
    }

    private static void testComponent(Component c) {
        JFrame frame = new JFrame("Test");
        JPanel panel = new JPanel(new GridLayout());
        panel.add(c);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
