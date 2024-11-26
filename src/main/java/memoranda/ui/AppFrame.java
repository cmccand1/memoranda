package memoranda.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
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

  ImageIcon image1;
  ImageIcon image2;
  ImageIcon image3;

  JPanel contentPane;

  JToolBar toolBar = new JToolBar();
  JButton jButton3 = new JButton();

  // The bottom status bar
  JLabel statusBar = new JLabel();

  BorderLayout frameLayout = new BorderLayout();
  JSplitPane splitPane = new JSplitPane();

  boolean isProjectsPanelExpanded = false;

  /**
   * The bottom panel withing the content pane
   */
  public WorkPanel workPanel = new WorkPanel();


  /**
   * The top panel with the list of projects
   */
  ProjectsPanel projectsPanel = new ProjectsPanel();


  HTMLEditor editor = workPanel.dailyItemsPanel.editorPanel.editor;

  static Vector exitListeners = new Vector();

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
  JMenu fileMenu = new JMenu();
  JMenu editMenu = new JMenu();
  JMenu insertMenu = new JMenu();
  JMenu FormatMenu = new JMenu();
  JMenu goMenu = new JMenu();
  JMenu helpMenu = new JMenu();

  /* =========================================================================================== */
  /**
   * File menu items
   */
  JMenuItem fileMenuExit = new JMenuItem();
  JMenuItem fileMenuNewProject = new JMenuItem();
  JMenuItem fileMenuNewNote = new JMenuItem(workPanel.dailyItemsPanel.editorPanel.newAction);
  JMenuItem fileMenuPackProject = new JMenuItem();
  JMenuItem fileMenuUnpackProject = new JMenuItem();
  JMenuItem jMenuFileExportPrj = new JMenuItem();
  JMenuItem jMenuFileImportPrj = new JMenuItem();
  JMenuItem fileMenuImportOneNote = new JMenuItem();
  JMenuItem fileMenuExportNote = new JMenuItem(
      workPanel.dailyItemsPanel.editorPanel.exportAction);
  JMenuItem fileMenuPreferences = new JMenuItem();
  JMenuItem fileMenuMinimize = new JMenuItem();


  JMenuItem jMenuItem1 = new JMenuItem(); // todo: this

  /* =========================================================================================== */
  /**
   * Edit menu items
   */
  JMenuItem blah = new JMenuItem();
  JMenuItem editMenuUndo = new JMenuItem(editor.undoAction);
  JMenuItem editMenuRedo = new JMenuItem(editor.redoAction);
  JMenuItem editMenuCut = new JMenuItem(editor.cutAction);
  JMenuItem editMenuCopy = new JMenuItem(editor.copyAction);
  JMenuItem editMenuPaste = new JMenuItem(editor.pasteAction);
  JMenuItem editMenuPasteSpecial = new JMenuItem(editor.stylePasteAction);
  JMenuItem editMenuSelectAll = new JMenuItem(editor.selectAllAction);
  JMenuItem editMenuFind = new JMenuItem(editor.findAction);

  /* =========================================================================================== */
  /**
   * Insert menu items
   */
  JMenuItem insertMenuImage = new JMenuItem(editor.imageAction);
  JMenuItem insertMenuTable = new JMenuItem(editor.tableAction);
  JMenuItem insertMenuLink = new JMenuItem(editor.linkAction);

  // Inner menu for inserting lists
  JMenu insertMenuList = new JMenu();
  JMenuItem insertMenuInsertListUL = new JMenuItem(editor.ulAction);
  JMenuItem insertMenuInsertListOL = new JMenuItem(editor.olAction);

  JMenuItem insertMenuLineBreak = new JMenuItem(editor.breakAction);
  JMenuItem insertMenuHorizontalRule = new JMenuItem(editor.insertHRAction);
  JMenuItem insertMenuSpecialChar = new JMenuItem(editor.insCharAction);
  JMenuItem insertMenuCurrentDate = new JMenuItem(
      workPanel.dailyItemsPanel.editorPanel.insertDateAction);
  JMenuItem insertMenuCurrentTime = new JMenuItem(
      workPanel.dailyItemsPanel.editorPanel.insertTimeAction);
  JMenuItem insertMenuFile = new JMenuItem(
      workPanel.dailyItemsPanel.editorPanel.importAction);
  JMenu jMenuInsertSpecial = new JMenu(); // todo: this is a duplicate?


  /* =========================================================================================== */
  /**
   * Format menu items
   */

  // Format Paragraph Style
  JMenu paragraphStyleMenu = new JMenu();
  JMenuItem paragraphStyleMenuP = new JMenuItem(editor.new BlockAction(editor.T_P,
      ""));
  JMenuItem paragraphStyleMenuH1 = new JMenuItem(editor.new BlockAction(editor.T_H1,
      ""));
  JMenuItem paragraphStyleMenuH2 = new JMenuItem(editor.new BlockAction(editor.T_H2,
      ""));
  JMenuItem paragraphStyleMenuH3 = new JMenuItem(editor.new BlockAction(editor.T_H3,
      ""));
  JMenuItem paragraphStyleMenuH4 = new JMenuItem(editor.new BlockAction(editor.T_H4,
      ""));
  JMenuItem paragraphStyleMenuH5 = new JMenuItem(editor.new BlockAction(editor.T_H5,
      ""));
  JMenuItem paragraphStyleMenuH6 = new JMenuItem(editor.new BlockAction(editor.T_H6,
      ""));
  JMenuItem paragraphStyleMenuPreformatted = new JMenuItem(editor.new BlockAction(
      editor.T_PRE, ""));
  JMenuItem paragraphStyleMenuBlockQuote = new JMenuItem(editor.new BlockAction(
      editor.T_BLOCKQ, ""));

  // Format Character Style
  JMenu characterStyleMenu = new JMenu();
  JMenuItem characterStyleMenuNormal = new JMenuItem(editor.new InlineAction(
      editor.I_NORMAL, ""));
  JMenuItem characterStyleMenuEM = new JMenuItem(editor.new InlineAction(
      editor.I_EM, ""));
  JMenuItem characterStyleMenuSTRONG = new JMenuItem(editor.new InlineAction(
      editor.I_STRONG, ""));
  JMenuItem characterStyleMenuCODE = new JMenuItem(editor.new InlineAction(
      editor.I_CODE, ""));
  JMenuItem characterStyleMenuCITE = new JMenuItem(editor.new InlineAction(
      editor.I_CITE, ""));
  JMenuItem characterStyleMenuSUPERSCRIPT = new JMenuItem(editor.new InlineAction(
      editor.I_SUPERSCRIPT, ""));
  JMenuItem characterStyleMenuSUBSCRIPT = new JMenuItem(editor.new InlineAction(
      editor.I_SUBSCRIPT, ""));
  JMenuItem characterStyleMenuCUSTOM = new JMenuItem(editor.new InlineAction(
      editor.I_CUSTOM, ""));
  JMenuItem characterStyleMenuBOLD = new JMenuItem(editor.boldAction);
  JMenuItem characterStyleMenuITALIC = new JMenuItem(editor.italicAction);
  JMenuItem characterStyleMenuUNDERLINE = new JMenuItem(editor.underAction);

  // Format Alignment Style
  JMenu alignmentStyleMenu = new JMenu();
  JMenuItem alignmentStyleAlignLeft = new JMenuItem(editor.lAlignAction);
  JMenuItem alignmentStyleAlignCenter = new JMenuItem(editor.cAlignAction);
  JMenuItem alignmentStyleAlignRight = new JMenuItem(editor.rAlignAction);

  // Format Table Style
  JMenu tableStyleMenu = new JMenu();
  JMenuItem tableStyleInsertRow = new JMenuItem(editor.insertTableRowAction);
  JMenuItem tableStyleInsertColumn = new JMenuItem(editor.insertTableCellAction);

  // Object properties
  JMenuItem formatMenuObjectProps = new JMenuItem(editor.propsAction);

  /* =========================================================================================== */
  /**
   * Go menu items
   */
  JMenuItem goMenuHistoryBack = new JMenuItem(History.historyBackAction);
  JMenuItem goMenuHistoryForward = new JMenuItem(History.historyForwardAction);

  JMenuItem goMenuDayBack = new JMenuItem(
      workPanel.dailyItemsPanel.calendar.dayBackAction);
  JMenuItem goMenuDayForward = new JMenuItem(
      workPanel.dailyItemsPanel.calendar.dayForwardAction);
  JMenuItem goMenuToday = new JMenuItem(
      workPanel.dailyItemsPanel.calendar.todayAction);

  /* =========================================================================================== */
  /**
   * Help menu items
   */
  JMenuItem helpMenuGuide = new JMenuItem();
  JMenuItem helpMenuWebsite = new JMenuItem();
  JMenuItem helpMenuBugReport = new JMenuItem();
  JMenuItem helpMenuAbout = new JMenuItem();


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

    // add action listeners
    addActionListeners();

    helpMenuAbout.setText(Local.getString("About Memoranda"));
    //jButton3.setIcon(image3);
    jButton3.setToolTipText(Local.getString("Help"));
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

    splitPane.setContinuousLayout(true);
    splitPane.setDividerSize(3);
    //splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(28);
    //projectsPanel.setMaximumSize(new Dimension(2147483647, 200));
    projectsPanel.setMinimumSize(new Dimension(10, 28));
    projectsPanel.setPreferredSize(new Dimension(10, 28));
        /*workPanel.setMinimumSize(new Dimension(734, 300));
         workPanel.setPreferredSize(new Dimension(1073, 300));*/
    splitPane.setDividerLocation(28);

    /**
     * File menu text setup
     */
    fileMenu.setText(Local.getString("File"));
    fileMenuExit.setText(Local.getString("Exit"));
    fileMenuNewProject.setAction(projectsPanel.newProjectAction);
    fileMenuUnpackProject.setText(Local.getString("Unpack project") + "...");
    fileMenuExportNote.setText(Local.getString("Export current note")
        + "...");
    fileMenuImportOneNote.setText(Local.getString("Import one note")
        + "...");
    fileMenuPackProject.setText(Local.getString("Pack project") + "...");
    fileMenuMinimize.setText(Local.getString("Close the window"));
    fileMenuMinimize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10,
        InputEvent.ALT_MASK));
    fileMenuPreferences.setText(Local.getString("Preferences") + "...");

    /**
     * Edit menu text setup
     */
    editMenu.setText(Local.getString("Edit"));
    editMenuUndo.setText(Local.getString("Undo"));
    editMenuUndo.setToolTipText(Local.getString("Undo"));
    editMenuRedo.setText(Local.getString("Redo"));
    editMenuRedo.setToolTipText(Local.getString("Redo"));
    editMenuCut.setText(Local.getString("Cut"));
    editMenuCut.setToolTipText(Local.getString("Cut"));
    editMenuCopy.setText((String) Local.getString("Copy"));
    editMenuCopy.setToolTipText(Local.getString("Copy"));
    editMenuPaste.setText(Local.getString("Paste"));
    editMenuPaste.setToolTipText(Local.getString("Paste"));
    editMenuPasteSpecial.setText(Local.getString("Paste special"));
    editMenuPasteSpecial.setToolTipText(Local.getString("Paste special"));
    editMenuSelectAll.setText(Local.getString("Select all"));
    editMenuFind.setText(Local.getString("Find & replace") + "...");


    /**
     * Insert menu text setup
     */
    insertMenu.setText(Local.getString("Insert"));
    insertMenuImage.setText(Local.getString("Image") + "...");
    insertMenuImage.setToolTipText(Local.getString("Insert Image"));
    insertMenuTable.setText(Local.getString("Table") + "...");
    insertMenuTable.setToolTipText(Local.getString("Insert Table"));
    insertMenuLink.setText(Local.getString("Hyperlink") + "...");
    insertMenuLink.setToolTipText(Local.getString("Insert Hyperlink"));

    insertMenuList.setText(Local.getString("List"));
    insertMenuInsertListUL.setText(Local.getString("Unordered"));
    insertMenuInsertListUL.setToolTipText(Local.getString("Insert Unordered"));
    insertMenuInsertListOL.setText(Local.getString("Ordered"));
    jMenuInsertSpecial.setText(Local.getString("Special")); // todo: this
    insertMenuLineBreak.setText(Local.getString("Line break"));
    insertMenuHorizontalRule.setText(Local.getString("Horizontal rule"));
    insertMenuInsertListOL.setToolTipText(Local.getString("Insert Ordered"));
    insertMenuSpecialChar.setText(Local.getString("Special character") + "...");
    insertMenuSpecialChar.setToolTipText(Local.getString(
        "Insert Special character"));
    insertMenuCurrentDate.setText(Local.getString("Current date"));
    insertMenuCurrentTime.setText(Local.getString("Current time"));
    insertMenuFile.setText(Local.getString("File") + "...");
    jMenuInsertSpecial.setText(Local.getString("Special")); // todo: this
    insertMenuLineBreak.setText(Local.getString("Line break"));
    insertMenuLineBreak.setToolTipText(Local.getString("Insert break"));
    insertMenuHorizontalRule.setText(Local.getString("Horizontal rule"));
    insertMenuHorizontalRule.setToolTipText(Local.getString("Insert Horizontal rule"));

    /**
     * Format menu text setup
     */
    FormatMenu.setText(Local.getString("Format"));
    paragraphStyleMenu.setText(Local.getString("Paragraph style"));
    paragraphStyleMenuP.setText(Local.getString("Paragraph"));
    paragraphStyleMenuH1.setText(Local.getString("Header") + " 1");
    paragraphStyleMenuH2.setText(Local.getString("Header") + " 2");
    paragraphStyleMenuH3.setText(Local.getString("Header") + " 3");
    paragraphStyleMenuH4.setText(Local.getString("Header") + " 4");
    paragraphStyleMenuH5.setText(Local.getString("Header") + " 5");
    paragraphStyleMenuH6.setText(Local.getString("Header") + " 6");
    paragraphStyleMenuPreformatted.setText(Local.getString("Preformatted text"));
    paragraphStyleMenuBlockQuote.setText(Local.getString("Blockquote"));

    characterStyleMenu.setText(Local.getString("Character style"));
    characterStyleMenuNormal.setText(Local.getString("Normal"));
    characterStyleMenuEM.setText(Local.getString("Emphasis"));
    characterStyleMenuSTRONG.setText(Local.getString("Strong"));
    characterStyleMenuCODE.setText(Local.getString("Code"));
    characterStyleMenuCITE.setText(Local.getString("Cite"));
    characterStyleMenuSUPERSCRIPT.setText(Local.getString("Superscript"));
    characterStyleMenuSUBSCRIPT.setText(Local.getString("Subscript"));
    characterStyleMenuCUSTOM.setText(Local.getString("Custom style") + "...");
    characterStyleMenuBOLD.setText(Local.getString("Bold"));
    characterStyleMenuBOLD.setToolTipText(Local.getString("Bold"));
    characterStyleMenuITALIC.setText(Local.getString("Italic"));
    characterStyleMenuITALIC.setToolTipText(Local.getString("Italic"));
    characterStyleMenuUNDERLINE.setText(Local.getString("Underline"));
    characterStyleMenuUNDERLINE.setToolTipText(Local.getString("Underline"));
    alignmentStyleMenu.setText(Local.getString("Alignment"));
    alignmentStyleAlignLeft.setText(Local.getString("Left"));
    alignmentStyleAlignLeft.setToolTipText(Local.getString("Left"));
    alignmentStyleAlignCenter.setText(Local.getString("Center"));
    alignmentStyleAlignCenter.setToolTipText(Local.getString("Center"));
    alignmentStyleAlignRight.setText(Local.getString("Right"));
    alignmentStyleAlignRight.setToolTipText(Local.getString("Right"));
    tableStyleMenu.setText(Local.getString("Table"));
    tableStyleInsertRow.setText(Local.getString("Insert row"));
    tableStyleInsertColumn.setText(Local.getString("Insert cell"));
    formatMenuObjectProps.setText(Local.getString("Object properties")
        + "...");
    formatMenuObjectProps.setToolTipText(Local.getString(
        "Object properties"));

    /**
     * Go menu text setup
     */
    goMenu.setText(Local.getString("Go"));
    goMenuHistoryBack.setText(Local.getString("History back"));
    goMenuHistoryBack.setToolTipText(Local.getString("History back"));
    goMenuHistoryForward.setText(Local.getString("History forward"));
    goMenuHistoryForward.setToolTipText(Local.getString("History forward"));
    goMenuDayBack.setText(Local.getString("One day back"));
    goMenuDayForward.setText(Local.getString("One day forward"));
    goMenuToday.setText(Local.getString("To today"));

    toolBar.add(jButton3); // todo: what is this?

    /**
     * Help menu text setup
     */
    helpMenu.setText(Local.getString("Help"));
    helpMenuGuide.setText(Local.getString("Online user's guide"));
    helpMenuGuide.setIcon(new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource(
        "/ui/icons/help.png"))));
    helpMenuWebsite.setText(Local.getString("Memoranda web site"));
    helpMenuWebsite.setIcon(new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource(
        "/ui/icons/web.png"))));
    helpMenuBugReport.setText(Local.getString("Report a bug"));

    /**
     * File menu additions
     */
    fileMenu.add(fileMenuNewProject);
    fileMenu.add(fileMenuNewNote);
    fileMenu.addSeparator();
    fileMenu.add(fileMenuPackProject);
    fileMenu.add(fileMenuUnpackProject);
    fileMenu.addSeparator();
    fileMenu.add(jMenuFileExportPrj);
    fileMenu.add(fileMenuExportNote);
    fileMenu.add(fileMenuImportOneNote);
    fileMenu.add(jMenuFileImportPrj);
    fileMenu.addSeparator();
    fileMenu.add(fileMenuPreferences);
    fileMenu.addSeparator();
    fileMenu.add(fileMenuMinimize);
    fileMenu.addSeparator();
    fileMenu.add(fileMenuExit);

    /**
     * Help menu additions
     */
    helpMenu.add(helpMenuGuide);
    helpMenu.add(helpMenuWebsite);
    helpMenu.add(helpMenuBugReport);
    helpMenu.addSeparator();
    helpMenu.add(helpMenuAbout);

    /**
     * Menu bar additions
     */
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    menuBar.add(insertMenu);
    menuBar.add(FormatMenu);
    menuBar.add(goMenu);
    menuBar.add(helpMenu);
    this.setJMenuBar(menuBar);

    /**
     * Add components to the frame
     */
