/**
 * ProjectManager.java Created on 11.02.2003, 17:50:27 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.projects;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import memoranda.date.CalendarDate;
import memoranda.history.History;
import memoranda.storage.CurrentStorage;
import memoranda.util.Local;
import memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

/**
 *
 */
public class ProjectManager {
//    public static final String NS_JNPROJECT = "http://www.openmechanics.org/2003/jnotes-projects-file";

  public static Document _doc = null;
  static Element _root = null;

  static {
    init();
  }

  public static void init() {
    CurrentStorage.get().openProjectManager();
    if (_doc == null) {
      _root = new Element("projects-list");
//            _root.addNamespaceDeclaration("jnotes", NS_JNPROJECT);
//            _root.appendChild(new Comment("This is JNotes 2 data file. Do not modify."));
      _doc = new Document(_root);
      createProject("__default", Local.getString("Default project"), CalendarDate.today(), null);
    } else {
      _root = _doc.getRootElement();
    }
  }

  public static Project getProject(String id) {
    Elements prjs = _root.getChildElements("project");
    for (int i = 0; i < prjs.size(); i++) {
      String pid = (prjs.get(i)).getAttribute("id").getValue();
      if (pid.equals(id)) {
        return new ProjectImpl( prjs.get(i));
      }
    }
    return null;
  }

  public static List<Project> getAllProjects() {
    Elements prjs = _root.getChildElements("project");
    List<Project> projectList = new ArrayList<>();
    for (int i = 0; i < prjs.size(); i++) {
      projectList.add(new ProjectImpl(prjs.get(i)));
    }
    return projectList;
  }

  public static int getAllProjectsNumber() {
    int i;
    try {
      i = (_root.getChildElements("project")).size();
    } catch (NullPointerException e) {
      i = 1;
    }
    return i;
  }

  public static List<Project> getActiveProjects() {
    Elements prjs = _root.getChildElements("project");
    List<Project> projectList = new ArrayList<>();
    for (int i = 0; i < prjs.size(); i++) {
      Project prj = new ProjectImpl(prjs.get(i));
      if (prj.getStatus() == Project.ACTIVE) {
        projectList.add(prj);
      }
    }
    return projectList;
  }

  public static int getActiveProjectsNumber() {
    Elements prjs = _root.getChildElements("project");
    int count = 0;
    for (int i = 0; i < prjs.size(); i++) {
      Project project = new ProjectImpl(prjs.get(i));
      if (project.getStatus() == Project.ACTIVE) {
        count++;
      }
    }
    return count;
  }

  public static Project createProject(String id, String title, CalendarDate startDate,
      CalendarDate endDate) {
    Element el = new Element("project");
    el.addAttribute(new Attribute("id", id));
    _root.appendChild(el);
    Project newProject = new ProjectImpl(el);
    newProject.setTitle(title);
    newProject.setStartDate(startDate);
    newProject.setEndDate(endDate);
    CurrentStorage.get().createProjectStorage(newProject);
    return newProject;
  }

  public static Project createProject(String title, CalendarDate startDate, CalendarDate endDate) {
    return createProject(Util.generateId(), title, startDate, endDate);
  }

  public static void removeProject(String id) {
    Project projectToRemove = getProject(id);
    if (projectToRemove == null) {
      return;
    }
    History.removeProjectHistory(projectToRemove);
    CurrentStorage.get().removeProjectStorage(projectToRemove);
    Elements prjs = _root.getChildElements("project");
    for (int i = 0; i < prjs.size(); i++) {
      String pid = (prjs.get(i)).getAttribute("id").getValue();
      if (pid.equals(id)) {
        _root.removeChild(prjs.get(i));
        return;
      }
    }
  }
}
