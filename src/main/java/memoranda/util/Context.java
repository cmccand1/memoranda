package memoranda.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import memoranda.storage.CurrentStorage;
import memoranda.ui.AppFrame;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class Context {

  public static LoadableProperties context = new LoadableProperties();

  static {
    CurrentStorage.get().restoreContext();
    AppFrame.addExitListener(e -> CurrentStorage.get().storeContext());
  }

  public static Object get(Object key) {
    return context.get(key);
  }

  public static void put(Object key, Object value) {
    context.put(key, value);
  }

}