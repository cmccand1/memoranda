package memoranda.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;

import memoranda.history.History;
import memoranda.projects.*;
import memoranda.notes.*;

import memoranda.date.CurrentDate;
import memoranda.resources.ResourcesList;
import memoranda.tasks.TaskList;
import memoranda.ui.htmleditor.HTMLEditor;
import memoranda.ui.projects.ProjectExportDialog;
import memoranda.ui.projects.ProjectsPanel;
import memoranda.util.Configuration;
import memoranda.util.Context;
import memoranda.storage.CurrentStorage;
import memoranda.util.Local;
import memoranda.projects.ProjectExporter;
import memoranda.projects.ProjectPackager;
import memoranda.util.Util;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;


/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

public class AppFrame extends JFrame {

  BorderLayout frameLayout = new BorderLayout();
  boolean isProjectsPanelExpanded = false;

  JPanel contentPane;
  JSplitPane splitPane = new JSplitPane();

  ProjectsPanel projectsPanel = new ProjectsPanel();
  public WorkPanel workPanel = new WorkPanel();
  HTMLEditor editor = workPanel.dailyItemsPanel.editorPanel.editor;
  JLabel statusBar = new JLabel();

  static List<ActionListener> exitListeners = new ArrayList<>();

  public Action exportNotesAction =
      new AbstractAction(Local.getString("Export notes") + "...") {

        public void actionPerformed(ActionEvent e) {
          exportNotes_actionPerformed(e);
        }
      };

  public Action importNotesAction =
      new AbstractAction(Local.getString("Import multiple notes")) {

        public void actionPerformed(ActionEvent e) {
          importNotes_actionPerformed(e);
        }
      };
  public Action importOneNoteAction =
      new AbstractAction(Local.getString("Import one note")) {

        public void actionPerformed(ActionEvent e) {
          importOneNote_actionPerformed(e);
        }
      };


  /* =========================================================================================== */
  /**
   * Main Menu bar
   */
  JMenuBar menuBar = new JMenuBar();
  JMenu fileMenu = new JMenu(Local.getString("File"));
  JMenu editMenu = new JMenu(Local.getString("Edit"));
  JMenu insertMenu = new JMenu(Local.getString("Insert"));
  JMenu FormatMenu = new JMenu(Local.getString("Format"));
  JMenu goMenu = new JMenu(Local.getString("Go"));
  JMenu helpMenu = new JMenu(Local.getString("Help"));

  /* =========================================================================================== */
  /**
   * File menu items
   */
  JMenuItem fileMenuNewProject = new JMenuItem(Local.getString("New project") + "...");
  JMenuItem fileMenuNewNote = new JMenuItem(Local.getString("New note") + "...");
  JMenuItem fileMenuPackProject = new JMenuItem(Local.getString("Pack project") + "...");
  JMenuItem fileMenuUnpackProject = new JMenuItem(Local.getString("Unpack project") + "...");
  JMenuItem jMenuFileExportPrj = new JMenuItem(Local.getString("Export project") + "...");
  JMenuItem jMenuFileImportPrj = new JMenuItem(Local.getString("Import project") + "...");
  JMenuItem fileMenuImportOneNote = new JMenuItem(Local.getString("Import one note") + "...");
  JMenuItem fileMenuExportNote = new JMenuItem(Local.getString("Export current note") + "...");
  JMenuItem fileMenuPreferences = new JMenuItem(Local.getString("Preferences") + "...");
  JMenuItem fileMenuExit = new JMenuItem(Local.getString("Exit"));
  JMenuItem fileMenuMinimize = new JMenuItem(Local.getString("Close the window"));

  /* =========================================================================================== */
  /**
   * Edit menu items
   */
  JMenuItem editMenuUndo = new JMenuItem(Local.getString("Undo"));
  JMenuItem editMenuRedo = new JMenuItem(Local.getString("Redo"));
  JMenuItem editMenuCut = new JMenuItem(Local.getString("Cut"));
  JMenuItem editMenuCopy = new JMenuItem(Local.getString("Copy"));
  JMenuItem editMenuPaste = new JMenuItem(Local.getString("Paste"));
  JMenuItem editMenuPasteSpecial = new JMenuItem(Local.getString("Paste special"));
  JMenuItem editMenuSelectAll = new JMenuItem(Local.getString("Select all"));
  JMenuItem editMenuFind = new JMenuItem(Local.getString("Find & replace") + "...");

  /* =========================================================================================== */
  /**
   * Insert menu items
   */
  JMenuItem insertMenuImage = new JMenuItem(Local.getString("Image") + "...");
  JMenuItem insertMenuTable = new JMenuItem(Local.getString("Table") + "...");
  JMenuItem insertMenuLink = new JMenuItem(Local.getString("Hyperlink") + "...");
  JMenu insertMenuList = new JMenu(Local.getString("List"));
  JMenuItem insertMenuInsertListUL = new JMenuItem(Local.getString("Unordered"));
  JMenuItem insertMenuInsertListOL = new JMenuItem(Local.getString("Ordered"));
  JMenuItem insertMenuLineBreak = new JMenuItem(Local.getString("Line break"));
  JMenuItem insertMenuHorizontalRule = new JMenuItem(Local.getString("Horizontal rule"));
  JMenuItem insertMenuSpecialChar = new JMenuItem(Local.getString("Special character") + "...");
  JMenuItem insertMenuCurrentDate = new JMenuItem(Local.getString("Current date"));
  JMenuItem insertMenuCurrentTime = new JMenuItem(Local.getString("Current time"));
  JMenuItem insertMenuFile = new JMenuItem(Local.getString("File") + "...");
  JMenu jMenuInsertSpecial = new JMenu(); // todo: this is a duplicate?

