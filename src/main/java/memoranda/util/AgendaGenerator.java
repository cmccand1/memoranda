/*
 * AgendaGenerator.java Package: net.sf.memoranda.util Created on 13.01.2004
 * 5:52:54 @author Alex
 */
package memoranda.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import memoranda.projects.CurrentProject;
import memoranda.events.Event;
import memoranda.events.EventsManager;
import memoranda.events.EventsScheduler;
import memoranda.projects.Project;
import memoranda.projects.ProjectManager;
import memoranda.storage.CurrentStorage;
import memoranda.tasks.Task;
import memoranda.tasks.TaskList;
import memoranda.date.CalendarDate;

import java.util.Collections;

import memoranda.ui.AppFrame;
import nu.xom.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */

/*$Id: AgendaGenerator.java,v 1.12 2005/06/13 21:25:27 velhonoja Exp $*/

public class AgendaGenerator {

  private static final Logger logger = LoggerFactory.getLogger(AgendaGenerator.class);

  static String HEADER =
      "<html><head><title></title>\n"
          + "<style>\n"
          + "    body, td {font: 12pt sans-serif}\n"
          + "    h1 {font:20pt sans-serif; background-color:#E0E0E0; margin-top:0}\n"
          + "    h2 {font:16pt sans-serif; margin-bottom:0}\n"
          + "    li {margin-bottom:5px}\n"
          + " a {color:black; text-decoration:none}\n"
          + "</style></head>\n"
          + "<body><table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"4\">\n"
          + "<tr>\n";
  static String FOOTER = "</td></tr></table></body></html>";

  static String generateTasksInfo(Project p, CalendarDate date, Collection expandedTasks) {
    TaskList tl;
    if (p.getID().equals(CurrentProject.get().getID())) {
      tl = CurrentProject.getTaskList();
    } else {
      tl = CurrentStorage.get().openTaskList(p);
    }
    String s = "";
    int k = getProgress(tl);
    if (k > -1) {
      s += "<br>" + Local.getString("Total progress") + ": " + k + "%";
    }
    s += "</td></tr></table>\n";

    Vector tasks = (Vector) tl.getActiveSubTasks(null, date);
    if (tasks.isEmpty()) {
      s += "<p>" + Local.getString("No actual tasks") + ".</p>\n";
    } else {
      s += Local.getString("Actual tasks") + ":<br>\n<ul>\n";

      //            TaskSorter.sort(tasks, date, TaskSorter.BY_IMP_RATE); // TODO: configurable method
      Collections.sort(tasks);
      for (Iterator i = tasks.iterator(); i.hasNext(); ) {
        Task t = (Task) i.next();
        // Always show active tasks only on agenda page from now on.
        // If it's not active, then it's probably "not on the agenda" isn't it?
        //        		if(Context.get("SHOW_ACTIVE_TASKS_ONLY").equals(new Boolean(true))) {
        //                    if (!((t.getStatus() == Task.ACTIVE) || (t.getStatus() == Task.DEADLINE) || (t.getStatus() == Task.FAILED))) {
        //                    	continue;
        //                	}	
        //        		}
        // ignore if it's a sub-task, iterate over ROOT tasks here only
        if (tl.hasParentTask(t.getID())) {
          continue;
        }

        s = s + renderTask(p, date, tl, t, 0, expandedTasks);
        if (expandedTasks.contains(t.getID())) {
          s = s + expandRecursively(p, date, tl, t, expandedTasks, 1);
        }
      }
      s += "\n</ul>\n";
    }

    // logger.debug("html for project {} is\n{}", p.getTitle(), s);
    return s;
  }

