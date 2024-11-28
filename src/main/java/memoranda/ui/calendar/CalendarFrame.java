package memoranda.ui.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.border.Border;

import memoranda.ui.ExceptionDialog;
import memoranda.util.Local;

public class CalendarFrame extends JInternalFrame {

  public CalendarPanel cal = new CalendarPanel();
  Border border1;

  public CalendarFrame() {
    try {
      initCalendarFrame();
    } catch (Exception e) {
      new ExceptionDialog(e);
    }

  }

  private void initCalendarFrame() throws Exception {
    border1 = BorderFactory.createLineBorder(Color.gray, 1);
    this.setClosable(true);
    this.setTitle(Local.getString("Select date"));
    this.setBorder(border1);
    //this.setPreferredSize(new Dimension(200, 200));
    this.setToolTipText("");
    cal.setPreferredSize(new Dimension(this.getContentPane().getWidth(),
        this.getContentPane().getHeight()));
    this.getContentPane().add(cal, BorderLayout.CENTER);
  }
}