  /* =========================================================================================== */
  /**
   * Format menu items
   */

  // Format Paragraph Style
  JMenu paragraphStyleMenu = new JMenu(Local.getString("Paragraph style"));
  JMenuItem paragraphStyleMenuP = new JMenuItem(Local.getString("Paragraph"));
  JMenuItem paragraphStyleMenuH1 = new JMenuItem(Local.getString("Header") + " 1");
  JMenuItem paragraphStyleMenuH2 = new JMenuItem(Local.getString("Header") + " 2");
  JMenuItem paragraphStyleMenuH3 = new JMenuItem(Local.getString("Header") + " 3");
  JMenuItem paragraphStyleMenuH4 = new JMenuItem(Local.getString("Header") + " 4");
  JMenuItem paragraphStyleMenuH5 = new JMenuItem(Local.getString("Header") + " 5");
  JMenuItem paragraphStyleMenuH6 = new JMenuItem(Local.getString("Header") + " 6");
  JMenuItem paragraphStyleMenuPreformatted = new JMenuItem(Local.getString("Preformatted text"));
  JMenuItem paragraphStyleMenuBlockQuote = new JMenuItem(Local.getString("Blockquote"));

  // Format Character Style
  JMenu characterStyleMenu = new JMenu(Local.getString("Character style"));
  JMenuItem characterStyleMenuNormal = new JMenuItem(Local.getString("Normal"));
  JMenuItem characterStyleMenuEM = new JMenuItem(Local.getString("Emphasis"));
  JMenuItem characterStyleMenuSTRONG = new JMenuItem(Local.getString("Strong"));
  JMenuItem characterStyleMenuCODE = new JMenuItem(Local.getString("Code"));
  JMenuItem characterStyleMenuCITE = new JMenuItem(Local.getString("Cite"));
  JMenuItem characterStyleMenuSUPERSCRIPT = new JMenuItem(Local.getString("Superscript"));
  JMenuItem characterStyleMenuSUBSCRIPT = new JMenuItem(Local.getString("Subscript"));
  JMenuItem characterStyleMenuCUSTOM = new JMenuItem(Local.getString("Custom style") + "...");
  JMenuItem characterStyleMenuBOLD = new JMenuItem(Local.getString("Bold"));
  JMenuItem characterStyleMenuITALIC = new JMenuItem(Local.getString("Italic"));
  JMenuItem characterStyleMenuUNDERLINE = new JMenuItem(Local.getString("Underline"));

  // Format Alignment Style
  JMenu alignmentStyleMenu = new JMenu(Local.getString("Alignment"));
  JMenuItem alignmentStyleAlignLeft = new JMenuItem(Local.getString("Left"));
  JMenuItem alignmentStyleAlignCenter = new JMenuItem(Local.getString("Center"));
  JMenuItem alignmentStyleAlignRight = new JMenuItem(Local.getString("Right"));

  // Format Table Style
  JMenu tableStyleMenu = new JMenu(Local.getString("Table"));
  JMenuItem tableStyleInsertRow = new JMenuItem(Local.getString("Insert row"));
  JMenuItem tableStyleInsertColumn = new JMenuItem(Local.getString("Insert cell"));

  // Object properties
  JMenuItem formatMenuObjectProps = new JMenuItem(Local.getString("Object properties") + "...");

  /* =========================================================================================== */
  /**
   * Go menu items
   */
  JMenuItem goMenuHistoryBack = new JMenuItem(Local.getString("History back"));
  JMenuItem goMenuHistoryForward = new JMenuItem(Local.getString("History forward"));
  JMenuItem goMenuDayBack = new JMenuItem(Local.getString("One day back"));
  JMenuItem goMenuDayForward = new JMenuItem(Local.getString("One day forward"));
  JMenuItem goMenuToday = new JMenuItem(Local.getString("To today"));

  /* =========================================================================================== */
  /**
   * Help menu items
   */
  JMenuItem helpMenuGuide = new JMenuItem(Local.getString("Online user's guide"));
  JMenuItem helpMenuWebsite = new JMenuItem(Local.getString("Memoranda web site"));
  JMenuItem helpMenuBugReport = new JMenuItem(Local.getString("Report a bug"));
  JMenuItem helpMenuAbout = new JMenuItem(Local.getString("About Memoranda"));

