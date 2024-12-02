package memoranda.ui.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

public class CalendarPanel extends JPanel {
  private static final Logger logger = LoggerFactory.getLogger(CalendarPanel.class);

  private final int MIN_YEAR = 1980;
  private final int MAX_YEAR = 2999;

  CalendarDate _calendarPanelDate = CurrentDate.get();
  JToolBar navigationBar = new JToolBar();
  JPanel mntyPanel = new JPanel(new BorderLayout());
  JPanel navbPanel = new JPanel(new BorderLayout());
  JButton dayForwardButton = new JButton();
  JPanel dayForwardButtonPanel = new JPanel();
  JButton todayButton = new JButton();
  JPanel todayButtonPanel = new JPanel();
  JPanel dayBackButtonPanel = new JPanel();
  JButton dayBackButton = new JButton();
  JComboBox monthsComboBox = new JComboBox(Local.getMonthNames());
  BorderLayout borderLayout4 = new BorderLayout();
  public CalendarTable calendarTable = new CalendarTable(CurrentDate.get());
  JPanel calendarPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JSpinner yearSpinner = new JSpinner(
      new SpinnerNumberModel(calendarTable.get().getYear(), MIN_YEAR, MAX_YEAR, 1));
  JSpinner.NumberEditor yearSpinnerNumberEditor = new JSpinner.NumberEditor(yearSpinner, "####");

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

    monthsComboBox.setRequestFocusEnabled(false);
    monthsComboBox.setMaximumRowCount(12);
    monthsComboBox.setPreferredSize(new Dimension(50, 20));
    border1 = BorderFactory.createEmptyBorder(0, 0, 5, 0);
    border2 = BorderFactory.createEmptyBorder();
    this.setLayout(new BorderLayout());
    navigationBar.setFloatable(false);
    dayForwardButton.setAction(dayForwardAction);
    dayForwardButton.setMinimumSize(new Dimension(24, 24));
    dayForwardButton.setOpaque(false);
    dayForwardButton.setPreferredSize(new Dimension(24, 24));
    dayForwardButton.setRequestFocusEnabled(false);
    dayForwardButton.setBorderPainted(false);
    dayForwardButton.setFocusPainted(false);
    dayForwardButton.setIcon(
        new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/forward.png"))));
    dayForwardButton.setText("");
    dayForwardButton.setToolTipText(Local.getString("One day forward"));

    dayForwardButtonPanel.setAlignmentX((float) 0.0);
    dayForwardButtonPanel.setMinimumSize(new Dimension(40, 24));
    dayForwardButtonPanel.setOpaque(false);
    dayForwardButtonPanel.setPreferredSize(new Dimension(40, 24));

    todayButton.setAction(todayAction);
    todayButton.setMinimumSize(new Dimension(24, 24));
    todayButton.setOpaque(false);
    todayButton.setPreferredSize(new Dimension(24, 24));
    todayButton.setRequestFocusEnabled(false);
    todayButton.setBorderPainted(false);
    todayButton.setFocusPainted(false);
    todayButton.setIcon(
        new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/today.png"))));
    todayButton.setText("");
    todayButton.setToolTipText(Local.getString("To today"));

    dayBackButtonPanel.setAlignmentX((float) 1.5);
    dayBackButtonPanel.setMinimumSize(new Dimension(40, 24));
    dayBackButtonPanel.setOpaque(false);
    dayBackButtonPanel.setPreferredSize(new Dimension(40, 24));

    dayBackButton.setAction(dayBackAction);
    dayBackButton.setMinimumSize(new Dimension(24, 24));
    dayBackButton.setOpaque(false);
    dayBackButton.setPreferredSize(new Dimension(24, 24));
    dayBackButton.setRequestFocusEnabled(false);
    dayBackButton.setToolTipText("");
    dayBackButton.setBorderPainted(false);
    dayBackButton.setFocusPainted(false);
    dayBackButton.setIcon(
        new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource("/ui/icons/back.png"))));
    dayBackButton.setText("");
    dayBackButton.setToolTipText(Local.getString("One day back"));

