/**
 * Storage.java Created on 12.02.2003, 0:21:40 Alex Package: net.sf.memoranda.util
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import memoranda.events.*;
import memoranda.notes.*;
import memoranda.projects.*;
import memoranda.resources.*;
import memoranda.tasks.*;
import memoranda.date.CalendarDate;
import memoranda.ui.ExceptionDialog;
import memoranda.ui.htmleditor.AltHTMLWriter;
import memoranda.util.Configuration;
import memoranda.util.Context;
import memoranda.util.MimeTypesList;
import memoranda.util.Util;
import nu.xom.Builder;
import nu.xom.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class FileStorage implements Storage {

  private static final Logger logger = LoggerFactory.getLogger(FileStorage.class);

  public static String JN_DOCPATH = Util.getEnvDir();
  private HTMLEditorKit editorKit = new HTMLEditorKit();

  public FileStorage() {
        /*The 'MEMORANDA_HOME' key is an undocumented feature for 
          hacking the default location (Util.getEnvDir()) of the memoranda 
          storage dir. Note that memoranda.config file is always placed at fixed 
          location (Util.getEnvDir()) anyway */
    String memorandaHomeDir = (String) Configuration.get("MEMORANDA_HOME");
    if (!memorandaHomeDir.isEmpty()) {
      JN_DOCPATH = memorandaHomeDir;
      logger.debug("Memoranda storage path set to: {}", JN_DOCPATH);
    }
  }

  public static void saveDocument(Document doc, String filePath) {
    /**
     * @todo: Configurable parameters
     */
    try {
      /*The XOM bug: reserved characters are not escaped*/
      //Serializer serializer = new Serializer(new FileOutputStream(filePath), "UTF-8");
      //serializer.write(doc);

      OutputStreamWriter fw =
          new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
      fw.write(doc.toXML());
      fw.flush();
      fw.close();
    } catch (IOException ex) {
      new ExceptionDialog(
          ex,
          "Failed to write a document to " + filePath,
          "");
    }
  }

  public static Document openDocument(InputStream in) throws Exception {
    Builder builder = new Builder();
    return builder.build(new InputStreamReader(in, "UTF-8"));
  }

  public static Document openDocument(String filePath) {
    try {
      return openDocument(new FileInputStream(filePath));
    } catch (Exception ex) {
      new ExceptionDialog(
          ex,
          "Failed to read a document from " + filePath,
          "");
    }
    return null;
  }

  public static boolean documentExists(String filePath) {
    return new File(filePath).exists();
  }

  /**
   * @see Storage#storeNote(memoranda.Note)
   */
  public void storeNote(Note note, javax.swing.text.Document doc) {
    String filename =
        JN_DOCPATH + note.getProject().getID() + File.separator;
    doc.putProperty(
        javax.swing.text.Document.TitleProperty,
        note.getTitle());
    CalendarDate d = note.getDate();

    filename += note.getId();//d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
    logger.debug("Save note: {}", filename);

    try {
      OutputStreamWriter fw =
          new OutputStreamWriter(new FileOutputStream(filename), "UTF-8");
      AltHTMLWriter writer = new AltHTMLWriter(fw, (HTMLDocument) doc);
      writer.write();
      fw.flush();
      fw.close();
      //editorKit.write(new FileOutputStream(filename), doc, 0, doc.getLength());
      //editorKit.write(fw, doc, 0, doc.getLength());
    } catch (Exception ex) {
      new ExceptionDialog(
          ex,
          "Failed to write a document to " + filename,
          "");
    }
        /*String filename = JN_DOCPATH + note.getProject().getID() + "/";
        doc.putProperty(javax.swing.text.Document.TitleProperty, note.getTitle());
        CalendarDate d = note.getDate();
        filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        try {
            long t1 = new java.util.Date().getTime();
            FileOutputStream ostream = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(ostream);
        
            oos.writeObject((HTMLDocument)doc);
        
            oos.flush();
            oos.close();
            ostream.close();
            long t2 = new java.util.Date().getTime();
            System.out.println(filename+" save:"+ (t2-t1) );
        }
            catch (Exception ex) {
                ex.printStackTrace();
            }*/

  }

  /**
   * @see Storage#openNote(memoranda.Note)
   */
  public javax.swing.text.Document openNote(Note note) {

    HTMLDocument doc = (HTMLDocument) editorKit.createDefaultDocument();
    if (note == null) {
      return doc;
    }
        /*
                String filename = JN_DOCPATH + note.getProject().getID() + File.separator;
                CalendarDate d = note.getDate();
                filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        */
    String filename = getNotePath(note);
    try {
      /*DEBUG*/

//            Util.debug("Open note: " + filename);
//        	Util.debug("Note Title: " + note.getTitle());
      doc.setBase(new URL(getNoteURL(note)));
      editorKit.read(
          new InputStreamReader(new FileInputStream(filename), "UTF-8"),
          doc,
          0);
    } catch (Exception ex) {
      //ex.printStackTrace();
      // Do nothing - we've got a new empty document!
    }

    return doc;
        /*HTMLDocument doc = (HTMLDocument)editorKit.createDefaultDocument();
        if (note == null) return doc;
        String filename = JN_DOCPATH + note.getProject().getID() + "/";
        CalendarDate d = note.getDate();
        filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        try {
            long t1 = new java.util.Date().getTime();
            FileInputStream istream = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(istream);
            doc = (HTMLDocument)ois.readObject();
            ois.close();
            istream.close();
            long t2 = new java.util.Date().getTime();
            System.out.println(filename+" open:"+ (t2-t1) );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return doc;*/
  }

  public String getNoteURL(Note note) {
    return "file:" + JN_DOCPATH + note.getProject().getID() + "/" + note.getId();
  }

  public String getNotePath(Note note) {
    String filename = JN_DOCPATH + note.getProject().getID() + File.separator;
//        CalendarDate d = note.getDate();
    filename += note.getId();//d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
    return filename;
  }


  public void removeNote(Note note) {
    File f = new File(getNotePath(note));
    logger.debug("Remove note: {}", getNotePath(note));
    f.delete();
  }

  /**
   * @see Storage#openProjectManager()
   */
  public void openProjectManager() {
    if (!new File(JN_DOCPATH + ".projects").exists()) {
      ProjectManager._doc = null;
      return;
    }
    logger.debug("Open project manager: {}", JN_DOCPATH + ".projects");
    ProjectManager._doc = openDocument(JN_DOCPATH + ".projects");
  }

  /**
   * @see Storage#storeProjectManager(nu.xom.Document)
   */
  public void storeProjectManager() {
    logger.debug("Save project manager: {}", JN_DOCPATH + ".projects");
    saveDocument(ProjectManager._doc, JN_DOCPATH + ".projects");
  }

  /**
   * @see Storage#removeProject(memoranda.Project)
   */
  public void removeProjectStorage(Project prj) {
    String id = prj.getID();
    File f = new File(JN_DOCPATH + id);
    File[] files = f.listFiles();
    for (File file : files) {
      file.delete();
    }
    f.delete();
  }

  public TaskList openTaskList(Project prj) {
    String fn = JN_DOCPATH + prj.getID() + File.separator + ".tasklist";

    if (documentExists(fn)) {
      logger.debug("Open task list: {}", JN_DOCPATH + prj.getID() + File.separator + ".tasklist");

      Document tasklistDoc = openDocument(fn);
            /*DocType tasklistDoctype = tasklistDoc.getDocType();
            String publicId = null;
            if (tasklistDoctype != null) {
                publicId = tasklistDoctype.getPublicID();
            }
            boolean upgradeOccurred = TaskListVersioning.upgradeTaskList(publicId);
            if (upgradeOccurred) {
                // reload from new file
                tasklistDoc = openDocument(fn);
            }*/
      return new TaskListImpl(tasklistDoc, prj);
    } else {
      logger.debug("New task list created");
      return new TaskListImpl(prj);
    }
  }

  public void storeTaskList(TaskList tasklist, Project prj) {
    logger.debug("Save task list: {}", JN_DOCPATH + prj.getID() + File.separator + ".tasklist");
    Document tasklistDoc = tasklist.getXMLContent();
    //tasklistDoc.setDocType(TaskListVersioning.getCurrentDocType());
    saveDocument(tasklistDoc, JN_DOCPATH + prj.getID() + File.separator + ".tasklist");
  }

  /**
   * @see Storage#createProjectStorage(memoranda.Project)
   */
  public void createProjectStorage(Project prj) {
    logger.debug("Create project dir: {}", JN_DOCPATH + prj.getID());
    File dir = new File(JN_DOCPATH + prj.getID());
    dir.mkdirs();
  }

  /**
   * @see Storage#openNoteList(memoranda.Project)
   */
  public NoteList openNoteList(Project prj) {
    String fn = JN_DOCPATH + prj.getID() + File.separator + ".notes";
    if (documentExists(fn)) {
      logger.debug("Open note list: {}", JN_DOCPATH + prj.getID() + File.separator + ".notes");
      return new NoteListImpl(openDocument(fn), prj);
    } else {
      logger.debug("New note list created");
      return new NoteListImpl(prj);
    }
  }

  /**
   * @see Storage#storeNoteList(memoranda.NoteList, memoranda.Project)
   */
  public void storeNoteList(NoteList nl, Project prj) {
    logger.debug("Save note list: {}", JN_DOCPATH + prj.getID() + File.separator + ".notes");
    saveDocument(
        nl.getXMLContent(),
        JN_DOCPATH + prj.getID() + File.separator + ".notes");
  }

  /**
   * @see Storage#openEventsList()
   */
  public void openEventsManager() {
    if (!new File(JN_DOCPATH + ".events").exists()) {
      EventsManager._doc = null;
      return;
    }
    logger.debug("Open events manager: {}", JN_DOCPATH + ".events");
    EventsManager._doc = openDocument(JN_DOCPATH + ".events");
  }

  /**
   * @see Storage#storeEventsList()
   */
  public void storeEventsManager() {
    logger.debug("Save events manager: {}", JN_DOCPATH + ".events");
    saveDocument(EventsManager._doc, JN_DOCPATH + ".events");
  }

  /**
   * @see Storage#openMimeTypesList()
   */
  public void openMimeTypesList() {
    if (!new File(JN_DOCPATH + ".mimetypes").exists()) {
      try {
        MimeTypesList._doc =
            openDocument(
                FileStorage.class.getResourceAsStream(
                    "/util/default.mimetypes"));
      } catch (Exception e) {
        new ExceptionDialog(
            e,
            "Failed to read default mimetypes config from resources",
            "");
      }
      return;
    }
    logger.debug("Open mimetypes list: {}", JN_DOCPATH + ".mimetypes");
    MimeTypesList._doc = openDocument(JN_DOCPATH + ".mimetypes");
  }

  /**
   * @see Storage#storeMimeTypesList()
   */
  public void storeMimeTypesList() {
    logger.debug("Save mimetypes list: {}", JN_DOCPATH + ".mimetypes");
    saveDocument(MimeTypesList._doc, JN_DOCPATH + ".mimetypes");
  }

  /**
   * @see Storage#openResourcesList(memoranda.Project)
   */
  public ResourcesList openResourcesList(Project prj) {
    String fn = JN_DOCPATH + prj.getID() + File.separator + ".resources";
    if (documentExists(fn)) {
      logger.debug("Open resources list: {}", JN_DOCPATH + prj.getID() + File.separator + ".resources");
      return new ResourcesListImpl(openDocument(fn), prj);
    } else {
      logger.debug("New resources list created");
      return new ResourcesListImpl(prj);
    }
  }

  /**
   * @see Storage#storeResourcesList(memoranda.ResourcesList, memoranda.Project)
   */
  public void storeResourcesList(ResourcesList rl, Project prj) {
    logger.debug("Save resources list: {}", JN_DOCPATH + prj.getID() + File.separator + ".resources");
    saveDocument(
        rl.getXMLContent(),
        JN_DOCPATH + prj.getID() + File.separator + ".resources");
  }

  /**
   * @see Storage#restoreContext()
   */
  public void restoreContext() {
    try {
      logger.debug("Open context: {}", JN_DOCPATH + ".context");
      Context.context.load(new FileInputStream(JN_DOCPATH + ".context"));
    } catch (Exception ex) {
      logger.debug("Context created");
    }
  }

  /**
   * @see Storage#storeContext()
   */
  public void storeContext() {
    try {
      logger.debug("Save context: {}", JN_DOCPATH + ".context");
      Context.context.save(new FileOutputStream(JN_DOCPATH + ".context"));
    } catch (Exception ex) {
      new ExceptionDialog(
          ex,
          "Failed to store context to " + JN_DOCPATH + ".context",
          "");
    }
  }

  @Override
  public String toString() {
    return "FileStorage{" +
        "editorKit=" + editorKit +
        '}';
  }
}
