/**
 * Util.java Created on 11.02.2003, 23:59:21 Alex Package: net.sf.memoranda.util
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda team:
 * http://memoranda.sf.net
 */
package memoranda.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import java.util.UUID;
import javax.swing.JFileChooser;

import memoranda.date.CalendarDate;
import memoranda.storage.CurrentStorage;
import memoranda.ui.App;
import memoranda.ui.AppFrame;
import memoranda.ui.ExceptionDialog;

/**
 *
 */
public class Util {

  static long seed = 0;

  /**
   * Generates a unique identifier in the form of a UUID string.
   *
   * @return a UUID string
   */
  public static String generateId() {
    return UUID.randomUUID().toString();
  }

  public static String getDateStamp(Calendar cal) {
    return cal.get(Calendar.DAY_OF_MONTH)
        + "/"
        + (cal.get(Calendar.MONTH))
        + "/"
        + new Integer(cal.get(Calendar.YEAR)).toString();

  }

  public static String getDateStamp(CalendarDate date) {
    return Util.getDateStamp(date.getCalendar());
  }

  public static int[] parseDateStamp(String s) {
    s = s.trim();
    int i1 = s.indexOf("/");
    int i2 = s.indexOf("/", i1 + 1);
    int[] date = new int[3];
    date[0] = Integer.parseInt(s.substring(0, i1));
    date[1] = Integer.parseInt(s.substring(i1 + 1, i2));
    date[2] = Integer.parseInt(s.substring(i2 + 1));
    return date;
  }

  public static String getEnvDir() {
    // Changed static building of getEnvDir
    // Now system-related path-separator is used
    String p = System.getProperty("user.home") + File.separator
        + ".jnotes2" + File.separator;
    if (new File(p).isDirectory()) {
      return p;
    }
    return System.getProperty("user.home") + File.separator
        + ".memoranda" + File.separator;
  }

  public static String getCDATA(String s) {
    return "<![CDATA[" + s + "]]>";
  }

  public static void runBrowser(String url) {
    if (!checkBrowser()) {
      return;
    }
    String commandLine = MimeTypesList.getAppList().getBrowserExec() + " " + url;
    System.out.println("Run: " + commandLine);
    try {
      /*DEBUG*/
      Runtime.getRuntime().exec(commandLine);
    } catch (Exception ex) {
      new ExceptionDialog(ex,
          "Failed to run an external web-browser application with commandline<br><code>"
              + commandLine + "</code>", "Check the application path and command line parameters " +
          "(File-&gt;Preferences-&gt;Resource types).");
    }
  }

  public static boolean checkBrowser() {
    AppList appList = MimeTypesList.getAppList();
    String bpath = appList.getBrowserExec();
    if (bpath != null) {
      if (new File(bpath).isFile()) {
        return true;
      }
    }
    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);
    chooser.setDialogTitle(Local.getString("Select the web-browser executable"));
    chooser.setAcceptAllFileFilterUsed(true);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        /*java.io.File lastSel = (java.io.File) Context.get("LAST_SELECTED_RESOURCE_FILE");
        if (lastSel != null)
            chooser.setCurrentDirectory(lastSel);*/
    if (chooser.showOpenDialog(App.getMainAppFrame()) != JFileChooser.APPROVE_OPTION) {
      return false;
    }
    appList.setBrowserExec(chooser.getSelectedFile().getPath());
    CurrentStorage.get().storeMimeTypesList();
    return true;
  }

  public static String getHoursFromMillis(long ms) {
    double numSeconds = (((double) ms) / 1000d);
    return String.valueOf(numSeconds / 3600);
  }

  public static long getMillisFromHours(String hours) {
    try {
      double numHours = Double.parseDouble(hours);
      double millisDouble = (numHours * 3600 * 1000);
      return (long) millisDouble;
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  static Set tempFiles = new HashSet();

  static {
    AppFrame.addExitListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
        for (Iterator i = tempFiles.iterator(); i.hasNext(); ) {
          ((File) i.next()).delete();
        }
      }
    });
  }

  public static File getTempFile() throws IOException {
    File f = File.createTempFile("tmp", ".html", null);
    tempFiles.add(f);
    return f;
  }
}
