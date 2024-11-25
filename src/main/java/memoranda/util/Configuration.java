/**
 * Configuration.java Created on 12.03.2003, 0:31:22 Alex Package: net.sf.memoranda.util
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import memoranda.ui.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Configuration {
  private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

  static LoadableProperties config = new LoadableProperties();
  static String configPath = getConfigPath();

  static {
    try {
      config.load(new FileInputStream(configPath));
      logger.debug("Loaded config from {}", configPath);
    } catch (Exception e) {
      File f = new File(configPath);
      new File(f.getParent()).mkdirs();
      logger.debug("New configuration created: {}", configPath);
      try {
        config.load(Configuration.class.getResourceAsStream("/util/memoranda.default.properties"));
        saveConfig();
      } catch (Exception e2) {
        new ExceptionDialog(e2, "Failed to load default configuration from resources.", "");
        config = null;
      }
    }
  }

  static String getConfigPath() {
    String memorandaPropertiesDir = Util.getEnvDir() + "memoranda.properties";
    if (new File(memorandaPropertiesDir).exists()) {
      return memorandaPropertiesDir;
    }
    String jNotes2PropertiesDir = Util.getEnvDir() + "jnotes2.properties";
    if (new File(jNotes2PropertiesDir).exists()) {
      logger.debug("{} not found.\n{} used instead.", memorandaPropertiesDir, jNotes2PropertiesDir);
      return jNotes2PropertiesDir;
    }
    return memorandaPropertiesDir;
  }

  public static void saveConfig() {
    try {
      config.save(new FileOutputStream(configPath));
      logger.debug("Saved config to {}", configPath);
    } catch (Exception e) {
      new ExceptionDialog(e, "Failed to save a configuration file:<br>" + configPath, "");
    }
  }

  public static Object get(String key) {
    // if no such key found, return empty string
    if ((config.get(key)) == null) {
      logger.debug("Key '{}' not found in config. Returning value as empty string.", key);
      return "";
    }
    return config.get(key);
  }

  public static void put(String key, Object value) {
    config.put(key, value);
  }
}
