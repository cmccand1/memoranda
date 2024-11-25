/*
 * Created on Feb 12, 2005
 *
 */
package memoranda.tasks;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import memoranda.projects.Project;
import memoranda.projects.ProjectManager;
import memoranda.storage.FileStorage;
import memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ryanho
 * <p>
 * Upgrades data files from older versions to new versions
 */
public class TaskListVersioning {

  private static final Logger logger = LoggerFactory.getLogger(TaskListVersioning.class);

  private static final String[] VERSIONS = new String[]{
      "-//Memoranda//DTD Tasklist 1.0//EN",
      "-//Memoranda//DTD Tasklist 1.1d1//EN"
  };

  public static DocType getCurrentDocType() {
    return new DocType("tasklist", TaskListVersioning.getCurrentVersionPublicId(), "tasklist.dtd");
  }

  public static String getCurrentVersionPublicId() {
    return VERSIONS[VERSIONS.length - 1];
  }

  public static int getIndexOfVersion(String publicId) {
    if (publicId == null) {
      // earlier versions do not have public ID, it is version 1.0 which is the first entry
      return 0;
    }
    for (int i = 0; i < VERSIONS.length; i++) {
      if (publicId.equals(VERSIONS[i])) {
        return i;
      }
    }
    logger.debug("Version {} not found", publicId);
    return -1;
  }

  public static boolean upgradeTaskList(String publicId) {
    int vid = getIndexOfVersion(publicId);

    if (vid == (VERSIONS.length - 1)) {
      logger.debug("Version {} is the latest version, skipping upgrade", publicId);
      return false;
    } else {
      List<Project> projects = ProjectManager.getAllProjects();
      String[] projectIds = new String[projects.size()];
      int count = 0;
      for (Project project : projects) {
        projectIds[count++] = project.getID();
      }

      // keep upgrading until it's the current version
      while (vid < (VERSIONS.length - 1)) {
        if (vid == 0) {
          upgrade1_1d1(projectIds);
        }
        vid++;
      }
      return true;
    }
  }

  private static void upgrade1_1d1(String[] projectIds) {
    for (String projectId : projectIds) {
      logger.debug("Upgrading project {} from version 1.0 to version 1.1d1", projectId);

      String filePath = FileStorage.JN_DOCPATH + projectId + File.separator + ".tasklist";
      Document doc = FileStorage.openDocument(filePath);

      Element root = doc.getRootElement();
      Elements tasks = root.getChildElements("task");

      for (int j = 0; j < tasks.size(); j++) {
        Element task = tasks.get(j);

//	Decided not to change the date format after all but I'm leaving this code here 
//	in case we need it later. Ryan
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//                Attribute startDateAttr = task.getAttribute("startDate");
//                Date startDate = (new CalendarDate(startDateAttr.getValue(),"/")).getDate();
//                startDateAttr.setValue(sdf.format(startDate));
//
//                Attribute endDateAttr = task.getAttribute("endDate");
//                if (endDateAttr != null) {
//                    Date endDate = (new CalendarDate(endDateAttr.getValue(),"/")).getDate();
//                    endDateAttr.setValue(sdf.format(endDate));                    
//                }

        Attribute parentAttr = task.getAttribute("parent");
        if ((parentAttr == null) || (parentAttr.getValue() == "")) {
          // no parent, do nothing here
        } else {
          // put the task under the parent task
          String parentId = parentAttr.getValue();
          for (int k = 0; k < tasks.size(); k++) {
            Element potentialParent = tasks.get(k);
            if (parentId.equals(potentialParent.getAttribute("id").getValue())) {
              // found parent, put self under it
              task.removeAttribute(parentAttr);
              task.detach();
              potentialParent.appendChild(task);
            }
          }
        }
      }
      doc.setDocType(getCurrentDocType());
      FileStorage.saveDocument(doc, filePath);
    }
  }
}