    yearSpinner.setPreferredSize(new Dimension(70, 20));
    yearSpinner.setRequestFocusEnabled(false);
    yearSpinner.setEditor(yearSpinnerNumberEditor);
    navbPanel.setMinimumSize(new Dimension(202, 30));
    navbPanel.setOpaque(false);
    navbPanel.setPreferredSize(new Dimension(155, 30));
    calendarTable.getTableHeader().setFont(new java.awt.Font("Dialog", 1, 10));
    calendarTable.setFont(new java.awt.Font("Dialog", 0, 10));
    calendarTable.setGridColor(Color.lightGray);
    calendarPanel.setLayout(borderLayout5);
    todayButtonPanel.setMinimumSize(new Dimension(68, 24));
    todayButtonPanel.setOpaque(false);
    todayButtonPanel.setPreferredSize(new Dimension(51, 24));
    this.add(navigationBar, BorderLayout.NORTH);
    navigationBar.add(navbPanel, null);
    navbPanel.add(dayBackButtonPanel, BorderLayout.WEST);
    dayBackButtonPanel.add(dayBackButton, null);
    navbPanel.add(todayButtonPanel, BorderLayout.CENTER);
    todayButtonPanel.add(todayButton, null);
    navbPanel.add(dayForwardButtonPanel, BorderLayout.EAST);
    dayForwardButtonPanel.add(dayForwardButton, null);
    this.add(mntyPanel, BorderLayout.SOUTH);
    mntyPanel.add(monthsComboBox, BorderLayout.CENTER);
    mntyPanel.add(yearSpinner, BorderLayout.EAST);
    this.add(calendarPanel, BorderLayout.CENTER);
    calendarTable.getTableHeader().setPreferredSize(new Dimension(200, 15));
    calendarPanel.add(calendarTable.getTableHeader(), BorderLayout.NORTH);
    calendarPanel.add(calendarTable, BorderLayout.CENTER);
    calendarTable.addSelectionListener(
        e -> setCurrentDateDay(calendarTable.get(), calendarTable.get().getDay()));
    /*CurrentDate.addChangeListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        _date = CurrentDate.get();
        refreshView();
      }
    });*/
    monthsComboBox.setFont(new java.awt.Font("Dialog", Font.PLAIN, 11));

    monthsComboBox.addActionListener(this::monthsComboBox_actionPerformed);

    yearSpinner.addChangeListener(e -> yearSpin_actionPerformed());
    CurrentProject.addProjectListener(new ProjectListener() {
      public void projectChange(Project p, NoteList nl, TaskList tl, ResourcesList rl) {
      }

      public void projectWasChanged() {
        calendarTable.updateUI();
      }
    });

    refreshView();
    yearSpinner.setBorder(border2);

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
    monthsComboBox.setSelectedIndex(_calendarPanelDate.getMonth() - 1); // -1 because combo box is 0-based
    yearSpinner.setValue(_calendarPanelDate.getYear());
    ignoreChange = false;
  }

  void monthsComboBox_actionPerformed(ActionEvent e) {
    if (ignoreChange) {
      return;
    }
    // snap to the first day of the month to avoid problems with months of different length
    _calendarPanelDate = new CalendarDate(1, monthsComboBox.getSelectedIndex() + 1, _calendarPanelDate.getYear()); // +1 because combo box is 0-based
    calendarTable.set(_calendarPanelDate);
    notifyListeners();
  }

  void yearSpin_actionPerformed() {
    if (ignoreChange) {
      return;
    }
    _calendarPanelDate = new CalendarDate(_calendarPanelDate.getDay(), _calendarPanelDate.getMonth(),
        ((Integer) yearSpinner.getValue()));
    calendarTable.set(_calendarPanelDate);
    notifyListeners();
  }

  void dayBackButton_actionPerformed(ActionEvent e) {
    logger.debug("dayBackButton_actionPerformed");
    _calendarPanelDate = _calendarPanelDate.yesterday();
    refreshView();
    notifyListeners();
  }

  void todayButton_actionPerformed(ActionEvent e) {
    logger.debug("todayButton_actionPerformed");
    _calendarPanelDate = CalendarDate.today();
    refreshView();
    notifyListeners();
  }

  void dayForwardButton_actionPerformed(ActionEvent e) {
    logger.debug("dayForwardButton_actionPerformed");
    _calendarPanelDate = _calendarPanelDate.tomorrow();
    refreshView();
    notifyListeners();
  }
}