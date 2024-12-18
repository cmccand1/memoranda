/**
 * NoteImpl.java Created on 13.02.2003, 15:36:55 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.notes;

import java.util.Objects;
import memoranda.date.CalendarDate;
import memoranda.projects.Project;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 *
 */
/*$Id: NoteImpl.java,v 1.6 2004/10/06 19:15:44 ivanrise Exp $*/
public class NoteImpl implements Note, Comparable {

  private Element _el;
  private Project _project;

  /**
   * Constructor for NoteImpl.
   */
  public NoteImpl(Element el, Project project) {
    _el = el;
    _project = project;
  }

  /**
   * @see Note#getDate()
   */
  public CalendarDate getDate() {
    Element day = (Element) _el.getParent();
    Element month = (Element) day.getParent();
    Element year = (Element) month.getParent();

    //   return new CalendarDate(day.getAttribute("date").getValue());

    return new CalendarDate(Integer.parseInt(day.getAttribute("day").getValue()),
        Integer.parseInt(month.getAttribute("month").getValue()),
        Integer.parseInt(year.getAttribute("year").getValue()));

  }

  public Project getProject() {
    return _project;
  }

  /**
   * @see Note#getTitle()
   */
  public String getTitle() {
    Attribute titleAttr = _el.getAttribute("title");
    if (titleAttr == null) {
      return "";
    }
    return _el.getAttribute("title").getValue();
  }

  /**
   * @see Note#setTitle(java.lang.String)
   */
  public void setTitle(String s) {
    Attribute titleAttr = _el.getAttribute("title");
    if (titleAttr == null) {
      _el.addAttribute(new Attribute("title", s));
    } else {
      titleAttr.setValue(s);
    }
  }

  /**
   * @see Note#getId
   */

  public String getId() {
    Attribute idAttr = _el.getAttribute("refid");
    if (idAttr == null) {
      return "";
    }
    return _el.getAttribute("refid").getValue();
  }

  /**
   * @see Note#setId(java.lang.String)
   */

  public void setId(String s) {
    Attribute idAttr = _el.getAttribute("refid");
    if (idAttr == null) {
      _el.addAttribute(new Attribute("refid", s));
    } else {
      idAttr.setValue(s);
    }
  }

  /**
   * @see Note#isMarked()
   */
  public boolean isMarked() {
    return _el.getAttribute("bookmark") != null;
  }

  /**
   * @see Note#setMark(boolean)
   */
  public void setMark(boolean isMarked) {
    Attribute bookmarkAttr = _el.getAttribute("bookmark");
    if (bookmarkAttr == null) {
      if (isMarked) {
        _el.addAttribute(new Attribute("bookmark", "yes"));
      }
    } else if (!isMarked) {
      _el.removeAttribute(bookmarkAttr);
    }
  }

  /*
   * Compare two notes by date
   */
  @Override
  public int compareTo(Object o) {
    Objects.requireNonNull(o);
    Note note = (Note) o;
    if (getDate().getDate().getTime() > note.getDate().getDate().getTime()) {
      return 1;
    } else if (getDate().getDate().getTime() < note.getDate().getDate().getTime()) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return "NoteImpl{" +
        "_el=" + _el +
        ", _project=" + _project +
        '}';
  }
}
