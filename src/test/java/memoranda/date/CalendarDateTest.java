package memoranda.date;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CalendarDateTest {

  CalendarDate calendarDate;

  @BeforeEach
  void setUp() {

  }

  @AfterEach
  void tearDown() {
    calendarDate = null;
  }

  @Test
  void testCalendarDateConstructorWithIntegers() {
    calendarDate = new CalendarDate(1, 1, 2021);
    assertEquals(1, calendarDate.getDay());
    assertEquals(1, calendarDate.getMonth());
    assertEquals(2021, calendarDate.getYear());
  }

  @Test
  void testCalendarDateConstructorWithDate() {
    CalendarDate calendarDate = new CalendarDate(new Date());
    assertNotNull(calendarDate);
    assertEquals(LocalDate.now().getDayOfMonth(), calendarDate.getDay());
    assertEquals(LocalDate.now().getMonthValue(), calendarDate.getMonth());
    assertEquals(LocalDate.now().getYear(), calendarDate.getYear());
  }

  @Test
  void testCalendarDateConstructorWithCalendar() {
    CalendarDate calendarDate = new CalendarDate(Calendar.getInstance());
    assertNotNull(calendarDate);
    assertEquals(LocalDate.now().getDayOfMonth(), calendarDate.getDay());
    assertEquals(LocalDate.now().getMonthValue(), calendarDate.getMonth());
    assertEquals(LocalDate.now().getYear(), calendarDate.getYear());
  }

  @Test
  void testCalendarDateConstructorWithString() {
    CalendarDate calendarDate = new CalendarDate("11/28/2024");
    assertNotNull(calendarDate);
    // Current format is day/month/year
    assertEquals(11, calendarDate.getDay());
    assertEquals(28, calendarDate.getMonth());
    assertEquals(2024, calendarDate.getYear());
  }

  @Test
  void dateToCalendarTakesADateAndReturnsACalendarWithThatDate() {
    // takes in a Date object and returns a Calendar object
    Date date = new Date();
    Calendar calendar = CalendarDate.dateToCalendar(date);
    assertNotNull(calendar);
    assertEquals(date, calendar.getTime());
  }

  @Test
  void toCalendarTakesIntegersAndReturnsACalendarWithThatDate() {
    Calendar calendar = CalendarDate.toCalendar(1, 1, 2021);
    assertNotNull(calendar);
    assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(1, calendar.get(Calendar.MONTH));
    assertEquals(2021, calendar.get(Calendar.YEAR));
  }

  @Test
  void toDateTakesIntegersAndReturnsADateWithThatDate() {
    Date date = CalendarDate.toDate(1, 1, 2021);
    assertNotNull(date);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(1, calendar.get(Calendar.MONTH));
    assertEquals(2021, calendar.get(Calendar.YEAR));
  }

  @Test
  void todayReturnsACalendarDateRepresentingTodaysDate() {
    CalendarDate today = CalendarDate.today();
    assertNotNull(today);
    assertEquals(LocalDate.now().getDayOfMonth(), today.getDay());
    assertEquals(LocalDate.now().getMonthValue(), today.getMonth());
    assertEquals(LocalDate.now().getYear(), today.getYear());
  }

  @Test
  void yesterdayReturnsACalendarDateRepresentingYesterdaysDate() {
    CalendarDate yesterday = CalendarDate.yesterday();
    assertNotNull(yesterday);
    assertEquals(LocalDate.now().minusDays(1).getDayOfMonth(), yesterday.getDay());
    assertEquals(LocalDate.now().minusDays(1).getMonthValue(), yesterday.getMonth());
    assertEquals(LocalDate.now().minusDays(1).getYear(), yesterday.getYear());
  }

  @Test
  void tomorrowReturnsACalendarDateRepresentingTomorrowsDate() {
    CalendarDate tomorrow = CalendarDate.tomorrow();
    assertNotNull(tomorrow);
    assertEquals(LocalDate.now().plusDays(1).getDayOfMonth(), tomorrow.getDay());
    assertEquals(LocalDate.now().plusDays(1).getMonthValue(), tomorrow.getMonth());
    assertEquals(LocalDate.now().plusDays(1).getYear(), tomorrow.getYear());
  }

  @Test
  void getCalendarReturnsACalendarObjectWithTheDate() {
    CalendarDate calendarDate = new CalendarDate(1, 1, 2021);

    Calendar calendar = calendarDate.getCalendar();
    assertNotNull(calendar);
    assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(1, calendar.get(Calendar.MONTH));
    assertEquals(2021, calendar.get(Calendar.YEAR));
  }

  @Test
  void getDateReturnsADateObjectWithTheDate() {
    CalendarDate calendarDate = new CalendarDate(1, 1, 2021);

    Date date = calendarDate.getDate();
    assertNotNull(date);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(1, calendar.get(Calendar.MONTH));
    assertEquals(2021, calendar.get(Calendar.YEAR));
  }

  @Test
  void getDayReturnsTheDay() {
    CalendarDate calendarDate = new CalendarDate(1, 1, 2021);
    assertEquals(1, calendarDate.getDay());
  }

  @Test
  void getMonthReturnsTheMonth() {
    CalendarDate calendarDate = new CalendarDate(1, 1, 2021);
    assertEquals(1, calendarDate.getMonth());
  }

  @Test
  void getYearReturnsTheYear() {
    CalendarDate calendarDate = new CalendarDate(1, 1, 2021);
    assertEquals(2021, calendarDate.getYear());
  }

  @Test
  void equalsReturnsTrueIfTheDatesAreEqual() {
    CalendarDate calendarDate1 = new CalendarDate(1, 1, 2021);
    CalendarDate calendarDate2 = new CalendarDate(1, 1, 2021);
    assertTrue(calendarDate1.equals(calendarDate2));
  }

  @Test
  void equalsReturnsFalseIfTheDatesAreNotEqual() {
    CalendarDate calendarDate1 = new CalendarDate(1, 1, 2021);
    CalendarDate calendarDate2 = new CalendarDate(1, 2, 2021);
    assertFalse(calendarDate1.equals(calendarDate2));
  }

  // test that equals obeys the contract

  @Test
  void equalsIsReflexive() {
    CalendarDate calendarDate = new CalendarDate(1, 1, 2021);
    assertTrue(calendarDate.equals(calendarDate));
  }
  @Test
  void equalsIsSymmetric() {
    CalendarDate calendarDate1 = new CalendarDate(1, 1, 2021);
    CalendarDate calendarDate2 = new CalendarDate(1, 1, 2021);
    assertTrue(calendarDate1.equals(calendarDate2));
    assertTrue(calendarDate2.equals(calendarDate1));
  }

  @Test
  void equalsIsTransitive() {
    CalendarDate calendarDate1 = new CalendarDate(1, 1, 2021);
    CalendarDate calendarDate2 = new CalendarDate(1, 1, 2021);
    CalendarDate calendarDate3 = new CalendarDate(1, 1, 2021);
    assertTrue(calendarDate1.equals(calendarDate2));
    assertTrue(calendarDate2.equals(calendarDate3));
    assertTrue(calendarDate1.equals(calendarDate3));
  }

  @Test
  void equalsObeysNonNullity() {
    CalendarDate calendarDate = new CalendarDate(1, 1, 2021);
    assertFalse(calendarDate.equals(null));
  }

  public static Stream<Arguments> beforeTestArgs() {
    return Stream.of(
        // boundary values
        Arguments.of(new CalendarDate(1, 2, 2021), true), // next day
        Arguments.of(new CalendarDate(1, 1, 2021), false), // same day
        Arguments.of(new CalendarDate(31, 12, 2020), false), // previous day
        // larger values
        Arguments.of(new CalendarDate(22, 2, 2021), true),
        Arguments.of(new CalendarDate(1, 3, 2028), true),
        // smaller values
        Arguments.of(new CalendarDate(31, 12, 2019), false),
        Arguments.of(new CalendarDate(1, 1, 2020), false)
    );
  }

  @ParameterizedTest
  @MethodSource("beforeTestArgs")
  void beforeTest(CalendarDate testDate, boolean expected) {
    CalendarDate beforeDate = new CalendarDate(1, 1, 2021);
    assertEquals(expected, beforeDate.before(testDate));
  }

  public static Stream<Arguments> afterTestArgs() {
    return Stream.of(
        // boundary values
        Arguments.of(new CalendarDate(1, 2, 2021), false), // next day
        Arguments.of(new CalendarDate(1, 1, 2021), false), // same day
        Arguments.of(new CalendarDate(31, 12, 2020), true), // previous day
        // larger values
        Arguments.of(new CalendarDate(22, 2, 2021), false),
        Arguments.of(new CalendarDate(1, 3, 2028), false),
        // smaller values
        Arguments.of(new CalendarDate(31, 12, 2019), true),
        Arguments.of(new CalendarDate(1, 1, 2020), true)
    );
  }

  @ParameterizedTest
  @MethodSource("afterTestArgs")
  void afterTest(CalendarDate testDate, boolean expected) {
    CalendarDate afterDate = new CalendarDate(1, 1, 2021);
    assertEquals(expected, afterDate.after(testDate));
  }

  public static Stream<Arguments> inPeriodTestArgs() {
    return Stream.of(
        // boundary values
        Arguments.of(new CalendarDate(1, 1, 2021), true), // start date
        Arguments.of(new CalendarDate(31, 12, 2020), false), // day before start date
        Arguments.of(new CalendarDate(31, 12, 2021), true), // end date
        Arguments.of(new CalendarDate(1, 1, 2022), false), // day after end date
        // other in period
        Arguments.of(new CalendarDate(22, 2, 2021), true),
        Arguments.of(new CalendarDate(1, 9, 2021), true),
        // long before
        Arguments.of(new CalendarDate(1, 1, 2020), false),
        Arguments.of(new CalendarDate(1, 1, 2011), false),
        // long after
        Arguments.of(new CalendarDate(1, 1, 2022), false),
        Arguments.of(new CalendarDate(1, 1, 2030), false)
    );
  }

  @ParameterizedTest
  @MethodSource("inPeriodTestArgs")
  void inPeriodTest(CalendarDate testDate, boolean expected) {
    CalendarDate startDate = new CalendarDate(1, 1, 2021);
    CalendarDate endDate = new CalendarDate(31, 12, 2021);
    assertEquals(expected, testDate.inPeriod(startDate, endDate));
  }


  @Test
  void testToString() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  void getFullDateString() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  void getMediumDateString() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  void getLongDateString() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  void getShortDateString() {
    throw new RuntimeException("Test not implemented");
  }
}