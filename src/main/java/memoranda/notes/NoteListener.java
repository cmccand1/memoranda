package memoranda.notes;

public interface NoteListener {

  void noteChange(Note note, boolean toSaveCurrentNote);
}
