/**
 * CurrentStorage.java Created on 13.02.2003, 18:30:59 Alex Package: net.sf.memoranda.util
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.storage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 *
 */
public class CurrentStorage {

  /**
   * @todo: implement storage congiguration
   */
  private static Storage _storage = new FileStorage();

  private static List<ActionListener> actionListeners = new ArrayList<>();

  public static Storage get() {
    return _storage;
  }

  public static void set(Storage storage) {
    _storage = storage;
    storageChanged();
  }

  public static void addChangeListener(ActionListener al) {
    actionListeners.add(al);
  }

  public static Collection getChangeListeners() {
    return actionListeners;
  }

  private static void storageChanged() {
    for (ActionListener actionListener : actionListeners) {
      actionListener.actionPerformed(new ActionEvent(null, 0, "Current storage changed"));
    }
  }

}
