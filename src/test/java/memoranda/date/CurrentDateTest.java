package memoranda.date;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CurrentDateTest {

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getReturnsACalendarDateWithTheCurrentDate() {
    CalendarDate currentDate = CurrentDate.get();
    assertNotNull(currentDate);
    assertEquals(LocalDate.now().getDayOfMonth(), currentDate.getDay());
    assertEquals(LocalDate.now().getMonthValue(), currentDate.getMonth());
    assertEquals(LocalDate.now().getYear(), currentDate.getYear());
  }

  @Test
  void settingTheCurrentDateToTheCurrentDateDoesntAffectTheCurrentDate() {
    CalendarDate currentDate = CurrentDate.get();
    CurrentDate.set(currentDate);
    assertEquals(currentDate, CurrentDate.get());
  }

  @Test
  void settingTheCurrentDateToADifferentDateChangesTheCurrentDate() {
    CalendarDate currentDate = CurrentDate.get();
    CalendarDate newDate = new CalendarDate(1, 1, 2000);
    assertNotEquals(newDate, currentDate);
    CurrentDate.set(newDate);
    assertEquals(newDate, CurrentDate.get());
  }

  @Test
  void resetSetsTheCurrentDateToTheCurrentDate() {
    CurrentDate.reset(); // Annoying but necessary due to singleton nature of CurrentDate
    CalendarDate currentDate = CurrentDate.get();
    assertEquals(LocalDate.now().getDayOfMonth(), currentDate.getDay());
    assertEquals(LocalDate.now().getMonthValue(), currentDate.getMonth());
    assertEquals(LocalDate.now().getYear(), currentDate.getYear());

    CurrentDate.set(new CalendarDate(1, 1, 2000));
    assertEquals(new CalendarDate(1, 1, 2000), CurrentDate.get());

    CurrentDate.reset();
    assertEquals(LocalDate.now().getDayOfMonth(), CurrentDate.get().getDay());
    assertEquals(LocalDate.now().getMonthValue(), CurrentDate.get().getMonth());
    assertEquals(LocalDate.now().getYear(), CurrentDate.get().getYear());
  }
}