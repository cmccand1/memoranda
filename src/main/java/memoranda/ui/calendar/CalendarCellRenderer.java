/**
 * JNCalendarCellRenderer.java Created on 14.02.2003, 0:09:11 Alex Package: net.sf.memoranda.ui
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.ui.calendar;

import java.awt.Color;
import java.awt.Component;

import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;
import memoranda.projects.CurrentProject;
import memoranda.events.EventsManager;
import memoranda.tasks.Task;
import memoranda.date.CalendarDate;
import memoranda.ui.App;
import memoranda.ui.AppFrame;

/**
 *
 */
public class CalendarCellRenderer extends DefaultTableCellRenderer {

  private static final Color IGNORED_CAL_DATE_COLOR = new Color(0xE0, 0xE0, 0xE0);
  private static final Color OUT_OF_PROJECT_DATE_COLOR = new Color(0xF0, 0xF0, 0xF0);
  private static final Color TODAY_BORDER_COLOR = new Color(100, 100, 128);
  private static final Color SUN_FOREGROUND_COLOR = new Color(255, 0, 0);
  private static final Color TASK_PANEL_DATE_BG_COLOR = new Color(230, 255, 230);
  static final Color NOTES_PANEL_DATE_BG_COLOR = new Color(255, 245, 200);
  static final Color EVENTS_PANEL_DATE_BG_COLOR = new Color(255, 230, 230);

  private CalendarDate calendarDate = null;
  boolean disabled = false;
  ImageIcon eventIcon = new ImageIcon(
      Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/en.png")));
  Task task = null;

  public void setTask(Task _t) {
    task = _t;
  }

  public Task getTask() {
    return task;
  }

  public Component getTableCellRendererComponent(
      JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column) {

    JLabel dateLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected,
        hasFocus,
        row, column);
    String currentPanel = (App.getMainAppFrame()).workPanel.dailyItemsPanel.getCurrentPanel();

    // How to render ignored dates
    if (calendarDate == null) {
      dateLabel.setEnabled(false);
      dateLabel.setIcon(null);
      dateLabel.setBackground(IGNORED_CAL_DATE_COLOR);
      return dateLabel;
    }

    if (isSelected) {
      dateLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
      return dateLabel;
    }

    // How to render dates outside the current project period
    if (!isDateInCurrentProjectPeriod(calendarDate)) {
      dateLabel.setBackground(OUT_OF_PROJECT_DATE_COLOR);
    }

    // How to render the dates in the current project period
    dateLabel.setHorizontalTextPosition(2);
    dateLabel.setEnabled(true);

    if (calendarDate.equals(CalendarDate.today())) {
      dateLabel.setBorder(BorderFactory.createLineBorder(TODAY_BORDER_COLOR));
    }

    // How to render Sundays
    if (calendarDate.dayOf("Sunday")) {
      dateLabel.setForeground(SUN_FOREGROUND_COLOR);
    } else {
      dateLabel.setForeground(Color.BLACK);
    }

    // Set background color for dates with tasks, notes or events
    if (currentPanel == null) {
      dateLabel.setBackground(Color.WHITE);
    } else if (currentPanel.equals("TASKS") && (task != null) &&
        (calendarDate.inPeriod(task.getStartDate(), task.getEndDate()))) {
      dateLabel.setBackground(TASK_PANEL_DATE_BG_COLOR);
    } else if (currentPanel.equals("NOTES") &&
        CurrentProject.getNoteList().getNoteForDate(calendarDate) != null) {
      dateLabel.setBackground(NOTES_PANEL_DATE_BG_COLOR);
    } else if (currentPanel.equals("EVENTS") &&
        (!(EventsManager.getEventsForDate(calendarDate).isEmpty()))) {
      dateLabel.setBackground(EVENTS_PANEL_DATE_BG_COLOR);
    } else if (!isSelected) {
      dateLabel.setBackground(Color.WHITE);
    }

    // always display NREvents
    if (EventsManager.isNREventsForDate(calendarDate)) {
      dateLabel.setIcon(eventIcon);
    } else {
      dateLabel.setIcon(null);
    }
    return dateLabel;
  }

  public void setDate(CalendarDate date) {
    calendarDate = date;
  }

  /**
   * Whether the specified date is in the project period. The project period is defined by the start
   * and end dates of the current project.
   *
   * @param date
   * @return
   */
  private boolean isDateInCurrentProjectPeriod(CalendarDate date) {
    return date.inPeriod(CurrentProject.get().getStartDate(), CurrentProject.get().getEndDate());
  }
}
