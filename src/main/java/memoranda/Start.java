/**
 * Start.java Created on 19.08.2003, 20:40:08 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda;

import static memoranda.Start.logger;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import memoranda.ui.*;
import memoranda.util.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Start class contains the main method to launch the application and handles the initialization
 * and configuration settings.
 */
public class Start {

  static final Logger logger = LoggerFactory.getLogger(Start.class);

  static App app = null;

  private static final int MIN_PORT = 1024;
  private static final int MAX_PORT = 65535;
  private static final int DEFAULT_PORT = 19432;

  static int port = DEFAULT_PORT;
  static boolean enableSingleInstanceCheck;

  // Static block to initialize configuration settings
  static {
    // Select the port number to use
    String desiredPortString = Configuration.get("PORT_NUMBER").toString().trim();
    if (!desiredPortString.isEmpty()) {
      int desiredPort = Integer.parseInt(desiredPortString);
      if (isValidPortNo(desiredPort)) {
        port = desiredPort;
        logger.debug("Port {} used.", port);
      } else {
        port = DEFAULT_PORT;
        logger.debug("Invalid port number {}. Using default port {}.", desiredPort, port);
      }
    }

    // Configure single instance check behavior
    String singleInstanceCheckConfig = Configuration.get("ENABLE_SINGLE_INSTANCE_CHECK").toString()
        .trim();
    enableSingleInstanceCheck = !singleInstanceCheckConfig.equalsIgnoreCase("no");
    logger.debug("Single instance check enabled: {}", enableSingleInstanceCheck);
  }

  /**
   * Validates if the given port number is within the valid range. The port number must be in the
   * range 1024 to 65535. Ports < 1024 are privileged on Unix-like systems. 65535 is the maximum
   * valid port number.
   *
   * @param desiredPort the port number to validate
   * @return true if the port number is valid, false otherwise
   */
  private static boolean isValidPortNo(int desiredPort) {
    return (desiredPort >= MIN_PORT) && (desiredPort <= MAX_PORT);
  }

  /**
   * The main method to start the application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    if (enableSingleInstanceCheck) {
      try {
        // Try to open a socket. If socket opened successfully (app is already started), take no action and exit.
        Socket socket = new Socket(InetAddress.getLocalHost(), port);
        socket.close();
        logger.debug("Application is already running on port {}. Exiting.", port);
        System.exit(0);

      } catch (Exception ignore) {
        // If socket is not opened (app is not started), continue
        logger.debug("No other instance is running. Proceeding with startup.");
        new SLThread().start();
      }
    }

    // System.out.println(EventsScheduler.isEventScheduled());

    // Initialize the application based on the command-line arguments
    if ((args.length == 0) || (!args[0].equals("-m"))) {
      app = new App(true);
    } else {
      app = new App(false);
    }
  }
}

/**
 * SLThread is a helper class that runs a server socket to check if the application is already
 * running.
 */
class SLThread extends Thread {

  @SuppressWarnings("SocketOpenedButNotSafelyClosed") // false positive
  public void run() {
    try (ServerSocket serverSocket = new ServerSocket(Start.port)) {
      // Wait for a connection from the client
      serverSocket.accept();
      // If a connection is made, show the main window
      Start.app.show();
      new SLThread().start();

    } catch (Exception e) {
      logger.error("Cannot create a socket connection on localhost:{}", Start.port, e);
      new ExceptionDialog(e,
          "Cannot create a socket connection on localhost:" + Start.port,
          "Make sure that other software does not use the port " + Start.port
              + " and examine your security settings.");
    }
  }
}
