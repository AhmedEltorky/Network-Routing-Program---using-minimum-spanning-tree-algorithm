package networkprojectrouting;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Ahmed El-Torky
 */
public class Screen {

    private GraphicsDevice gd;

    public Screen() {
        GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = genv.getDefaultScreenDevice();
    }

    public void setWindowTitle(String title, JFrame window) {
        window.setTitle(title);
    }

    public void setWindowIcon(Image icon, JFrame window) {
        window.setIconImage(icon);
    }

    public void setFullScreen(DisplayMode dm, JFrame window) {
        window.setResizable(false);
        gd.setFullScreenWindow(window);
        if (dm != null && gd.isDisplayChangeSupported()) {
            try {
                gd.setDisplayMode(dm);
            } catch (Exception ex) {

            }
        }
    }

    public Window getFullScreenWindow() {
        return gd.getFullScreenWindow();
    }

    public void restoreScreen() {
        Window win = gd.getFullScreenWindow();
        if (win != null) {
            win.dispose();
        }
        gd.setFullScreenWindow(null);
    }

    public void showMessageDialog(String message, JFrame window) {
        JOptionPane.showMessageDialog(window, message);
    }

    public void showInputMessage(String message, String title, JFrame window) {
        JOptionPane.showInputDialog(window, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    public void showErrorMessage(String message, String title, JFrame window) {
        JOptionPane.showMessageDialog(window, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