  /**
   * @param t
   * @param expandedTasks
   */
  private static String expandRecursively(Project p, CalendarDate date, TaskList tl, Task t,
      Collection expandedTasks, int level) {
    logger.debug("Expanding task {} level {}", t.getText(), level);

    Collection st = tl.getActiveSubTasks(t.getID(), date);

    logger.debug("number of subtasks {}", st.size());

    String s = "\n<ul>\n";

    for (Iterator iter = st.iterator(); iter.hasNext(); ) {
      Task subTask = (Task) iter.next();
      //			if(Context.get("SHOW_ACTIVE_TASKS_ONLY").equals(new Boolean(true))) {
      //                if (!((subTask.getStatus() == Task.ACTIVE) || (subTask.getStatus() == Task.DEADLINE) || (subTask.getStatus() == Task.FAILED))) {
      //                	continue;
      //            	}	
      //			}
      s = s + renderTask(p, date, tl, subTask, level, expandedTasks);
      if (expandedTasks.contains(subTask.getID())) {
        s = s + expandRecursively(p, date, tl, subTask, expandedTasks, level + 1);
      }
    }
    s += "\n</ul>\n";

    return s;
  }

  /**
   * @param p
   * @param date
   * @param s
   * @param t
   * @return
   */
  private static String renderTask(Project p, CalendarDate date, TaskList tl, Task t, int level,
      Collection expandedTasks) {
    String s = "";

    int pg = t.getProgress();
    String progress = "";
    if (pg == 100) {
      progress = "<font color=\"green\">" + Local.getString("Completed") + "</font>";
    } else {
      progress = pg + Local.getString("% done");
    }

    //		String nbsp = "&nbsp;&nbsp;";
    //		String spacing = "";
    //		for(int i = 0; i < level; i ++) {
    //			spacing = spacing + nbsp;
    //		}
    // logger.debug("Spacing for task {} is {}", t.getText(), spacing);

    String subTaskOperation = "";
    if (tl.hasSubTasks(t.getID())) {
//      logger.debug("Task {} has subtasks", t.getID());
      if (expandedTasks.contains(t.getID())) {
        // logger.debug("Task {} is in list of expanded tasks", t.getID());
        subTaskOperation = "<a href=\"memoranda:closesubtasks#" + t.getID() + "\">(-)</a>";
      } else {
        // logger.debug("Task {} is not in list of expanded tasks", t.getID());
        subTaskOperation = "<a href=\"memoranda:expandsubtasks#" + t.getID() + "\">(+)</a>";
      }
    }

    s += "<a name=\"" + t.getID() + "\"><li><p>" + subTaskOperation + "<a href=\"memoranda:tasks#"
        + p.getID()
        + "\"><b>"
        + t.getText()
        + "</b></a> : "
        + progress
        + "</p>"
        + "<p>"
        + Local.getString("Priority")
        + ": "
        + getPriorityString(t.getPriority())
        + "</p>";
		/*<<<<<<< AgendaGenerator.java
		if (!(t.getStartDate().getDate()).after(t.getEndDate().getDate())) {
		    if (t.getEndDate().equals(date))
		        s += "<p><font color=\"#FF9900\"><b>"
		            + Local.getString("Should be done today")
		            + ".</b></font></p>";
		    else {
		        Calendar endDateCal = t.getEndDate().getCalendar();
		        Calendar dateCal = date.getCalendar();
		        int numOfDays = (endDateCal.get(Calendar.YEAR)*365 + endDateCal.get(Calendar.DAY_OF_YEAR)) - 
		                (dateCal.get(Calendar.YEAR)*365 + dateCal.get(Calendar.DAY_OF_YEAR));
		        String days = "";
		        if (numOfDays > 1)
		            days = Local.getString("in")+" "+numOfDays+" "+Local.getString("day(s)");
		        else
		            days = Local.getString("tomorrow");
		        s += "<p>"
		            + Local.getString("Deadline")
		            + ": <i>"
		            + t.getEndDate().getMediumDateString()
		            + "</i> ("+days+")</p>";
		    }                    
		}
=======*/
    if (t.getEndDate().equals(date)) {
      s += "<p><font color=\"#FF9900\"><b>"
          + Local.getString("Should be done today")
          + ".</b></font></p>";
    } else {
      Calendar endDateCal = t.getEndDate().getCalendar();
      Calendar dateCal = date.getCalendar();
      int numOfDays = (endDateCal.get(Calendar.YEAR) * 365 + endDateCal.get(Calendar.DAY_OF_YEAR)) -
          (dateCal.get(Calendar.YEAR) * 365 + dateCal.get(Calendar.DAY_OF_YEAR));
      String days = "";
      if (numOfDays > 0) {
        if (numOfDays > 1) {
          days = Local.getString("in") + " " + numOfDays + " " + Local.getString("day(s)");
        } else {
          days = Local.getString("tomorrow");
        }
        s += "<p>"
            + Local.getString("Deadline")
            + ": <i>"
            + t.getEndDate().getMediumDateString()
            + "</i> (" + days + ")</p>";
      } else if ((numOfDays < 0) && (numOfDays > -10000)) {
        String overdueDays = String.valueOf(-1 * numOfDays);
        s += "<p><font color=\"#FF9900\"><b>"
            + overdueDays + " "
            + Local.getString("days overdue")
            + ".</b></font></p>";
      } else {
        // tasks that have no deadline
        s += "<p>"
            + Local.getString("No Deadline")
            + "</p>";
      }
    }
    //>>>>>>> 1.4
    s += "</li>\n";
    return s;
  }