//    contentPane.add(toolBar, BorderLayout.NORTH);
    contentPane.add(statusBar, BorderLayout.SOUTH);
    contentPane.add(splitPane, BorderLayout.CENTER);
    splitPane.add(projectsPanel, JSplitPane.TOP);
    splitPane.add(workPanel, JSplitPane.BOTTOM);

    /**
     //this.setSize(new Dimension(800, 500));
     this.setTitle("Memoranda - " + CurrentProject.get().getTitle());

     statusBar.setText(String.format(" Version: %s (Build %s )", App.VERSION_INFO, App.BUILD_INFO));
     * Edit menu setup
     */
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

    /**
     * Insert menu setup
     */
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

    /**
     * Format menu setup
     */
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

    /**
     * Go menu setup
     */
    goMenu.add(goMenuHistoryBack);
    goMenu.add(goMenuHistoryForward);
    goMenu.addSeparator();
    goMenu.add(goMenuDayBack);
    goMenu.add(goMenuDayForward);
    goMenu.add(goMenuToday);

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
    fileMenuNewNote.addActionListener(e -> workPanel.dailyItemsPanel.editorPanel.newAction.actionPerformed(e));
    fileMenuExit.addActionListener(e -> doExit());
    fileMenuPackProject.addActionListener(e -> doPrjPack());
    fileMenuUnpackProject.addActionListener(e -> doPrjUnPack());
    fileMenuMinimize.addActionListener(e -> doMinimize());
    fileMenuPreferences.addActionListener(e -> showPreferences());
    fileMenuExportNote.addActionListener(this::exportNotes_actionPerformed);
