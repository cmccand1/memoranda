package memoranda.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.*;

public class LoadableProperties extends Hashtable {

  public LoadableProperties() {
    super();
  }

  public void load(InputStream inStream) throws IOException {

    BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));

    String aKey;
    String aValue;
    int index;
    String line = getNextLine(in);
    while (line != null) {
      line = line.trim();
      if (isValid(line)) {
        index = line.indexOf("=");
        aKey = line.substring(0, index).trim();
        aValue = line.substring(index + 1).trim();
        put(aKey.toUpperCase(), aValue);
      }
      line = getNextLine(in);
    }
  }

  public void save(OutputStream outStream, boolean sorted) throws IOException {
    if (!sorted) {
      save(outStream);
      return;
    }
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
    String aKey;
    Object aValue;
    TreeMap tm = new TreeMap(this);
    for (Object o : tm.keySet()) {
      aKey = (String) o;
      aValue = get(aKey);
      out.write(aKey + " = " + aValue);
      out.newLine();
    }
    out.flush();
    out.close();
  }

  public void save(OutputStream outStream) throws IOException {
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
    String aKey;
    Object aValue;
    for (Enumeration e = keys(); e.hasMoreElements(); ) {
      aKey = (String) e.nextElement();
      aValue = get(aKey);
      out.write(aKey + " = " + aValue);
      out.newLine();
    }
    out.flush();
    out.close();
  }

  private boolean isValid(String str) {
    if (str == null) {
      return false;
    }
    if (!str.isEmpty()) {
      if (str.startsWith("#") || str.startsWith("!")) {
        return false;
      }
    } else {
      return false;
    }

    int index = str.indexOf("=");
    return index > 0;
  }

  private String getNextLine(BufferedReader br) {
    try {
      return br.readLine();
    } catch (Exception e) {
      return null;
    }

  }

}