  static int getProgress(TaskList tl) {
    Vector v = (Vector) tl.getAllSubTasks(null);
    if (v.isEmpty()) {
      return -1;
    }
    int p = 0;
    for (Enumeration en = v.elements(); en.hasMoreElements(); ) {
      Task t = (Task) en.nextElement();
      p += t.getProgress();
    }
    return (p * 100) / (v.size() * 100);
  }

  static String getPriorityString(int p) {
    return switch (p) {
      case Task.PRIORITY_NORMAL -> "<font color=\"green\">" + Local.getString("Normal") + "</font>";
      case Task.PRIORITY_LOW -> "<font color=\"#3333CC\">" + Local.getString("Low") + "</font>";
      case Task.PRIORITY_LOWEST ->
          "<font color=\"#666699\">" + Local.getString("Lowest") + "</font>";
      case Task.PRIORITY_HIGH -> "<font color=\"#FF9900\">" + Local.getString("High") + "</font>";
      case Task.PRIORITY_HIGHEST -> "<font color=\"red\">" + Local.getString("Highest") + "</font>";
      default -> "";
    };
  }

  static String generateProjectInfo(Project p, CalendarDate date, Collection expandedTasks) {
    String s = "<h2><a href=\"memoranda:project#"
        + p.getID()
        + "\">"
        + p.getTitle()
        + "</a></h2>\n"
        + "<table border=\"0\" width=\"100%\" cellpadding=\"2\" bgcolor=\"#EFEFEF\"><tr><td>"
        + Local.getString("Start date") + ": <i>" + p.getStartDate().getMediumDateString()
        + "</i>\n";
    if (p.getEndDate() != null) {
      s += "<br>" + Local.getString("End date") + ": <i>" + p.getEndDate().getMediumDateString()
          + "</i>\n";
    }
    return s + generateTasksInfo(p, date, expandedTasks);
  }

  static String generateAllProjectsInfo(CalendarDate date, Collection expandedTasks) {
    String s =
        "<td width=\"66%\" valign=\"top\">"
            + "<h1>"
            + Local.getString("Projects and tasks")
            + "</h1>\n";
    s += generateProjectInfo(CurrentProject.get(), date, expandedTasks);
    for (Project activeProject : ProjectManager.getActiveProjects()) {
      Project p = activeProject;
      if (!p.getID().equals(CurrentProject.get().getID())) {
        s += generateProjectInfo(p, date, expandedTasks);
      }
    }
    return s + "</td>";
  }

