package memoranda.notes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import memoranda.date.CalendarDate;
import nu.xom.Attribute;
import nu.xom.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import memoranda.projects.ProjectManager;
import memoranda.projects.Project;

class NoteImplTest {

  Project testProject;
  Note testNote;

  @BeforeEach
  void setUp() {
    // Create a project with the project manager
    testProject = ProjectManager.createProject("test project", new CalendarDate(27, 11, 2024),
        new CalendarDate(28, 11, 2024));
    // Create a note with the project
    Element noteElement = new Element("note");
    Attribute idAttribute = new Attribute("refid", UUID.randomUUID().toString());
    Attribute titleAttribute = new Attribute("title", "test note");
    Attribute startDateAttribute = new Attribute("startDate", "27/11/2024");
    Attribute endDateAttribute = new Attribute("endDate", "28/11/2024");
    noteElement.addAttribute(idAttribute);
    noteElement.addAttribute(titleAttribute);
    noteElement.addAttribute(startDateAttribute);
    noteElement.addAttribute(endDateAttribute);

    testNote = new NoteImpl(noteElement, testProject);
  }

  @AfterEach
  void tearDown() {
    // remove the test note
    testNote = null;

    // remove the test project
    String id = testProject.getID();
    ProjectManager.removeProject(id);
    testProject = null;
  }

  @Test
  void getDate() {
  }

  @Test
  void getProject() {
    assertEquals(testProject, testNote.getProject());
  }

  @Test
  void getTitle() {
    assertEquals("test note", testNote.getTitle());
  }

  @Test
  void whenTitleIsNullThenGetTitleReturnsEmptyString() {
    Element noteElement = new Element("note");
    Note note = new NoteImpl(noteElement, testProject);
    assertEquals("", note.getTitle());
  }

  @Test
  void setTitleWhenAlreadyPresent() {
    String newTitle = "new title for this test";
    assertNotEquals(newTitle, testNote.getTitle());
    testNote.setTitle(newTitle);
    assertEquals(newTitle, testNote.getTitle());
  }

  @Test
  void setTitleWhenNotPresent() {
    Element noteElement = new Element("note");
    Note note = new NoteImpl(noteElement, testProject);
    String newTitle = "new title for this test";
    assertEquals("", note.getTitle());
    note.setTitle(newTitle);
    assertEquals(newTitle, note.getTitle());
  }

  @Test
  void gettingIdWhenNotPresentReturnsEmptyString(){
    Element noteElement = new Element("note");
    Note note = new NoteImpl(noteElement, testProject);
    assertEquals("", note.getId());
  }

  @Test
  void getIDReturnsIDAsUUIDString() {
    assertDoesNotThrow(() -> UUID.fromString(testNote.getId()));
  }

  @Test
  void setId() {
    String newId = UUID.randomUUID().toString();
    assertNotEquals(newId, testNote.getId());
    testNote.setId(newId);
    assertEquals(newId, testNote.getId());
  }

  @Test
  void setIdWhenNull() {
    Note note = new NoteImpl(new Element("note"), testProject);
    String newId = UUID.randomUUID().toString();
    assertEquals("", note.getId());
    note.setId(newId);
    assertEquals(newId, note.getId());
  }

  @Test
  void isMarked() {
    assertTrue(!testNote.isMarked());
  }

  @Test
  void setMarkOnUnmarkedNoteAddsAttribute() {
    assertTrue(!testNote.isMarked());
    testNote.setMark(true);
    assertTrue(testNote.isMarked());
  }

  @Test
  void setMarkOnMarkedNoteRemovesAttribute() {
    testNote.setMark(true);
    assertTrue(testNote.isMarked());
    testNote.setMark(false);
    assertTrue(!testNote.isMarked());
  }

  @Test
  void compareTo() {
    // create a second note whose date is after the first note

  }
}