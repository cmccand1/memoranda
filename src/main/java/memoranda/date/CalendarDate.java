/**
 * CalendarDate.java Created on 11.02.2003, 18:02:02 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.date;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import memoranda.util.Local;
import memoranda.util.Util;

public class CalendarDate {

  private final LocalDate date;

  /**
   * Creates a new instance of CalendarDate with the current date.
   */
  public CalendarDate() {
    date = LocalDate.now();
  }

  public CalendarDate(int day, int month, int year) {
    date = LocalDate.of(year, month, day);
  }

  private CalendarDate(Calendar cal) {
    Objects.requireNonNull(cal);
    date = LocalDate.of(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH)
    );
  }

  public CalendarDate(Date date) {
    Objects.requireNonNull(date);
    this.date = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public CalendarDate(String date) {
    Objects.requireNonNull(date);
    int[] dateParts = Util.parseDateStamp(date);
    this.date = LocalDate.of(
        dateParts[2],
        Month.of(dateParts[1]),
        dateParts[0]
    );

  }

  private CalendarDate(LocalDate localDate) {
    Objects.requireNonNull(localDate);
    date = localDate;
  }

  public static CalendarDate today() {
    return new CalendarDate();
  }

  public static CalendarDate yesterday() {
    return new CalendarDate(LocalDate.now().minusDays(1));
  }

  public static CalendarDate tomorrow() {
    return new CalendarDate(LocalDate.now().plusDays(1));
  }

  public static Calendar dateToCalendar(Date date) {
    Objects.requireNonNull(date);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  public static Calendar toCalendar(int day, int month, int year) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month);
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.getTime();
    return cal;
  }

  public static Date toDate(int day, int month, int year) {
    return Date.from(toCalendar(day, month, year).toInstant());
  }

  public Calendar getCalendar() {
    return toCalendar(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
  }

  public Date getDate() {
    return toDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
  }

  public int getDay() {
    return date.getDayOfMonth();
  }

  public int getMonth() {
    return date.getMonthValue();
  }

  public int getLengthOfMonth() {
    return date.lengthOfMonth();
  }

  public int getYear() {
    return date.getYear();
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null) {
      return false;
    } else if (object instanceof CalendarDate calendarDate) {
      return compareDates(calendarDate);
    } else if (object instanceof Calendar cal) {
      return compareDates(new CalendarDate(cal));
    } else if (object instanceof Date date) {
      return compareDates(new CalendarDate(date));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return date.hashCode();
  }

  private boolean compareDates(CalendarDate date) {
    return this.date.equals(date.date);
  }

  public boolean before(CalendarDate date) {
    if (date == null) {
      return true;
    }
    return this.date.isBefore(date.date);
  }

  public boolean after(CalendarDate date) {
    if (date == null) {
      return true;
    }
    return this.date.isAfter(date.date);
  }

  public boolean inPeriod(CalendarDate startDate, CalendarDate endDate) {
    return (after(startDate) && before(endDate)) || compareDates(startDate) || compareDates(endDate);
  }

  @Override
  public String toString() {
    return Util.getDateStamp(this);
  }

  public String getFullDateString() {
    DateTimeFormatter fullDateStringFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    return date.format(fullDateStringFormatter);
  }

  public String getMediumDateString() {
    DateTimeFormatter mediumDateStringFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
    return date.format(mediumDateStringFormatter);
  }

  public String getLongDateString() {
    DateTimeFormatter longDateStringFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    return date.format(longDateStringFormatter);
  }

  public String getShortDateString() {
    DateTimeFormatter shortDateStringFormatter = DateTimeFormatter.ofPattern("M/d/yy");
    return date.format(shortDateStringFormatter);
  }
}
