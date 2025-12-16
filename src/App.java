import javax.swing.SwingUtilities;
import view.LoginView;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