  static String generateEventsInfo(CalendarDate date) {
    String s =
        "<td width=\"34%\" valign=\"top\">"
            + "<a href=\"memoranda:events\"><h1>"
            + Local.getString("Events")
            + "</h1></a>\n"
            + "<table width=\"100%\" valign=\"top\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFF6\">\n";

    List<Event> eventsForDate = (ArrayList<Event>) EventsManager.getEventsForDate(date);
    for (Event e : eventsForDate) {
      String txt = e.getText();
      String iurl =
          AppFrame
              .class
              .getResource("/ui/agenda/spacer.gif")
              .toExternalForm();
      if (date.equals(CalendarDate.today())) {
        if (e.getTime().after(new Date())) {
          txt = "<b>" + txt + "</b>";
        }
        if ((EventsScheduler.isEventScheduled())
            && (EventsScheduler
            .getFirstScheduledEvent()
            .getTime()
            .equals(e.getTime()))) {
          iurl =
              AppFrame
                  .class
                  .getResource("/ui/agenda/arrow.gif")
                  .toExternalForm();
        }
      }
      String icon =
          "<img align=\"right\" width=\"16\" height=\"16\" src=\""
              + iurl
              + "\" border=\"0\"  hspace=\"0\" vspace=\"0\" alt=\"\">";

      s += "<tr>\n<td>"
          + icon
          + "</td>"
          + "<td nowrap class=\"eventtime\">"
          + e.getTimeString()
          + "</td>"
          + "<td width=\"100%\" class=\"eventtext\">&nbsp;&nbsp;"
          + txt
          + "</td>\n"
          + "</tr>";

    }
    return s + "</table>";
  }

  static String generateStickers(CalendarDate date) {
    String iurl =
            AppFrame
            .class
            .getResource("/ui/agenda/addsticker.gif")
            .toExternalForm();
    String iurl2 =
            AppFrame
            .class
            .getResource("/ui/agenda/removesticker.gif")
            .toExternalForm();
    String s =
        "<hr><hr><table border=\"0\" cellpadding=\"0\" width=\"100%\"><tr><td><a href=\"memoranda:importstickers\"><b>"
            + Local.getString("Import sticker")
            + "</b></a></td><td><a href=\"memoranda:exportstickerst\"><b>" + Local.getString(
            "Export sticker as .txt")
            + "</b></a><td><a href=\"memoranda:exportstickersh\"><b>" + Local.getString(
            "Export sticker as HTML") + "</b></a></td></tr></table>"
            + "<table border=\"0\" cellpadding=\"0\" width=\"100%\"><tr><td><a href=\"memoranda:addsticker\"><img align=\"left\" width=\"22\" height=\"22\" src=\""
            + iurl
            + "\" border=\"0\"  hspace=\"0\" vspace=\"0\" alt=\"New sticker\"></a></td><td width=\"100%\"><a href=\"memoranda:addsticker\"><b>&nbsp;"
            + Local.getString("Add sticker") + "</b></a></td></tr></table>";
    PriorityQueue pQ = sortStickers();
    while (!pQ.isEmpty()) {
      Element el = pQ.removeMax();
      String id = el.getAttributeValue("id");
      String txt = el.getValue();
      s +=
          "\n<table border=\"0\" cellpadding=\"0\" width=\"100%\"><table width=\"100%\"><tr bgcolor=\"#E0E0E0\"><td><a href=\"memoranda:editsticker#"
              + id + "\">" + Local.getString("EDIT")
              + "</a></td><td width=\"70%\"><a href=\"memoranda:expandsticker#" + id + "\">"
              + Local.getString("OPEN IN A NEW WINDOW") + "</></td><td align=\"right\">" +
              "&nbsp;" + // without this removesticker link takes klicks from whole cell
              "<a href=\"memoranda:removesticker#" + id
              + "\"><img align=\"left\" width=\"14\" height=\"14\" src=\""
              + iurl2
              + "\" border=\"0\"  hspace=\"0\" vspace=\"0\" alt=\"Remove sticker\"></a></td></table></tr><tr><td>"
              + txt + "</td></tr></table>";
    }
    s += "<hr>";
    return s;
  }

