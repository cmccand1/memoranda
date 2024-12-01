package memoranda.date;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
  void testCalendarDateConstructor() {
    calendarDate = new CalendarDate();
    assertNotNull(calendarDate);
    assertEquals(LocalDate.now().getDayOfMonth(), calendarDate.getDay());
    assertEquals(LocalDate.now().getMonthValue(), calendarDate.getMonth());
    assertEquals(LocalDate.now().getYear(), calendarDate.getYear());
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
    CalendarDate calendarDate = new CalendarDate(new Date(LocalDate.now().getYear()-1900, LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth()));
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
    CalendarDate calendarDate = new CalendarDate("11/8/2024");
    assertNotNull(calendarDate);
    // Current format is day/month/year
    assertEquals(11, calendarDate.getDay());
    assertEquals(8, calendarDate.getMonth());
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
  void yesterdayReturnsACalendarDateRepresentingTheDayBefore() {
    CalendarDate today = CalendarDate.today();
    CalendarDate yesterday = today.yesterday();
    assertNotNull(yesterday);
    assertEquals(LocalDate.now().minusDays(1).getDayOfMonth(), yesterday.getDay());
    assertEquals(LocalDate.now().minusDays(1).getMonthValue(), yesterday.getMonth());
    assertEquals(LocalDate.now().minusDays(1).getYear(), yesterday.getYear());
  }

  @Test
  void tomorrowReturnsACalendarDateRepresentingTheDayBeforeADate() {
    CalendarDate today = CalendarDate.today();
    CalendarDate tomorrow = today.tomorrow();
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

  public static Stream<Arguments> equalsTestArgs() {
    return Stream.of(
        // boundary values
        Arguments.of(new CalendarDate(1, 1, 2021), new CalendarDate(1, 1, 2021), true), // same date
        Arguments.of(new CalendarDate(1, 1, 2021), new CalendarDate(1, 2, 2021), false),
        // different day
        Arguments.of(new CalendarDate(1, 1, 2021), new CalendarDate(2, 1, 2021), false),
        // different month
        Arguments.of(new CalendarDate(1, 1, 2021), new CalendarDate(1, 1, 2022), false),
        // different year
        // larger values
        Arguments.of(new CalendarDate(22, 2, 2021), new CalendarDate(22, 2, 2021), true),
        Arguments.of(new CalendarDate(1, 3, 2028), new CalendarDate(1, 3, 2028), true),
        // smaller values
        Arguments.of(new CalendarDate(31, 12, 2019), new CalendarDate(31, 12, 2019), true),
        Arguments.of(new CalendarDate(1, 1, 2020), new CalendarDate(1, 1, 2020), true)
    );
  }

  @ParameterizedTest
  @MethodSource("equalsTestArgs")
  void equalsTest(CalendarDate calendarDate1, CalendarDate calendarDate2, boolean expected) {
    assertEquals(expected, calendarDate1.equals(calendarDate2));
  }

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
        Arguments.of(null, true),
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
        Arguments.of(null, true),
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
    CalendarDate calendarDate = new CalendarDate(13, 1, 2021);
    assertEquals("13/1/2021", calendarDate.toString());
  }

  @Test
  void getFullDateString() {
    CalendarDate calendarDate = new CalendarDate(13, 1, 2021);
    String actualDateString = calendarDate.getFullDateString();
    System.out.println("Full Date String: " + actualDateString);

    // local date starts the month at 1
    LocalDate date = LocalDate.of(2021, 1, 13);
    // Define a custom DateTimeFormatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    // Format the date
    String expectedDateStrings = date.format(formatter);
    // Print the formatted date
    System.out.println("Formatted date: " + expectedDateStrings);

    assertEquals(expectedDateStrings, actualDateString);
  }

  @Test
  void getMediumDateString() {
    CalendarDate calendarDate = new CalendarDate(13, 1, 2021);
    String actualDateString = calendarDate.getMediumDateString();
    System.out.println("Medium Date String: " + actualDateString);

    // local date starts the month at 1
    LocalDate date = LocalDate.of(2021, 1, 13);
    // Define a custom DateTimeFormatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
    // Format the date
    String expectedDateString = date.format(formatter);
    // Print the formatted date
    System.out.println("Formatted date: " + expectedDateString);

    assertEquals(expectedDateString, actualDateString);
  }

  @Test
  void getLongDateString() {
    CalendarDate calendarDate = new CalendarDate(13, 1, 2021);
    String actualDateString = calendarDate.getLongDateString();
    System.out.println("Long Date String: " + actualDateString);

    // local date starts the month at 1
    LocalDate date = LocalDate.of(2021, 1, 13);
    // Define a custom DateTimeFormatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    // Format the date
    String expectedDateString = date.format(formatter);
    // Print the formatted date
    System.out.println("Formatted date: " + expectedDateString);

    assertEquals(expectedDateString, actualDateString);
  }

  @Test
  void getShortDateString() {
    CalendarDate calendarDate = new CalendarDate(13, 1, 2021);
    String actualDateString = calendarDate.getShortDateString();
    System.out.println("Short Date String: " + actualDateString);

    // local date starts the month at 1
    LocalDate date = LocalDate.of(2021, 1, 13);
    // Define a custom DateTimeFormatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
    // Format the date
    String expectedDateString = date.format(formatter);
    // Print the formatted date
    System.out.println("Formatted date: " + expectedDateString);

    assertEquals(expectedDateString, actualDateString);
  }

  @Test
  void dayOf() {
    CalendarDate calendarDate = new CalendarDate(1, 12, 2024);
    assertTrue(calendarDate.dayOf("sunday"));
    assertFalse(calendarDate.dayOf("MONDAY"));
  }
}