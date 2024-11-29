package memoranda.ui.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

import memoranda.projects.CurrentProject;
import memoranda.notes.NoteList;
import memoranda.projects.Project;
import memoranda.projects.ProjectListener;
import memoranda.resources.ResourcesList;
import memoranda.tasks.TaskList;
import memoranda.date.CalendarDate;
import memoranda.date.CurrentDate;
import memoranda.ui.AppFrame;
import memoranda.ui.ExceptionDialog;
import memoranda.util.Local;

/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

public class CalendarPanel extends JPanel {

  private final int MIN_YEAR = 1980;
  private final int MAX_YEAR = 2999;

  CalendarDate _calendarPanelDate = CurrentDate.get();
  JToolBar navigationBar = new JToolBar();
  JPanel mntyPanel = new JPanel(new BorderLayout());
  JPanel navbPanel = new JPanel(new BorderLayout());
  JButton dayForwardB = new JButton();
  JPanel dayForwardBPanel = new JPanel();
  JButton todayB = new JButton();
  JPanel todayBPanel = new JPanel();
  JPanel dayBackBPanel = new JPanel();
  JButton dayBackB = new JButton();
  JComboBox monthsCB = new JComboBox(Local.getMonthNames());
  BorderLayout borderLayout4 = new BorderLayout();
  public CalendarTable calendarTable = new CalendarTable(CurrentDate.get());
  JPanel jnCalendarPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JSpinner yearSpin = new JSpinner(
      new SpinnerNumberModel(calendarTable.get().getYear(), MIN_YEAR, MAX_YEAR, 1));
  JSpinner.NumberEditor yearSpinner = new JSpinner.NumberEditor(yearSpin, "####");

  boolean ignoreChange = false;

  private Vector selectionListeners = new Vector();

  Border border1;
  Border border2;

  public CalendarPanel() {
    try {
      jbInit();
    } catch (Exception ex) {
      new ExceptionDialog(ex);
    }
  }