  private static PriorityQueue sortStickers() {
    Map stickers = EventsManager.getStickers();
    PriorityQueue pQ = new PriorityQueue(stickers.size());
    for (Iterator i = stickers.keySet().iterator(); i.hasNext(); ) {
      String id = (String) i.next();
      Element el = (Element) stickers.get(id);
      int j = 2;
      j = Integer.parseInt(el.getAttributeValue("priority"));
      pQ.insert(new Pair(el, j));
    }
    return pQ;
  }

  private static String addExpandHyperLink(String txt, String id) {
    String ret = "";
    int first = txt.indexOf(">");
    int last = txt.lastIndexOf("<");
    ret = txt.substring(0, first + 1) + "<a href=\"memoranda:expandsticker#" + id + "\">"
        + txt.substring(first + 1, last)
        + "</a>" + txt.substring(last);
    return ret;
  }

  private static String addEditHyperLink(String txt, String id) {
    String ret = "";
    int first = txt.indexOf(">");
    int last = txt.lastIndexOf("<");
    ret = txt.substring(0, first + 1) + "<a href=\"memoranda:editsticker#" + id + "\">"
        + txt.substring(first + 1, last) + "</a>" + txt.substring(last);
    return ret;
  }

  public static String getAgenda(CalendarDate date, Collection expandedTasks) {
    String s = HEADER;
    s += generateAllProjectsInfo(date, expandedTasks);
    s += generateEventsInfo(date);
    s += generateStickers(date);
    //        /*DEBUG*/System.out.println(s+FOOTER);
    return s + FOOTER;
  }
	/*    
    we do not need this. Tasks are sorted using the Comparable interface
    public static class TaskSorter {

        static final int BY_IMP_RATE = 0;
        static final int BY_END_DATE = 1;
        static final int BY_PRIORITY = 2;
        static final int BY_COMPLETION = 3;

        private static Vector tasks = null;
        private static CalendarDate date = null;  
        private static int mode = 0;

        public static long calcTaskRate(Task t, CalendarDate d) {
            /*
	 * A "Task rate" is an informal index of importance of the task
	 * considering priority, number of days to deadline and current
	 * progress.
	 * 
	 * rate = (100-progress) / (numOfDays+1) * (priority+1)
	 * /
            Calendar endDateCal = t.getEndDate().getCalendar();
            Calendar dateCal = d.getCalendar();
            int numOfDays = (endDateCal.get(Calendar.YEAR)*365 + endDateCal.get(Calendar.DAY_OF_YEAR)) - 
                            (dateCal.get(Calendar.YEAR)*365 + dateCal.get(Calendar.DAY_OF_YEAR));
            if (numOfDays < 0) return -1; //Something wrong ?
            return (100-t.getProgress()) / (numOfDays+1) * (t.getPriority()+1);
        }

        static long getRate(Object task) {
            Task t = (Task)task;
            switch (mode) {
                case BY_IMP_RATE: return -1*calcTaskRate(t, date);
                case BY_END_DATE: return t.getEndDate().getDate().getTime();
                case BY_PRIORITY: return 5-t.getPriority();
                case BY_COMPLETION: return 100-t.getProgress();
            }
            return -1;         
        }

        private static void doSort(int L, int R) { // Hoar's QuickSort
            int i = L;
            int j = R;
            long x = getRate(tasks.get((L + R) / 2));
            Object w = null;
            do {
                while (getRate(tasks.get(i)) < x) 
                    i++;
                while (x < getRate(tasks.get(j)) && j > 0) 
                    if (j > 0) j--;              
                if (i <= j) {
                    w = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, w);
                    i++;
                    j--;
                }
            }
            while (i <= j);
            if (L < j) 
                doSort(L, j);       
            if (i < R) 
                doSort(i, R);         
        }

        public static void sort(Vector theTasks, CalendarDate theDate, int theMode) {
            if (theTasks == null) return;
            if (theTasks.size() <= 1) return;
            tasks = theTasks; 
            date = theDate;
            mode = theMode;
            doSort(0, tasks.size() - 1);
        }

    }
	 */
}