//    fileMenuImportNotes.addActionListener(this::importNotes_actionPerformed);
    fileMenuImportOneNote.addActionListener(this::importOneNote_actionPerformed);


    // Help actions
    helpMenuGuide.addActionListener(this::helpMenuGuide_actionPerformed);
    helpMenuWebsite.addActionListener(this::helpMenuWebsite_actionPerformed);
    helpMenuBugReport.addActionListener(this::helpMenuBugReport_actionPerformed);
    helpMenuAbout.addActionListener(this::helpMenuAbout_actionPerformed);
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
      Dimension frmSize = this.getSize();
      Point loc = this.getLocation();

      ExitConfirmationDialog dlg = new ExitConfirmationDialog(this, Local.getString("Exit"));
      dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
          (frmSize.height - dlg.getSize().height) / 2 + loc.y);
      dlg.setVisible(true);
      if (dlg.CANCELLED) {
        return;
      }
    }

    Context.put("FRAME_WIDTH", new Integer(this.getWidth()));
    Context.put("FRAME_HEIGHT", new Integer(this.getHeight()));
    Context.put("FRAME_XPOS", new Integer(this.getLocation().x));
    Context.put("FRAME_YPOS", new Integer(this.getLocation().y));
    exitNotify();
    System.exit(0);
  }

  public void doMinimize() {
    exitNotify();
    App.closeWindow();
  }

  //Help | About action performed
  public void helpMenuAbout_actionPerformed(ActionEvent e) {
    AppFrame_AboutBox dlg = new AppFrame_AboutBox(this);
    Dimension dlgSize = dlg.getSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
        (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.setVisible(true);
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
    for (Object exitListener : exitListeners) {
      ((ActionListener) exitListener).actionPerformed(null);
    }
  }

  public void setEnabledEditorMenus(boolean enabled) {
    this.editMenu.setEnabled(enabled);
    this.FormatMenu.setEnabled(enabled);
    this.insertMenu.setEnabled(enabled);
    this.fileMenuNewNote.setEnabled(enabled);
    this.fileMenuExportNote.setEnabled(enabled);
  }

  public void doPrjPack() {
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

  public void doPrjUnPack() {
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