package memoranda.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SplashFrame extends JFrame {

  private final int WIDTH = 400;
  private final int HEIGHT = 300;

  private final String SPLASH_IMAGE = "/ui/splash.png";
  private ImageIcon splashIcon;
  private JLabel splashLabel;

  public SplashFrame() {
    splashIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(SPLASH_IMAGE)));
    splashLabel = new JLabel();
    splashLabel.setSize(WIDTH, HEIGHT);
    splashLabel.setIcon(splashIcon);
    getContentPane().add(splashLabel);
    setSize(WIDTH, HEIGHT);

    // Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(
        (screenSize.width - WIDTH) / 2,
        (screenSize.height - HEIGHT) / 2);

    setUndecorated(true);
  }
}
