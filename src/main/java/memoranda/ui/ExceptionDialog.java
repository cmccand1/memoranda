package memoranda.ui;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;

import memoranda.util.*;

import java.awt.event.*;
import java.io.*;

/*$Id: ExceptionDialog.java,v 1.2 2004/10/18 19:09:10 ivanrise Exp $*/
public class ExceptionDialog extends JDialog {

  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  private JPanel jPanel1 = new JPanel();
  private JLabel jLabel1 = new JLabel();
  private JPanel jPanel2 = new JPanel();
  private JLabel jLabel2 = new JLabel();
  private BorderLayout borderLayout2 = new BorderLayout();
  private BorderLayout borderLayout3 = new BorderLayout();
  private JLabel descriptionLabel = new JLabel();

  private String description;
  private String tip;
  private String trace;
  private JPanel jPanel3 = new JPanel();
  private JScrollPane scrollPane = new JScrollPane();
  private JTextArea traceTextArea = new JTextArea();
  private JButton reportButton = new JButton();
  private JButton closeButton = new JButton();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JPanel jPanel4 = new JPanel();
  private JButton copyButton = new JButton();
  private BorderLayout borderLayout4 = new BorderLayout();

  private Frame dialogOwner;

  public ExceptionDialog(Exception exc, String description, String tip) {
    super(App.getMainAppFrame(), "Problem", true);
    exc.printStackTrace();
    dialogOwner = App.getMainAppFrame();
    if ((description != null) && (!description.isEmpty())) {
      this.description = description;
    } else if (exc.getMessage() != null) {
      this.description = exc.getMessage();
    } else {
      this.description = "Unknown error";
    }
    this.tip = tip;
    StringWriter sw = new StringWriter();
    exc.printStackTrace(new PrintWriter(sw));
    this.trace = sw.toString();
    try {
      jbInit();
      setVisible(true);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public ExceptionDialog(Exception exc) {
    this(exc, "", "");
  }

  private void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    jPanel1.setBackground(Color.white);
    jPanel1.setLayout(borderLayout3);
    jLabel1.setFont(new java.awt.Font("Dialog", Font.BOLD, 16));
    jLabel1.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
    jLabel1.setText("Problem occured");
    jLabel1.setIcon(new ImageIcon(Objects.requireNonNull(ExceptionDialog.class.getResource(
        "/ui/icons/error.png"))));

    jLabel2.setFont(new Font("Dialog", Font.PLAIN, 11));
    jLabel2.setText("<html>An internal exception occured. It is may be a result of bug in the " +
        "program, corrupted data, incorrect configuration or hardware failure.<br><br>" +
        "Click on <b>Report bug..</b> button to submit a bug to the Memoranda bugs tracker on SourceForge.net </html>");
    jPanel2.setLayout(borderLayout2);
    jPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    borderLayout3.setVgap(5);
    String labelText = "<html><b>Description:</b><br>" + description;
    if ((tip != null) && (!tip.isEmpty())) {
      labelText = labelText + "<br><br><b>Tip:</b><br>" + tip;
    }
    labelText = labelText + "<br><br><b>Stack trace:</b></html>";
    descriptionLabel.setText(labelText);
    descriptionLabel.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
    scrollPane.setEnabled(false);
    reportButton.setMaximumSize(new Dimension(120, 25));
    reportButton.setMinimumSize(new Dimension(120, 25));
    reportButton.setPreferredSize(new Dimension(120, 25));
    reportButton.setText("Report bug...");
    reportButton.addActionListener(this::reportB_actionPerformed);
    closeButton.setMaximumSize(new Dimension(120, 25));
    closeButton.setMinimumSize(new Dimension(120, 25));
    closeButton.setPreferredSize(new Dimension(120, 25));
    closeButton.setText("Close");
    closeButton.addActionListener(this::closeB_actionPerformed);
    this.getRootPane().setDefaultButton(closeButton);
    jPanel3.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    copyButton.setText("Copy to clipboard");
    copyButton.addActionListener(this::copyB_actionPerformed);
    copyButton.setHorizontalAlignment(SwingConstants.RIGHT);
    jPanel4.setLayout(borderLayout4);
    traceTextArea.setText(trace);
    traceTextArea.setEditable(false);
    borderLayout1.setVgap(5);
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel1, BorderLayout.NORTH);
    jPanel1.add(jLabel2, BorderLayout.CENTER);
    panel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(descriptionLabel, BorderLayout.NORTH);
    jPanel2.add(scrollPane, BorderLayout.CENTER);
    jPanel2.add(jPanel4, BorderLayout.SOUTH);
    jPanel4.add(copyButton, BorderLayout.WEST);
    scrollPane.getViewport().add(traceTextArea, null);
    panel1.add(jPanel3, BorderLayout.SOUTH);
    jPanel3.add(closeButton, null);
    jPanel3.add(reportButton, null);
    Dimension dlgSize = new Dimension(400, 500);
    this.setSize(dlgSize);
    if (dialogOwner != null) {
      Dimension frmSize = dialogOwner.getSize();
      Point loc = dialogOwner.getLocation();
      this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
          (frmSize.height - dlgSize.height) / 2 + loc.y);
    }
  }

  void copyB_actionPerformed(ActionEvent e) {
    traceTextArea.selectAll();
    traceTextArea.copy();
    traceTextArea.setSelectionEnd(0);
  }

  void closeB_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  void reportB_actionPerformed(ActionEvent e) {
    Util.runBrowser(App.BUGS_TRACKER_URL);
  }
}