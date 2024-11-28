/**
 * ProjectImpl.java Created on 11.02.2003, 23:06:22 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.projects;

import memoranda.date.CalendarDate;
import memoranda.date.CurrentDate;
import memoranda.notes.NoteList;
import memoranda.resources.ResourcesList;
import memoranda.storage.CurrentStorage;
import memoranda.tasks.TaskList;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Default implementation of Project interface
 */
public class ProjectImpl implements Project {

  private Element _root;

  /**
   * Constructor for ProjectImpl.
   */
  public ProjectImpl(Element root) {
    _root = root;
  }

  /**
   * @see Project#getID()
   */
  @Override
  public String getID() {
    return _root.getAttribute("id").getValue();
  }

  /**
   * @see Project#getStartDate()
   */
  @Override
  public CalendarDate getStartDate() {
    Attribute dateAttr = _root.getAttribute("startDate");
    if (dateAttr == null) {
      return null;
    }
    return new CalendarDate(dateAttr.getValue());
  }

  /**
   * @see Project#setStartDate(net.sf.memoranda.util.CalendarDate)
   */
  @Override
  public void setStartDate(CalendarDate date) {
    if (date != null) {
      setAttr("startDate", date.toString());
    }
  }

  /**
   * @see Project#getEndDate()
   */
  @Override
  public CalendarDate getEndDate() {
    Attribute dateAttr = _root.getAttribute("endDate");
    if (dateAttr == null) {
      return null;
    }
    return new CalendarDate(dateAttr.getValue());
  }

  /**
   * @see Project#setEndDate(net.sf.memoranda.util.CalendarDate)
   */
  @Override
  public void setEndDate(CalendarDate date) {
    if (date != null) {
      setAttr("endDate", date.toString());
    } else if (_root.getAttribute("endDate") != null) {
      setAttr("endDate", null);
    }
  }

  /**
   * @see Project#getStatus()
   */
  @Override
  public int getStatus() {
    if (isFrozen()) {
      return Project.FROZEN;
    }
    CalendarDate today = CurrentDate.get();
    CalendarDate projectStart = getStartDate();
    CalendarDate projectEnd = getEndDate();
    if (projectEnd == null) {
      if (today.before(projectStart)) {
        return Project.SCHEDULED;
      } else {
        return Project.ACTIVE;
      }
    }
    if (today.inPeriod(projectStart, projectEnd)) {
      return Project.ACTIVE;
    } else if (today.after(projectEnd)) {
      //if (getProgress() == 100)
      return Project.COMPLETED;
            /*else
                return Project.FAILED;*/
    } else {
      return Project.SCHEDULED;
    }
  }

  private boolean isFrozen() {
    return _root.getAttribute("frozen") != null;
  }


    /*public int getProgress() {
        Vector v = getAllTasks();
        if (v.size() == 0) return 0;
        int p = 0;
        for (Enumeration en = v.elements(); en.hasMoreElements();) {
          Task t = (Task) en.nextElement();
          p += t.getProgress();
        }
        return (p*100)/(v.size()*100);
    }*/


  /**
   * @see Project#freeze()
   */
  @Override
  public void freeze() {
    _root.addAttribute(new Attribute("frozen", "yes"));
  }

  /**
   * @see Project#unfreeze()
   */
  @Override
  public void unfreeze() {
    if (this.isFrozen()) {
      _root.removeAttribute(new Attribute("frozen", "yes"));
    }
  }

  /**
   * @see Project#getTitle()
   */
  @Override
  public String getTitle() {
    Attribute titleAttr = _root.getAttribute("title");
    if (titleAttr != null) {
      return titleAttr.getValue();
    }
    return "";
  }

  /**
   * @see Project#setTitle(java.lang.String)
   */
  @Override
  public void setTitle(String title) {
    setAttr("title", title);
  }

  private void setAttr(String name, String value) {
    Attribute attr = _root.getAttribute(name);
    if (attr == null) {
      if (value != null) {
        _root.addAttribute(new Attribute(name, value));
      }
    } else if (value != null) {
      attr.setValue(value);
    } else {
      _root.removeAttribute(attr);
    }
  }

  @Override
  public String getDescription() {
    Element thisElement = _root.getFirstChildElement("description");
    if (thisElement == null) {
      return null;
    } else {
      return thisElement.getValue();
    }
  }

  @Override
  public void setDescription(String s) {
    Element description = _root.getFirstChildElement("description");
    if (description == null) {
      description = new Element("description");
      description.appendChild(s);
      _root.appendChild(description);
    } else {
      description.removeChildren();
      description.appendChild(s);
    }
  }

  /**
   * @see net.sf.memoranda.Project#getTaskList()
   */
  @Override
  public TaskList getTaskList() {
    return CurrentStorage.get().openTaskList(this);
  }

  /**
   * @see net.sf.memoranda.Project#getNoteList()
   */
  @Override
  public NoteList getNoteList() {
    return CurrentStorage.get().openNoteList(this);
  }

  /**
   * @see net.sf.memoranda.Project#getResourcesList()
   */
  @Override
  public ResourcesList getResourcesList() {
    return CurrentStorage.get().openResourcesList(this);
  }

  @Override
  public String toString() {
    return "ProjectImpl{" +
        "_root=" + _root +
        '}';
  }
}
