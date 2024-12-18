/**
 * CurrentProject.java Created on 13.02.2003, 13:16:52 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.projects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import memoranda.storage.CurrentStorage;
import memoranda.ui.AppFrame;
import memoranda.util.Context;
import memoranda.storage.CurrentStorage;
import memoranda.storage.Storage;
import memoranda.notes.NoteList;
import memoranda.resources.ResourcesList;
import memoranda.tasks.TaskList;

/**
 *
 */
public class CurrentProject {

  private static Project _project;
  private static TaskList _tasklist;
  private static NoteList _notelist;
  private static ResourcesList _resources;
  private static List<ProjectListener> projectListeners = new ArrayList<>();


  static {
    String projectId = (String) Context.get("LAST_OPENED_PROJECT_ID");
    if (projectId == null) {
      projectId = "__default";
      Context.put("LAST_OPENED_PROJECT_ID", projectId);
    }
    //ProjectManager.init();
    _project = ProjectManager.getProject(projectId);

    if (_project == null) {
      _project = ProjectManager.getProject("__default");
      if (_project == null) {
        _project = ProjectManager.getActiveProjects().get(0);
      }
      Context.put("LAST_OPENED_PROJECT_ID", _project.getID());
    }

    _tasklist = CurrentStorage.get().openTaskList(_project);
    _notelist = CurrentStorage.get().openNoteList(_project);
    _resources = CurrentStorage.get().openResourcesList(_project);
    AppFrame.addExitListener(e -> save());
  }


  public static Project get() {
    return _project;
  }

  public static TaskList getTaskList() {
    return _tasklist;
  }

  public static NoteList getNoteList() {
    return _notelist;
  }

  public static ResourcesList getResourcesList() {
    return _resources;
  }

  public static void set(Project project) {
    if (project.getID().equals(_project.getID())) {
      return;
    }
    TaskList newtasklist = CurrentStorage.get().openTaskList(project);
    NoteList newnotelist = CurrentStorage.get().openNoteList(project);
    ResourcesList newresources = CurrentStorage.get().openResourcesList(project);
    notifyListenersBefore(project, newnotelist, newtasklist, newresources);
    _project = project;
    _tasklist = newtasklist;
    _notelist = newnotelist;
    _resources = newresources;
    notifyListenersAfter();
    Context.put("LAST_OPENED_PROJECT_ID", project.getID());
  }

  public static void addProjectListener(ProjectListener pl) {
    projectListeners.add(pl);
  }

  public static Collection getChangeListeners() {
    return projectListeners;
  }

  private static void notifyListenersBefore(Project project, NoteList nl, TaskList tl,
      ResourcesList rl) {
    for (ProjectListener projectListener : projectListeners) {
      projectListener.projectChange(project, nl, tl, rl);
    }
  }

  private static void notifyListenersAfter() {
    for (ProjectListener projectListener : projectListeners) {
      projectListener.projectWasChanged();
    }
  }

  public static void save() {
    Storage storage = CurrentStorage.get();

    storage.storeNoteList(_notelist, _project);
    storage.storeTaskList(_tasklist, _project);
    storage.storeResourcesList(_resources, _project);
    storage.storeProjectManager();
  }

  public static void free() {
    _project = null;
    _tasklist = null;
    _notelist = null;
    _resources = null;
  }
}
