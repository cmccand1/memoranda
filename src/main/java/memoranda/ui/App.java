package memoranda.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.Calendar;

import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import memoranda.events.EventsScheduler;
import memoranda.util.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);
  // boolean packFrame = false;

  static AppFrame mainAppFrame = null;

  public static final String GUIDE_URL = "http://memoranda.sourceforge.net/guide.html";
  public static final String BUGS_TRACKER_URL = "http://sourceforge.net/tracker/?group_id=90997&atid=595566";
  public static final String WEBSITE_URL = "http://memoranda.sourceforge.net";

  private SplashFrame splashFrame = null;

  /*========================================================================*/
	/* Note: Please DO NOT edit the version/build info manually!
       The actual values are substituted by the Ant build script using 
       'version' property and datestamp.*/

  public static final String VERSION_INFO = "@VERSION@";
  public static final String BUILD_INFO = "@BUILD@";

  /*========================================================================*/

  public static AppFrame getMainAppFrame() {
    return mainAppFrame;
  }

  public void show() {
    if (mainAppFrame.isVisible()) {
      mainAppFrame.toFront();
      mainAppFrame.requestFocus();
    } else {
      showWindowFullscreen();
    }
  }

  /**
   * Creates the main frame according to the current configuration.
   */
  public App() {
    super();

    boolean startFullscreen = Configuration.get("START_MINIMIZED").toString()
        .equalsIgnoreCase("no");
    logger.debug("Memoranda version: {}", VERSION_INFO);

    // show the splash screen
    if (Configuration.get("SHOW_SPLASH").toString().equalsIgnoreCase("yes")) {
      splashFrame = new SplashFrame();
      splashFrame.show();
    }

    setLookAndFeel();
    setFirstDayOfWeek();
    EventsScheduler.init();

    // create the main frame
    mainAppFrame = new AppFrame();
    if (startFullscreen) {
      showWindowFullscreen();
    }

    // close the splash screen
    if (Configuration.get("SHOW_SPLASH").toString().equalsIgnoreCase("yes")) {
      splashFrame.dispose();
    }
  }

  private void setFirstDayOfWeek() {
    if (Configuration.get("FIRST_DAY_OF_WEEK").equals("")) {
      String firstDayOfWeek;
      if (Calendar.getInstance().getFirstDayOfWeek() == 2) {
        firstDayOfWeek = "mon";
      } else {
        firstDayOfWeek = "sun";
      }
      Configuration.put("FIRST_DAY_OF_WEEK", firstDayOfWeek);
      Configuration.saveConfig();
    }
  }

  private void setLookAndFeel() {
    try {
      switch (Configuration.get("LOOK_AND_FEEL").toString()) {
        case "system" -> UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        case "default" ->
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        default -> UIManager.setLookAndFeel(Configuration.get("LOOK_AND_FEEL").toString());
      }
    } catch (Exception e) {
      new ExceptionDialog(e,
          "Error when initializing a pluggable look-and-feel. Default LF will be used.",
          "Make sure that specified look-and-feel library classes are on the CLASSPATH.");
    }
  }

  void showWindowFullscreen() {
    /*
     * if (packFrame) { frame.pack(); } else { frame.validate(); }
     *
     * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
     *
     * Dimension frameSize = frame.getSize(); if (frameSize.height >
     * screenSize.height) { frameSize.height = screenSize.height; } if
     * (frameSize.width > screenSize.width) { frameSize.width =
     * screenSize.width; }
     *
     *
     * Make the window fullscreen - On Request of users This seems not to
     * work on sun's version 1.4.1_01 Works great with 1.4.2 !!! So update
     * your J2RE or J2SDK.
     */
    /* Used to maximize the screen if the JVM Version if 1.4 or higher */
    /* --------------------------------------------------------------- */
    double JVMVer =
        Double.parseDouble(System.getProperty("java.version").substring(0, 3));

    mainAppFrame.pack();
    if (JVMVer >= 1.4) {
      mainAppFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
    } else {
      mainAppFrame.setExtendedState(Frame.NORMAL);
    }
    /* --------------------------------------------------------------- */
    /* Added By Jeremy Whitlock (jcscoobyrs) 07-Nov-2003 at 15:54:24 */

    // Not needed ???
    mainAppFrame.setVisible(true);
    mainAppFrame.toFront();
    mainAppFrame.requestFocus();
  }

  public static void closeWindow() {
    if (mainAppFrame == null) {
      return;
    }
    mainAppFrame.dispose();
  }
}