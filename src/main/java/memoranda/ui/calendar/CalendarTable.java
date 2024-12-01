/**
 * JNCalendar.java Created on 13.02.2003, 21:26:38 Alex Package: net.sf.memoranda.ui
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.ui.calendar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import memoranda.date.CalendarDate;
import memoranda.date.CurrentDate;
import memoranda.util.Configuration;
import memoranda.util.Local;

/**
 *
 */
public class CalendarTable extends JTable {

  private CalendarDate _date = null;
  private boolean ignoreChange = false;
  private List<ActionListener> selectionListeners = new ArrayList<>();
  CalendarDate startPeriod = null;
  CalendarDate endPeriod = null;
  public CalendarCellRenderer renderer = new CalendarCellRenderer();

  public CalendarTable() {
    this(CurrentDate.get());
  }

  public CalendarTable(CalendarDate date) {
    super();
    setCellSelectionEnabled(true);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    getTableHeader().setReorderingAllowed(false);
    getTableHeader().setResizingAllowed(false);
    set(date);

    final ListSelectionModel rowSM = getSelectionModel();
    final ListSelectionModel colSM = getColumnModel().getSelectionModel();
    ListSelectionListener lsl = e -> {
      if (e.getValueIsAdjusting()) {
        return;
      }
      if (ignoreChange) {
        return;
      }
      // get the selected row and column
      int row = getSelRow();
      int col = getSelCol();
      Object val = getModel().getValueAt(row, col);
      if (val != null) {
        // do nothing if selecting the same day
        if ((int) val == _date.getDay()) {
          return;
        }
        _date =
            new CalendarDate(
                (int) val,
                _date.getMonth(),
                _date.getYear());
        notifyListeners();
        doSelection();
      } else {
        // do nothing if selecting a day out of month
//        doSelection();
      }
    };
    rowSM.addListSelectionListener(lsl);
    colSM.addListSelectionListener(lsl);
  }

  int getSelRow() {
    return this.getSelectedRow();
  }

  int getSelCol() {
    return this.getSelectedColumn();
  }

  public CalendarTable(CalendarDate date, CalendarDate sd, CalendarDate ed) {
    this(date);
    setSelectablePeriod(sd, ed);
  }

  public void set(CalendarDate date) {
    _date = date;
    setCalendarParameters();
    ignoreChange = true;
    this.setModel(new CalendarModel());
    ignoreChange = false;
    doSelection();
  }

  public CalendarDate get() {
    return _date;
  }

  public void addSelectionListener(ActionListener al) {
    selectionListeners.add(al);
  }

  public void setSelectablePeriod(CalendarDate sd, CalendarDate ed) {
    startPeriod = sd;
    endPeriod = ed;
  }

  private void notifyListeners() {
    for (ActionListener selectionListener : selectionListeners) {
      selectionListener.actionPerformed(
          new ActionEvent(this, 0, "Calendar event"));
    }
  }

  @Override
  public TableCellRenderer getCellRenderer(int row, int col) {
    Object day = this.getModel().getValueAt(row, col);
    if (day != null) {
      renderer.setDate(
          new CalendarDate(
              (int) day,
              _date.getMonth(),
              _date.getYear()));
    } else {
      // day out of month
      renderer.setDate(null);
    }
    return renderer;
  }

  void doSelection() {
    ignoreChange = true;
    int selRow = getRow(_date.getDay());
    int selCol = getCol(_date.getDay());
    this.setRowSelectionInterval(selRow, selRow);
    this.setColumnSelectionInterval(selCol, selCol);
    ignoreChange = false;
  }

  int getRow(int day) {
    return ((day - 1) + firstDay) / 7;
  }

  int getCol(int day) {
    return ((day - 1) + firstDay) % 7;
  }

  int firstDay;
  int daysInMonth;

  void setCalendarParameters() {
    firstDay = _date.getFirstDayOfMonth();
    daysInMonth = _date.getLengthOfMonth();
  }

  /**
   * Inner class CalendarModel is a model for the CalendarTable.
   */
  public class CalendarModel extends AbstractTableModel {

    private final String[] dayNames = Local.getWeekdayNames();
    private final int ROWS = 6;
    private final int COLS = 7;


    public CalendarModel() {
      super();
    }

    @Override
    public int getColumnCount() {
      return COLS;
    }

    /**
     * Returns the value for the cell at columnIndex and rowIndex. If the day is not in the current
     * month, returns null.
     *
     * @param row the row whose value is to be queried
     * @param col the column whose value is to be queried
     * @return
     */
    @Override
    public Object getValueAt(int row, int col) {
      int pos = (row * 7 + (col + 1)) - firstDay;
      if ((pos > 0) && (pos <= _date.getLengthOfMonth())) {
        return pos;
      } else {
        return null;
      }
    }

    @Override
    public int getRowCount() {
      return ROWS;
    }

    @Override
    public String getColumnName(int col) {
      return dayNames[col];
    }
  }
}
