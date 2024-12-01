/**
 * CurrentDate.java Created on 13.02.2003, 2:11:03 Alex Package: net.sf.memoranda.date
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.date;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 *
 */
public class CurrentDate {

  private static CalendarDate _date = new CalendarDate();
  private static List<DateListener> dateListeners = new ArrayList<>();

  public static CalendarDate get() {
    return _date;
  }

  public static void set(CalendarDate date) {
    Objects.requireNonNull(date);
    if (date.equals(_date)) {
      return;
    }
    _date = date;
    dateChanged(date);
  }

  public static void reset() {
    set(new CalendarDate());
  }

  public static void addDateListener(DateListener dl) {
    dateListeners.add(dl);
  }

  public static Collection getChangeListeners() {
    return dateListeners;
  }

  private static void dateChanged(CalendarDate date) {
    for (DateListener dateListener : dateListeners) {
      dateListener.dateChange(date);
    }
  }
}
