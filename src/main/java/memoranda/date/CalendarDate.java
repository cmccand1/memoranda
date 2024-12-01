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

import memoranda.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalendarDate {
  private static final Logger logger = LoggerFactory.getLogger(CalendarDate.class);

  private final LocalDate date;

  /**
   * Creates a new instance of CalendarDate with the current date.
   */
  public CalendarDate() {
    date = LocalDate.now();
  }

  /**
   * Creates a new instance of CalendarDate with the specified date.
   * @param day The day of the month.
   * @param month The month of the year.
   * @param year The year.
   */
  public CalendarDate(int day, int month, int year) {
    date = LocalDate.of(year, month, day);
  }

  /**
   * Creates a new instance of CalendarDate with the specified date.
   * @param cal The Calendar object representing the date.
   */
  public CalendarDate(Calendar cal) {
    Objects.requireNonNull(cal);
    date = LocalDate.of(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1, // Calendar months are 0-based
        cal.get(Calendar.DAY_OF_MONTH)
    );
  }

  /**
   * Creates a new instance of CalendarDate with the specified date.
   * @param date The Date object representing the date.
   */
  public CalendarDate(Date date) {
    Objects.requireNonNull(date);
    this.date = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  /**
   * Creates a new instance of CalendarDate with the specified date.
   * @param date The date in the format "dd/mm/yyyy".
   */
  public CalendarDate(String date) {
    Objects.requireNonNull(date);
    int[] dateParts = Util.parseDateStamp(date);
    this.date = LocalDate.of(
        dateParts[2],
        Month.of(dateParts[1]),
        dateParts[0]
    );
  }

  /**
   * Creates a new instance of CalendarDate with the specified date.
   * @param localDate The LocalDate object representing the date.
   */
  private CalendarDate(LocalDate localDate) {
    Objects.requireNonNull(localDate);
    date = localDate;
  }

  /**
   * A copy constructor for CalendarDate. Creates a new instance of CalendarDate with the same date as the specified CalendarDate.
   * @param calendarDate The CalendarDate object representing the date.
   */
  public CalendarDate(CalendarDate calendarDate) {
    Objects.requireNonNull(calendarDate);
    date = LocalDate.of(
        calendarDate.getYear(),
        calendarDate.getMonth(),
        calendarDate.getDay()
    );
  }

  /**
   * Returns a new instance of CalendarDate with the current date.
   * @return
   */
  public static CalendarDate today() {
    return new CalendarDate();
  }

  /**
   * Returns a new instance of CalendarDate with the date of yesterday w.r.t. this instance.
   * @return
   */
  public CalendarDate yesterday() {
    return new CalendarDate(date.minusDays(1));
  }

  /**
   * Returns a new instance of CalendarDate with the date of tomorrow w.r.t. this instance.
   * @return
   */
  public CalendarDate tomorrow() {
    return new CalendarDate(date.plusDays(1));
  }

  /**
   * Converts a Date object to a Calendar object.
   * @param date The Date object to convert.
   * @return The Calendar object representing the date.
   */
  public static Calendar dateToCalendar(Date date) {
    Objects.requireNonNull(date);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  /**
   * Creates a Calendar with the specified date.
   * @param day The day of the month.
   * @param month The month of the year.
   * @param year The year.
   * @return The Calendar object representing the date.
   */
  public static Calendar toCalendar(int day, int month, int year) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month);
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.getTime();
    return cal;
  }

  /**
   * Creates a Date object with the specified date.
   * @param day The day of the month.
   * @param month The month of the year.
   * @param year The year.
   * @return The Date object representing the date.
   */
  public static Date toDate(int day, int month, int year) {
    return Date.from(toCalendar(day, month, year).toInstant());
  }

  /**
   * Returns this CalendarDate as a Calendar object.
   * @return The Calendar object representing the date.
   */
  public Calendar getCalendar() {
    return toCalendar(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
  }

  /**
   * Returns this CalendarDate as a Date object.
   * @return The Date object representing the date.
   */
  public Date getDate() {
    return toDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
  }

  /**
   * Returns the day of the month.
   * @return The day of the month.
   */
  public int getDay() {
    return date.getDayOfMonth();
  }

  /**
   * Returns the month of the year.
   * @return The month of the year.
   */
  public int getMonth() {
    return date.getMonthValue();
  }

  /**
   * Returns the length of the month (between 1 and 31).
   * @return The length of the month.
   */
  public int getLengthOfMonth() {
    return date.lengthOfMonth();
  }

  /**
   * Returns the integer representation of the first day of the week (1 = Monday, 7 = Sunday).
   * @return An integer between 1 (Monday) and 7 (Sunday).
   */
  public int getFirstDayOfMonth() {
    return date.withDayOfMonth(1).getDayOfWeek().getValue();
  }

  /**
   * Returns the year number.
   * @return An integer representing the year.
   */
  public int getYear() {
    return date.getYear();
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object instanceof CalendarDate calendarDate) {
      return areDatesEqual(calendarDate);
    } else if (object instanceof Calendar cal) {
      return areDatesEqual(new CalendarDate(cal));
    } else if (object instanceof Date date) {
      return areDatesEqual(new CalendarDate(date));
    }
    return false;
  }

  /**
   * Returns the hash code of this CalendarDate.
   * @return The hash code of this CalendarDate.
   */
  @Override
  public int hashCode() {
    return date.hashCode();
  }

  /**
   * A helper method used by the equals method to compare two CalendarDate objects.
   * @param date The CalendarDate object to compare.
   * @return True if the dates are equal, false otherwise.
   */
  private boolean areDatesEqual(CalendarDate date) {
    return this.date.equals(date.date);
  }

  /**
   * Returns true if the day of the week is the specified day, ignoring case.
   * @param dayOfWeek A string representing the day of the week.
   * @return True if the day of the week is the specified day, false otherwise.
   */
  public boolean dayOf(String dayOfWeek) {
    return date.getDayOfWeek().toString().equalsIgnoreCase(dayOfWeek);
  }

  /**
   * Returns true if this date is before the specified date.
   * @param date The date to compare.
   * @return True if this date is before the specified date, false otherwise.
   */
  public boolean before(CalendarDate date) {
    if (date == null) {
      return true;
    }
    return this.date.isBefore(date.date);
  }

  /**
   * Returns true if this date is after the specified date.
   * @param date The date to compare.
   * @return True if this date is after the specified date, false otherwise.
   */
  public boolean after(CalendarDate date) {
    if (date == null) {
      return true;
    }
    return this.date.isAfter(date.date);
  }

  /**
   * Returns true if this date falls within the specified period (inclusive).
   * @param startDate The start date of the period.
   * @param endDate The end date of the period.
   * @return True if this date falls within the specified period, false otherwise.
   */
  public boolean inPeriod(CalendarDate startDate, CalendarDate endDate) {
    return (after(startDate) && before(endDate)) || equals(startDate) || equals(
        endDate);
  }

  /**
   * Returns the string representation of this CalendarDate.
   * @return The string representation of this CalendarDate.
   */
  @Override
  public String toString() {
    return Util.getDateStamp(this);
  }

  /**
   * Returns the full date string representation of this CalendarDate.
   * The format is "Day of the week, Month Day, Year".
   * @return The full date string representation of this CalendarDate.
   */
  public String getFullDateString() {
    DateTimeFormatter fullDateStringFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    return date.format(fullDateStringFormatter);
  }

  /**
   * Returns the medium date string representation of this CalendarDate.
   * The format is "Month Day, Year".
   * @return The medium date string representation of this CalendarDate.
   */
  public String getMediumDateString() {
    DateTimeFormatter mediumDateStringFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
    return date.format(mediumDateStringFormatter);
  }

  /**
   * Returns the long date string representation of this CalendarDate.
   * The format is "(Full) Month Day, Year".
   * @return The long date string representation of this CalendarDate.
   */
  public String getLongDateString() {
    DateTimeFormatter longDateStringFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    return date.format(longDateStringFormatter);
  }

  /**
   * Returns the short date string representation of this CalendarDate.
   * The format is "M/d/yy".
   * @return The short date string representation of this CalendarDate.
   */
  public String getShortDateString() {
    DateTimeFormatter shortDateStringFormatter = DateTimeFormatter.ofPattern("M/d/yy");
    return date.format(shortDateStringFormatter);
  }
}