  public Action dayBackAction =
      new AbstractAction(
          "Go one day back",
          new ImageIcon(
              Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/back16.png")))) {
        public void actionPerformed(ActionEvent e) {
          dayBackButton_actionPerformed(e);
        }
      };

  public Action dayForwardAction =
      new AbstractAction(
          "Go one day forward",
          new ImageIcon(
              Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/forward16.png")))) {
        public void actionPerformed(ActionEvent e) {
          dayForwardButton_actionPerformed(e);
        }
      };

  public Action todayAction =
      new AbstractAction(
          "Go to today",
          new ImageIcon(
              Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/today16.png")))) {
        public void actionPerformed(ActionEvent e) {
          todayButton_actionPerformed(e);
        }
      };

  void jbInit() throws Exception {
    //dayBackAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, KeyEvent.ALT_MASK));
    //dayForwardAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, KeyEvent.ALT_MASK));
    todayAction.putValue(Action.ACCELERATOR_KEY,
        KeyStroke.getKeyStroke(KeyEvent.VK_HOME, KeyEvent.ALT_MASK));

    monthsCB.setRequestFocusEnabled(false);
    monthsCB.setMaximumRowCount(12);
    monthsCB.setPreferredSize(new Dimension(50, 20));
    border1 = BorderFactory.createEmptyBorder(0, 0, 5, 0);
    border2 = BorderFactory.createEmptyBorder();
    this.setLayout(new BorderLayout());
    navigationBar.setFloatable(false);
    dayForwardB.setAction(dayForwardAction);
    dayForwardB.setMinimumSize(new Dimension(24, 24));
    dayForwardB.setOpaque(false);
    dayForwardB.setPreferredSize(new Dimension(24, 24));
    dayForwardB.setRequestFocusEnabled(false);
    dayForwardB.setBorderPainted(false);
    dayForwardB.setFocusPainted(false);
    dayForwardB.setIcon(
        new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/forward.png"))));
    dayForwardB.setText("");
    dayForwardB.setToolTipText(Local.getString("One day forward"));

    dayForwardBPanel.setAlignmentX((float) 0.0);
    dayForwardBPanel.setMinimumSize(new Dimension(40, 24));
    dayForwardBPanel.setOpaque(false);
    dayForwardBPanel.setPreferredSize(new Dimension(40, 24));

    todayB.setAction(todayAction);
    todayB.setMinimumSize(new Dimension(24, 24));
    todayB.setOpaque(false);
    todayB.setPreferredSize(new Dimension(24, 24));
    todayB.setRequestFocusEnabled(false);
    todayB.setBorderPainted(false);
    todayB.setFocusPainted(false);
    todayB.setIcon(
        new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/today.png"))));
    todayB.setText("");
    todayB.setToolTipText(Local.getString("To today"));

    dayBackBPanel.setAlignmentX((float) 1.5);
    dayBackBPanel.setMinimumSize(new Dimension(40, 24));
    dayBackBPanel.setOpaque(false);
    dayBackBPanel.setPreferredSize(new Dimension(40, 24));

    dayBackB.setAction(dayBackAction);
    dayBackB.setMinimumSize(new Dimension(24, 24));
    dayBackB.setOpaque(false);
    dayBackB.setPreferredSize(new Dimension(24, 24));
    dayBackB.setRequestFocusEnabled(false);
    dayBackB.setToolTipText("");
    dayBackB.setBorderPainted(false);
    dayBackB.setFocusPainted(false);
    dayBackB.setIcon(
        new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/back.png"))));
    dayBackB.setText("");
    dayBackB.setToolTipText(Local.getString("One day back"));

    yearSpin.setPreferredSize(new Dimension(70, 20));
    yearSpin.setRequestFocusEnabled(false);
    yearSpin.setEditor(yearSpinner);
    navbPanel.setMinimumSize(new Dimension(202, 30));
    navbPanel.setOpaque(false);
    navbPanel.setPreferredSize(new Dimension(155, 30));
    calendarTable.getTableHeader().setFont(new java.awt.Font("Dialog", 1, 10));
    calendarTable.setFont(new java.awt.Font("Dialog", 0, 10));
    calendarTable.setGridColor(Color.lightGray);
    jnCalendarPanel.setLayout(borderLayout5);
    todayBPanel.setMinimumSize(new Dimension(68, 24));
    todayBPanel.setOpaque(false);
    todayBPanel.setPreferredSize(new Dimension(51, 24));
    this.add(navigationBar, BorderLayout.NORTH);
    navigationBar.add(navbPanel, null);
    navbPanel.add(dayBackBPanel, BorderLayout.WEST);
    dayBackBPanel.add(dayBackB, null);
    navbPanel.add(todayBPanel, BorderLayout.CENTER);
    todayBPanel.add(todayB, null);
    navbPanel.add(dayForwardBPanel, BorderLayout.EAST);
    dayForwardBPanel.add(dayForwardB, null);
    this.add(mntyPanel, BorderLayout.SOUTH);
    mntyPanel.add(monthsCB, BorderLayout.CENTER);
    mntyPanel.add(yearSpin, BorderLayout.EAST);
    this.add(jnCalendarPanel, BorderLayout.CENTER);
    calendarTable.getTableHeader().setPreferredSize(new Dimension(200, 15));
    jnCalendarPanel.add(calendarTable.getTableHeader(), BorderLayout.NORTH);
    jnCalendarPanel.add(calendarTable, BorderLayout.CENTER);
    calendarTable.addSelectionListener(
        e -> setCurrentDateDay(calendarTable.get(), calendarTable.get().getDay()));
    /*CurrentDate.addChangeListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        _date = CurrentDate.get();
        refreshView();
      }
    });*/
    monthsCB.setFont(new java.awt.Font("Dialog", 0, 11));

    monthsCB.addActionListener(this::monthsComboBox_actionPerformed);

    yearSpin.addChangeListener(e -> yearSpin_actionPerformed());
    CurrentProject.addProjectListener(new ProjectListener() {
      public void projectChange(Project p, NoteList nl, TaskList tl, ResourcesList rl) {
      }

      public void projectWasChanged() {
        calendarTable.updateUI();
      }
    });

    refreshView();
    yearSpin.setBorder(border2);

  }

  public void set(CalendarDate date) {
    _calendarPanelDate = date;
    refreshView();
  }

  public CalendarDate get() {
    return _calendarPanelDate;
  }

  public void addSelectionListener(ActionListener al) {
    selectionListeners.add(al);
  }

  private void notifyListeners() {
    for (Enumeration en = selectionListeners.elements(); en.hasMoreElements(); ) {
      ((ActionListener) en.nextElement()).actionPerformed(
          new ActionEvent(this, 0, "Calendar event"));
    }
  }

  private void setCurrentDateDay(CalendarDate dt, int d) {
    if (ignoreChange) {
      return;
    }
    if (_calendarPanelDate.equals(dt)) {
      return;
    }
    _calendarPanelDate = new CalendarDate(d, _calendarPanelDate.getMonth(), _calendarPanelDate.getYear());
    notifyListeners();
  }

  private void refreshView() {
    ignoreChange = true;
    calendarTable.set(_calendarPanelDate);
    monthsCB.setSelectedIndex(_calendarPanelDate.getMonth());
    yearSpin.setValue(_calendarPanelDate.getYear());
    ignoreChange = false;
  }

  void monthsComboBox_actionPerformed(ActionEvent e) {
    if (ignoreChange) {
      return;
    }
    System.out.println("selected index: " + monthsCB.getSelectedIndex());
    _calendarPanelDate = new CalendarDate(_calendarPanelDate.getDay(), monthsCB.getSelectedIndex(), _calendarPanelDate.getYear());
    calendarTable.set(_calendarPanelDate);
    notifyListeners();
  }

  void yearSpin_actionPerformed() {
    if (ignoreChange) {
      return;
    }
    _calendarPanelDate = new CalendarDate(_calendarPanelDate.getDay(), _calendarPanelDate.getMonth(),
        ((Integer) yearSpin.getValue()));
    calendarTable.set(_calendarPanelDate);
    notifyListeners();
  }

  void dayBackButton_actionPerformed(ActionEvent e) {
    _calendarPanelDate = _calendarPanelDate.yesterday();
    refreshView();
    notifyListeners();
  }

  void todayButton_actionPerformed(ActionEvent e) {
    _calendarPanelDate = CalendarDate.today();
    refreshView();
    notifyListeners();
  }

  void dayForwardButton_actionPerformed(ActionEvent e) {
    _calendarPanelDate = _calendarPanelDate.tomorrow();
    refreshView();
    notifyListeners();
  }
}