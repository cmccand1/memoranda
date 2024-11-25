/**
 * Start.java Created on 19.08.2003, 20:40:08 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda;

import java.net.ServerSocket;
import java.net.Socket;

import memoranda.ui.*;
import memoranda.util.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Start {
  private static final Logger logger = LoggerFactory.getLogger(Start.class);

  static App app = null;

  static int DEFAULT_PORT = 19432;
  static boolean checkIfAlreadyStartet = true;

  static {
    String port = Configuration.get("PORT_NUMBER").toString().trim();
    if (!port.isEmpty()) {
      // The Portnumber must be between 1024 (in *nix all Port's < 1024
      // are privileged) and 65535 (the highest Portnumber everywhere)
      int i = Integer.parseInt(port);
      if ((i >= 1024) && (i <= 65535)) {
        DEFAULT_PORT = i;
      }
      logger.debug("Port {} used.", DEFAULT_PORT);
    }

    String check = Configuration.get("CHECK_IF_ALREADY_STARTED").toString().trim();
    if (check.length() > 0 && check.equalsIgnoreCase("no")) {
      checkIfAlreadyStartet = false;
    }
  }

  public static void main(String[] args) {
    if (checkIfAlreadyStartet) {
      try {
        // Try to open a socket. If socket opened successfully (app is already started), take no action and exit.
        Socket socket = new Socket("127.0.0.1", DEFAULT_PORT);
        socket.close();
        System.exit(0);

      } catch (Exception e) {
        // If socket is not opened (app is not started), continue
        // e.printStackTrace();
      }
      new SLThread().start();
    }

    //System.out.println(EventsScheduler.isEventScheduled());
    if ((args.length == 0) || (!args[0].equals("-m"))) {
      app = new App(true);
    } else {
      app = new App(false);
    }
  }
}

class SLThread extends Thread {

  @SuppressWarnings("SocketOpenedButNotSafelyClosed") // false positive
  public void run() {
    try (ServerSocket serverSocket = new ServerSocket(Start.DEFAULT_PORT)) {
      // Wait for a connection from the client
      serverSocket.accept();
      // If a connection is made, show the main window
      Start.app.show();
      new SLThread().start();

    } catch (Exception e) {
      System.err.println("Port: " + Start.DEFAULT_PORT);
      e.printStackTrace();
      new ExceptionDialog(e,
          "Cannot create a socket connection on localhost:" + Start.DEFAULT_PORT,
          "Make sure that other software does not use the port " + Start.DEFAULT_PORT
              + " and examine your security settings.");
    }
  }
}
