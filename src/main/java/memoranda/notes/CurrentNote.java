package memoranda.notes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class CurrentNote {

  private static Note currentNote = null;
  private static List<NoteListener> noteListeners = new ArrayList<>();

  public static Note get() {
    return currentNote;
  }

  public static void set(Note note, boolean toSaveCurrentNote) {
    noteChanged(note, toSaveCurrentNote);
    currentNote = note;
  }

  public static void reset() {
//    	 set toSave to true to mimic status quo behaviour only. the appropriate setting could be false
    set(null, true);
  }

  public static void addNoteListener(NoteListener nl) {
    noteListeners.add(nl);
  }

  public static Collection getChangeListeners() {
    return noteListeners;
  }

  private static void noteChanged(Note note, boolean toSaveCurrentNote) {
    for (NoteListener noteListener : noteListeners) {
      (noteListener).noteChange(note, toSaveCurrentNote);
    }
  }
}