  //Construct the frame
  public AppFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      init();
    } catch (Exception e) {
      new ExceptionDialog(e);
    }
  }

  //Component initialization
  private void init() throws Exception {
    this.setIconImage(new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource(
        "/ui/icons/jnotes16.png")))
        .getImage());
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(frameLayout);

    // Set the actions for the menu items
    addActionListeners();

    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    splitPane.setContinuousLayout(true);
    splitPane.setDividerSize(3);
    splitPane.setDividerLocation(28);
    projectsPanel.setMinimumSize(new Dimension(10, 28));
    projectsPanel.setPreferredSize(new Dimension(10, 28));
    splitPane.setDividerLocation(28);

    helpMenuGuide.setIcon(new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource(
        "/ui/icons/help.png"))));
    helpMenuWebsite.setIcon(new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource(
        "/ui/icons/web.png"))));

    /**
     * Add menu items to menus
     */
    initFileMenu();
    initEditMenu();
    initInsertMenu();
    initFormatMenu();
    initHelpMenu();
    initGoMenu();
    addMenusToMenuBar();

    /**
     * Add components to the frame
     */
    contentPane.add(statusBar, BorderLayout.SOUTH);
    contentPane.add(splitPane, BorderLayout.CENTER);
    splitPane.add(projectsPanel, JSplitPane.TOP);
    splitPane.add(workPanel, JSplitPane.BOTTOM);
    splitPane.setBorder(null);
    workPanel.setBorder(null);

    setEnabledEditorMenus(false);

    ActionListener setMenusDisabled = e -> setEnabledEditorMenus(false);

    this.workPanel.dailyItemsPanel.taskB
        .addActionListener(setMenusDisabled);
    this.workPanel.dailyItemsPanel.alarmB.addActionListener(
        setMenusDisabled);
    this.workPanel.tasksB.addActionListener(setMenusDisabled);
    this.workPanel.eventsB.addActionListener(setMenusDisabled);
    this.workPanel.filesB.addActionListener(setMenusDisabled);
    this.workPanel.agendaB.addActionListener(setMenusDisabled);
    this.workPanel.notesB.addActionListener(
        e -> setEnabledEditorMenus(true));

    Object fwo = Context.get("FRAME_WIDTH");
    Object fho = Context.get("FRAME_HEIGHT");
    if ((fwo != null) && (fho != null)) {
      int w = new Integer((String) fwo).intValue();
      int h = new Integer((String) fho).intValue();
      this.setSize(w, h);
    } else {
      this.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    Object xo = Context.get("FRAME_XPOS");
    Object yo = Context.get("FRAME_YPOS");
    if ((xo != null) && (yo != null)) {
      int x = new Integer((String) xo).intValue();
      int y = new Integer((String) yo).intValue();
      this.setLocation(x, y);
    }

    String pan = (String) Context.get("CURRENT_PANEL");
    if (pan != null) {
      workPanel.selectPanel(pan);
      setEnabledEditorMenus(pan.equalsIgnoreCase("NOTES"));
    }

    CurrentProject.addProjectListener(new ProjectListener() {

      public void projectChange(Project prj, NoteList nl, TaskList tl,
          ResourcesList rl) {
      }

      public void projectWasChanged() {
        setTitle("Memoranda - " + CurrentProject.get().getTitle());
      }
    });

  }

  private void initGoMenu() {
    goMenu.add(goMenuHistoryBack);
    goMenu.add(goMenuHistoryForward);
    goMenu.addSeparator();
    goMenu.add(goMenuDayBack);
    goMenu.add(goMenuDayForward);
    goMenu.add(goMenuToday);
  }

  private void initFormatMenu() {
    FormatMenu.add(paragraphStyleMenu);
    FormatMenu.add(characterStyleMenu);
    FormatMenu.add(alignmentStyleMenu);
    FormatMenu.addSeparator();
    FormatMenu.add(tableStyleMenu);
    FormatMenu.addSeparator();
    FormatMenu.add(formatMenuObjectProps);

    paragraphStyleMenu.add(paragraphStyleMenuP);
    paragraphStyleMenu.addSeparator();
    paragraphStyleMenu.add(paragraphStyleMenuH1);
    paragraphStyleMenu.add(paragraphStyleMenuH2);
    paragraphStyleMenu.add(paragraphStyleMenuH3);
    paragraphStyleMenu.add(paragraphStyleMenuH4);
    paragraphStyleMenu.add(paragraphStyleMenuH5);
    paragraphStyleMenu.add(paragraphStyleMenuH6);
    paragraphStyleMenu.addSeparator();
    paragraphStyleMenu.add(paragraphStyleMenuPreformatted);
    paragraphStyleMenu.add(paragraphStyleMenuBlockQuote);

    characterStyleMenu.add(characterStyleMenuNormal);
    characterStyleMenu.addSeparator();
    characterStyleMenu.add(characterStyleMenuBOLD);
    characterStyleMenu.add(characterStyleMenuITALIC);
    characterStyleMenu.add(characterStyleMenuUNDERLINE);
    characterStyleMenu.addSeparator();
    characterStyleMenu.add(characterStyleMenuEM);
    characterStyleMenu.add(characterStyleMenuSTRONG);
    characterStyleMenu.add(characterStyleMenuCODE);
    characterStyleMenu.add(characterStyleMenuCITE);
    characterStyleMenu.addSeparator();
    characterStyleMenu.add(characterStyleMenuSUPERSCRIPT);
    characterStyleMenu.add(characterStyleMenuSUBSCRIPT);
    characterStyleMenu.addSeparator();
    characterStyleMenu.add(characterStyleMenuCUSTOM);

    alignmentStyleMenu.add(alignmentStyleAlignLeft);
    alignmentStyleMenu.add(alignmentStyleAlignCenter);
    alignmentStyleMenu.add(alignmentStyleAlignRight);

    tableStyleMenu.add(tableStyleInsertRow);
    tableStyleMenu.add(tableStyleInsertColumn);
  }

  private void initInsertMenu() {
    insertMenu.add(insertMenuImage);
    insertMenu.add(insertMenuTable);
    insertMenu.add(insertMenuLink);
    insertMenu.add(insertMenuList);
    //jMenuInsert.add(jMenuInsertSpecial);
    insertMenuList.add(insertMenuInsertListUL);
    insertMenuList.add(insertMenuInsertListOL);
    insertMenu.addSeparator();
    insertMenu.add(insertMenuLineBreak);
    insertMenu.add(insertMenuHorizontalRule);
    insertMenu.add(insertMenuSpecialChar);
    insertMenu.addSeparator();
    insertMenu.add(insertMenuCurrentDate);
    insertMenu.add(insertMenuCurrentTime);
    insertMenu.addSeparator();
    insertMenu.add(insertMenuFile);
  }

  private void initEditMenu() {
    editMenu.add(editMenuUndo);
    editMenu.add(editMenuRedo);
    editMenu.addSeparator();
    editMenu.add(editMenuCut);
    editMenu.add(editMenuCopy);
    editMenu.add(editMenuPaste);
    editMenu.add(editMenuPasteSpecial);
    editMenu.addSeparator();
    editMenu.add(editMenuSelectAll);
    editMenu.addSeparator();
    editMenu.add(editMenuFind);
  }

  private void addMenusToMenuBar() {
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    menuBar.add(insertMenu);
    menuBar.add(FormatMenu);
    menuBar.add(goMenu);
    menuBar.add(helpMenu);
    this.setJMenuBar(menuBar);
  }

  private void initHelpMenu() {
    helpMenu.add(helpMenuGuide);
    helpMenu.add(helpMenuWebsite);
    helpMenu.add(helpMenuBugReport);
    helpMenu.addSeparator();
    helpMenu.add(helpMenuAbout);
  }

  private void initFileMenu() {
    fileMenu.add(fileMenuNewProject);
    fileMenu.add(fileMenuNewNote);
    fileMenu.addSeparator();
    fileMenu.add(fileMenuPackProject);
    fileMenu.add(fileMenuUnpackProject);
    fileMenu.addSeparator();
//    fileMenu.add(jMenuFileExportPrj);
    fileMenu.add(fileMenuExportNote);
    fileMenu.add(fileMenuImportOneNote);
//    fileMenu.add(jMenuFileImportPrj);
    fileMenu.addSeparator();
    fileMenu.add(fileMenuPreferences);
    fileMenu.addSeparator();
    fileMenu.add(fileMenuMinimize);
    fileMenu.addSeparator();
    fileMenu.add(fileMenuExit);
  }

  private void projectsPanelExpand_actionPerformed() {
    if (isProjectsPanelExpanded) {
      isProjectsPanelExpanded = false;
      splitPane.setDividerLocation(28);
    } else {
      isProjectsPanelExpanded = true;
      splitPane.setDividerLocation(0.2);
    }
  }

  private void addActionListeners() {
    projectsPanel.AddExpandListener(e -> projectsPanelExpand_actionPerformed());
    // File actions
    fileMenuNewNote.addActionListener(workPanel.dailyItemsPanel.editorPanel.newAction);
    fileMenuExit.addActionListener(e -> doExit());
    fileMenuPackProject.addActionListener(e -> packProject());
    fileMenuUnpackProject.addActionListener(e -> unpackProject());
    fileMenuMinimize.addActionListener(e -> doMinimize());
    fileMenuPreferences.addActionListener(e -> showPreferences());
    fileMenuExportNote.addActionListener(this::exportNotes_actionPerformed);
//    fileMenuExportNote.addActionListener(workPanel.dailyItemsPanel.editorPanel.exportAction); // todo: which is it? this or the line above
//    fileMenuImportNotes.addActionListener(this::importNotes_actionPerformed);
    fileMenuImportOneNote.addActionListener(this::importOneNote_actionPerformed);

    // Edit actions
    editMenuUndo.addActionListener(editor.undoAction);
    editMenuRedo.addActionListener(editor.redoAction);
    editMenuCut.addActionListener(editor.cutAction);
    editMenuCopy.addActionListener(editor.copyAction);
    editMenuPaste.addActionListener(editor.pasteAction);
    editMenuPasteSpecial.addActionListener(editor.stylePasteAction);
    editMenuSelectAll.addActionListener(editor.selectAllAction);
    editMenuFind.addActionListener(editor.findAction);

    // Insert actions
    insertMenuImage.addActionListener(editor.imageAction);
    insertMenuTable.addActionListener(editor.tableAction);
    insertMenuLink.addActionListener(editor.linkAction);
    insertMenuInsertListUL.addActionListener(editor.ulAction);
    insertMenuInsertListOL.addActionListener(editor.olAction);
    insertMenuLineBreak.addActionListener(editor.breakAction);
    insertMenuHorizontalRule.addActionListener(editor.insertHRAction);
    insertMenuSpecialChar.addActionListener(editor.insCharAction);
    insertMenuCurrentDate.addActionListener(workPanel.dailyItemsPanel.editorPanel.insertDateAction);
    insertMenuCurrentTime.addActionListener(workPanel.dailyItemsPanel.editorPanel.insertTimeAction);
    insertMenuFile.addActionListener(workPanel.dailyItemsPanel.editorPanel.importAction);

    // Format actions
    paragraphStyleMenuP.addActionListener(editor.new BlockAction(editor.T_P, ""));
    paragraphStyleMenuH1.addActionListener(editor.new BlockAction(editor.T_H1, ""));
    paragraphStyleMenuH2.addActionListener(editor.new BlockAction(editor.T_H2, ""));
    paragraphStyleMenuH3.addActionListener(editor.new BlockAction(editor.T_H3, ""));
    paragraphStyleMenuH4.addActionListener(editor.new BlockAction(editor.T_H4, ""));
    paragraphStyleMenuH5.addActionListener(editor.new BlockAction(editor.T_H5, ""));
    paragraphStyleMenuH6.addActionListener(editor.new BlockAction(editor.T_H6, ""));
    paragraphStyleMenuPreformatted.addActionListener(editor.new BlockAction(editor.T_PRE, ""));
    paragraphStyleMenuBlockQuote.addActionListener(editor.new BlockAction(editor.T_BLOCKQ, ""));

    // Format character actions
    characterStyleMenuNormal.addActionListener(editor.new InlineAction(editor.I_NORMAL, ""));
    characterStyleMenuEM.addActionListener(editor.new InlineAction(editor.I_EM, ""));
    characterStyleMenuSTRONG.addActionListener(editor.new InlineAction(editor.I_STRONG, ""));
    characterStyleMenuCODE.addActionListener(editor.new InlineAction(editor.I_CODE, ""));
    characterStyleMenuCITE.addActionListener(editor.new InlineAction(editor.I_CITE, ""));
    characterStyleMenuSUPERSCRIPT.addActionListener(
        editor.new InlineAction(editor.I_SUPERSCRIPT, ""));
    characterStyleMenuSUBSCRIPT.addActionListener(editor.new InlineAction(editor.I_SUBSCRIPT, ""));
    characterStyleMenuCUSTOM.addActionListener(editor.new InlineAction(editor.I_CUSTOM, ""));
    characterStyleMenuBOLD.addActionListener(editor.boldAction);
    characterStyleMenuITALIC.addActionListener(editor.italicAction);
    characterStyleMenuUNDERLINE.addActionListener(editor.underAction);

    // Format alignment actions
    alignmentStyleAlignLeft.addActionListener(editor.lAlignAction);
    alignmentStyleAlignCenter.addActionListener(editor.cAlignAction);
    alignmentStyleAlignRight.addActionListener(editor.rAlignAction);

    // Format table actions
    tableStyleInsertRow.addActionListener(editor.insertTableRowAction);
    tableStyleInsertColumn.addActionListener(editor.insertTableCellAction);

    // Help actions
    helpMenuGuide.addActionListener(this::helpMenuGuide_actionPerformed);
    helpMenuWebsite.addActionListener(this::helpMenuWebsite_actionPerformed);
    helpMenuBugReport.addActionListener(this::helpMenuBugReport_actionPerformed);
    helpMenuAbout.addActionListener(this::helpMenuAbout_actionPerformed);

    // Object properties
    formatMenuObjectProps.addActionListener(editor.propsAction);

    // Go actions
    goMenuHistoryBack.addActionListener(History.historyBackAction);
    goMenuHistoryForward.addActionListener(History.historyForwardAction);
    goMenuDayBack.addActionListener(workPanel.dailyItemsPanel.calendar.dayBackAction);
    goMenuDayForward.addActionListener(workPanel.dailyItemsPanel.calendar.dayForwardAction);
    goMenuToday.addActionListener(workPanel.dailyItemsPanel.calendar.todayAction);

  }

  protected void helpMenuBugReport_actionPerformed(ActionEvent e) {
    Util.runBrowser(App.BUGS_TRACKER_URL);
  }

  protected void helpMenuWebsite_actionPerformed(ActionEvent e) {
    Util.runBrowser(App.WEBSITE_URL);
  }

  protected void helpMenuGuide_actionPerformed(ActionEvent e) {
    Util.runBrowser(App.GUIDE_URL);
  }

  //File | Exit action performed
  public void doExit() {
    if (Configuration.get("ASK_ON_EXIT").equals("yes")) {
      Dimension frameSize = this.getSize();
      Point location = this.getLocation();

      ExitConfirmationDialog confirmExitDialog = new ExitConfirmationDialog(this,
          Local.getString("Exit"));
      confirmExitDialog.setLocation(
          (frameSize.width - confirmExitDialog.getSize().width) / 2 + location.x,
          (frameSize.height - confirmExitDialog.getSize().height) / 2 + location.y);
      confirmExitDialog.setVisible(true);
      if (confirmExitDialog.CANCELLED) {
        return;
      }
    }

    // Write the frame details to the configuration file
    Context.put("FRAME_WIDTH", this.getWidth());
    Context.put("FRAME_HEIGHT", this.getHeight());
    Context.put("FRAME_XPOS", this.getLocation().x);
    Context.put("FRAME_YPOS", this.getLocation().y);
    exitNotify();
    System.exit(0);
  }

  public void doMinimize() {
    exitNotify();
    App.closeWindow();
  }

  //Help | About action performed
  public void helpMenuAbout_actionPerformed(ActionEvent e) {
    AppFrame_AboutBox dialog = new AppFrame_AboutBox(this);
    Dimension dlgSize = dialog.getSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
        (frmSize.height - dlgSize.height) / 2 + loc.y);
    dialog.setModal(true);
    dialog.setVisible(true);
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      if (Configuration.get("ON_CLOSE").equals("exit")) {
        doExit();
      } else {
        doMinimize();
      }
    } else if ((e.getID() == WindowEvent.WINDOW_ICONIFIED)) {
      super.processWindowEvent(new WindowEvent(this,
          WindowEvent.WINDOW_CLOSING));
      doMinimize();
    } else {
      super.processWindowEvent(e);
    }
  }

  public static void addExitListener(ActionListener al) {
    exitListeners.add(al);
  }

  public static Collection getExitListeners() {
    return exitListeners;
  }

  private static void exitNotify() {
    for (ActionListener exitListener : exitListeners) {
      exitListener.actionPerformed(null);
    }
  }

  public void setEnabledEditorMenus(boolean enabled) {
    this.editMenu.setEnabled(enabled);
    this.FormatMenu.setEnabled(enabled);
    this.insertMenu.setEnabled(enabled);
    this.fileMenuNewNote.setEnabled(enabled);
    this.fileMenuExportNote.setEnabled(enabled);
  }

  public void packProject() {
    // Fix until Sun's JVM supports more locales...
    UIManager.put("FileChooser.saveInLabelText", Local
        .getString("Save in:"));
    UIManager.put("FileChooser.upFolderToolTipText", Local.getString(
        "Up One Level"));
    UIManager.put("FileChooser.newFolderToolTipText", Local.getString(
        "Create New Folder"));
    UIManager.put("FileChooser.listViewButtonToolTipText", Local
        .getString("List"));
    UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
        .getString("Details"));
    UIManager.put("FileChooser.fileNameLabelText", Local.getString(
        "File Name:"));
    UIManager.put("FileChooser.filesOfTypeLabelText", Local.getString(
        "Files of Type:"));
    UIManager.put("FileChooser.saveButtonText", Local.getString("Save"));
    UIManager.put("FileChooser.saveButtonToolTipText", Local.getString(
        "Save selected file"));
    UIManager
        .put("FileChooser.cancelButtonText", Local.getString("Cancel"));
    UIManager.put("FileChooser.cancelButtonToolTipText", Local.getString(
        "Cancel"));

    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);
    chooser.setDialogTitle(Local.getString("Pack project"));
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    //chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.RTF));
    chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.ZIP));
    // fixes XP style look cosmetical problems JVM 1.4.2 and 1.4.2_01
    chooser.setPreferredSize(new Dimension(550, 375));

    //Added to fix the problem with packing a file then deleting that file.
    //(jcscoobyrs) 17-Nov-2003 at 14:57:06 PM
    //---------------------------------------------------------------------
    File lastSel = null;

    try {
      lastSel = (java.io.File) Context.get("LAST_SELECTED_PACK_FILE");
    } catch (ClassCastException cce) {
      lastSel = new File(System.getProperty("user.dir") + File.separator);
    }
    //---------------------------------------------------------------------

    if (lastSel != null) {
      chooser.setCurrentDirectory(lastSel);
    }
    if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    Context.put("LAST_SELECTED_PACK_FILE", chooser.getSelectedFile());
    java.io.File f = chooser.getSelectedFile();
    ProjectPackager.pack(CurrentProject.get(), f);
  }

  public void unpackProject() {
    // Fix until Sun's JVM supports more locales...
    UIManager.put("FileChooser.lookInLabelText", Local
        .getString("Look in:"));
    UIManager.put("FileChooser.upFolderToolTipText", Local.getString(
        "Up One Level"));
    UIManager.put("FileChooser.newFolderToolTipText", Local.getString(
        "Create New Folder"));
    UIManager.put("FileChooser.listViewButtonToolTipText", Local
        .getString("List"));
    UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
        .getString("Details"));
    UIManager.put("FileChooser.fileNameLabelText", Local.getString(
        "File Name:"));
    UIManager.put("FileChooser.filesOfTypeLabelText", Local.getString(
        "Files of Type:"));
    UIManager.put("FileChooser.openButtonText", Local.getString("Open"));
    UIManager.put("FileChooser.openButtonToolTipText", Local.getString(
        "Open selected file"));
    UIManager
        .put("FileChooser.cancelButtonText", Local.getString("Cancel"));
    UIManager.put("FileChooser.cancelButtonToolTipText", Local.getString(
        "Cancel"));

    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);
    chooser.setDialogTitle(Local.getString("Unpack project"));
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.ZIP));
    //chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.RTF));
    // fixes XP style look cosmetical problems JVM 1.4.2 and 1.4.2_01
    chooser.setPreferredSize(new Dimension(550, 375));

    //Added to fix the problem with packing a file then deleting that file.
    //(jcscoobyrs) 17-Nov-2003 at 14:57:06 PM
    //---------------------------------------------------------------------
    File lastSel = null;

    try {
      lastSel = (java.io.File) Context.get("LAST_SELECTED_PACK_FILE");
    } catch (ClassCastException cce) {
      lastSel = new File(System.getProperty("user.dir") + File.separator);
    }
    //---------------------------------------------------------------------

    if (lastSel != null) {
      chooser.setCurrentDirectory(lastSel);
    }
    if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    Context.put("LAST_SELECTED_PACK_FILE", chooser.getSelectedFile());
    java.io.File f = chooser.getSelectedFile();
    ProjectPackager.unpack(f);
    projectsPanel.prjTablePanel.updateUI();
  }

  public void showPreferences() {
    PreferencesDialog dlg = new PreferencesDialog(this);
    dlg.pack();
    dlg.setLocationRelativeTo(this);
    dlg.setVisible(true);
  }

  protected void exportNotes_actionPerformed(ActionEvent e) {
    // Fix until Sun's JVM supports more locales...
    UIManager.put(
        "FileChooser.lookInLabelText",
        Local.getString("Save in:"));
    UIManager.put(
        "FileChooser.upFolderToolTipText",
        Local.getString("Up One Level"));
    UIManager.put(
        "FileChooser.newFolderToolTipText",
        Local.getString("Create New Folder"));
    UIManager.put(
        "FileChooser.listViewButtonToolTipText",
        Local.getString("List"));
    UIManager.put(
        "FileChooser.detailsViewButtonToolTipText",
        Local.getString("Details"));
    UIManager.put(
        "FileChooser.fileNameLabelText",
        Local.getString("File Name:"));
    UIManager.put(
        "FileChooser.filesOfTypeLabelText",
        Local.getString("Files of Type:"));
    UIManager.put("FileChooser.saveButtonText", Local.getString("Save"));
    UIManager.put(
        "FileChooser.saveButtonToolTipText",
        Local.getString("Save selected file"));
    UIManager.put(
        "FileChooser.cancelButtonText",
        Local.getString("Cancel"));
    UIManager.put(
        "FileChooser.cancelButtonToolTipText",
        Local.getString("Cancel"));

    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);
    chooser.setDialogTitle(Local.getString("Export notes"));
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    chooser.addChoosableFileFilter(
        new AllFilesFilter(AllFilesFilter.XHTML));
    chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.HTML));

    String lastSel = (String) Context.get("LAST_SELECTED_EXPORT_FILE");
    if (lastSel != null) {
      chooser.setCurrentDirectory(new File(lastSel));
    }

    ProjectExportDialog dlg =
        new ProjectExportDialog(
            App.getMainAppFrame(),
            Local.getString("Export notes"),
            chooser);
    String enc = (String) Context.get("EXPORT_FILE_ENCODING");
    if (enc != null) {
      dlg.encCB.setSelectedItem(enc);
    }
    String spl = (String) Context.get("EXPORT_SPLIT_NOTES");
    if (spl != null) {
      dlg.splitChB.setSelected(spl.equalsIgnoreCase("true"));
    }
    String ti = (String) Context.get("EXPORT_TITLES_AS_HEADERS");
    if (ti != null) {
      dlg.titlesAsHeadersChB.setSelected(ti.equalsIgnoreCase("true"));
    }
    Dimension dlgSize = new Dimension(550, 500);
    dlg.setSize(dlgSize);
    Dimension frmSize = App.getMainAppFrame().getSize();
    Point loc = App.getMainAppFrame().getLocation();
    dlg.setLocation(
        (frmSize.width - dlgSize.width) / 2 + loc.x,
        (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setVisible(true);
    if (dlg.CANCELLED) {
      return;
    }

    Context.put(
        "LAST_SELECTED_EXPORT_FILE",
        chooser.getSelectedFile().getPath());
    Context.put("EXPORT_SPLIT_NOTES", Boolean.valueOf(dlg.splitChB.isSelected()).toString());
    Context.put("EXPORT_TITLES_AS_HEADERS",
        Boolean.valueOf(dlg.titlesAsHeadersChB.isSelected()).toString());

    int ei = dlg.encCB.getSelectedIndex();
    enc = null;
    if (ei == 1) {
      enc = "UTF-8";
    }
    boolean nument = (ei == 2);
    File f = chooser.getSelectedFile();
    boolean xhtml =
        chooser.getFileFilter().getDescription().contains("XHTML");
    CurrentProject.save();
    ProjectExporter.export(CurrentProject.get(), chooser.getSelectedFile(), enc, xhtml,
        dlg.splitChB.isSelected(), true, nument, dlg.titlesAsHeadersChB.isSelected(), false);
  }

  protected void importNotes_actionPerformed(ActionEvent e) {

    UIManager.put("FileChooser.lookInLabelText", Local
        .getString("Look in:"));
    UIManager.put("FileChooser.upFolderToolTipText", Local.getString(
        "Up One Level"));
    UIManager.put("FileChooser.newFolderToolTipText", Local.getString(
        "Create New Folder"));
    UIManager.put("FileChooser.listViewButtonToolTipText", Local
        .getString("List"));
    UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
        .getString("Details"));
    UIManager.put("FileChooser.fileNameLabelText", Local.getString(
        "File Name:"));
    UIManager.put("FileChooser.filesOfTypeLabelText", Local.getString(
        "Files of Type:"));
    UIManager.put("FileChooser.openButtonText", Local.getString("Open"));
    UIManager.put("FileChooser.openButtonToolTipText", Local.getString(
        "Open selected file"));
    UIManager.put("FileChooser.cancelButtonText", Local.getString("Cancel"));
    UIManager.put("FileChooser.cancelButtonToolTipText", Local.getString(
        "Cancel"));

    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);
    chooser.setDialogTitle(Local.getString("Import notes"));
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.HTML));
    chooser.setPreferredSize(new Dimension(550, 375));

    File lastSel = null;

    try {
      lastSel = (java.io.File) Context.get("LAST_SELECTED_NOTE_FILE");
    } catch (ClassCastException cce) {
      lastSel = new File(System.getProperty("user.dir") + File.separator);
    }
    //---------------------------------------------------------------------

    if (lastSel != null) {
      chooser.setCurrentDirectory(lastSel);
    }
    if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    Context.put("LAST_SELECTED_NOTE_FILE", chooser.getSelectedFile());
    java.io.File f = chooser.getSelectedFile();
    HashMap<String, String> notesName = new HashMap();
    HashMap<String, String> notesContent = new HashMap();
    Builder parser = new Builder();
    String id = "", name = "", content = "";
    try {
      Document document = parser.build(f);
      Element body = document.getRootElement().getFirstChildElement("body");
      Element names = body.getFirstChildElement("div").getFirstChildElement("ul");
      Elements namelist = names.getChildElements("li");
      Element item;

      for (int i = 0; i < namelist.size(); i++) {
        item = namelist.get(i);
        id = item.getFirstChildElement("a").getAttributeValue("href").replace("\"", "")
            .replace("#", "");
        name = item.getValue();
        notesName.put(id, name);
      }
      System.out.println("id: " + id + " name: " + name);

      Elements contlist = body.getChildElements("a");
      for (int i = 0; i < (contlist.size() - 1); i++) {
        item = contlist.get(i);
        id = item.getAttributeValue("name").replace("\"", "");
        content = item.getFirstChildElement("div").getValue();
        notesContent.put(id, content);
      }

      JEditorPane p = new JEditorPane();
      p.setContentType("text/html");
      for (Map.Entry<String, String> entry : notesName.entrySet()) {
        id = entry.getKey();
        name = entry.getValue().substring(11);
        content = notesContent.get(id);
        p.setText(content);
        HTMLDocument doc = (HTMLDocument) p.getDocument();
        Note note = CurrentProject.getNoteList().createNoteForDate(CurrentDate.get());
        note.setTitle(name);
        note.setId(Util.generateId());
        CurrentStorage.get().storeNote(note, doc);
      }
      workPanel.dailyItemsPanel.notesControlPane.refresh();

    } catch (Exception exc) {
      exc.printStackTrace();
    }
  }

  protected void importOneNote_actionPerformed(ActionEvent e) {

    UIManager.put("FileChooser.lookInLabelText", Local
        .getString("Look in:"));
    UIManager.put("FileChooser.upFolderToolTipText", Local.getString(
        "Up One Level"));
    UIManager.put("FileChooser.newFolderToolTipText", Local.getString(
        "Create New Folder"));
    UIManager.put("FileChooser.listViewButtonToolTipText", Local
        .getString("List"));
    UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
        .getString("Details"));
    UIManager.put("FileChooser.fileNameLabelText", Local.getString(
        "File Name:"));
    UIManager.put("FileChooser.filesOfTypeLabelText", Local.getString(
        "Files of Type:"));
    UIManager.put("FileChooser.openButtonText", Local.getString("Open"));
    UIManager.put("FileChooser.openButtonToolTipText", Local.getString(
        "Open selected file"));
    UIManager.put("FileChooser.cancelButtonText", Local.getString("Cancel"));
    UIManager.put("FileChooser.cancelButtonToolTipText", Local.getString(
        "Cancel"));

    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);

    chooser.setDialogTitle(Local.getString("Import notes"));
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.HTML));
    chooser.setPreferredSize(new Dimension(550, 375));

    File lastSel = null;

    try {
      lastSel = (java.io.File) Context.get("LAST_SELECTED_NOTE_FILE");
    } catch (ClassCastException cce) {
      lastSel = new File(System.getProperty("user.dir") + File.separator);
    }
    //---------------------------------------------------------------------

    if (lastSel != null) {
      chooser.setCurrentDirectory(lastSel);
    }
    if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    Context.put("LAST_SELECTED_NOTE_FILE", chooser.getSelectedFile());
    java.io.File f = chooser.getSelectedFile();
    HashMap<String, String> notesName = new HashMap<>();
    HashMap<String, String> notesContent = new HashMap<>();
    Builder parser = new Builder();
    String id = "", name = "", content = "";
    try {
      Document document = parser.build(f);
      content = document.getRootElement().getFirstChildElement("body").getValue();
      content = content.substring(content.indexOf("\n", content.indexOf("-")));
      content = content.replace("<p>", "").replace("</p>", "\n");
      name = f.getName().substring(0, f.getName().lastIndexOf("."));
      Element item;
      id = Util.generateId();
      System.out.println(id + " " + name + " " + content);
      notesName.put(id, name);
      notesContent.put(id, content);
      JEditorPane p = new JEditorPane();
      p.setContentType("text/html");

      for (Map.Entry<String, String> entry : notesName.entrySet()) {
        id = entry.getKey();
        System.out.println(id + " " + name + " " + content);
        p.setText(content);
        HTMLDocument doc = (HTMLDocument) p.getDocument();
        Note note = CurrentProject.getNoteList().createNoteForDate(CurrentDate.get());
        note.setTitle(name);
        note.setId(Util.generateId());
        CurrentStorage.get().storeNote(note, doc);
      }
      workPanel.dailyItemsPanel.notesControlPane.refresh();

    } catch (Exception exc) {
      exc.printStackTrace();
    }
  }

}