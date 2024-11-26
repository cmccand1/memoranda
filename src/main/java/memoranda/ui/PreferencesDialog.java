package memoranda.ui;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import memoranda.ui.resources.ResourceTypePanel;
import memoranda.util.Configuration;
import memoranda.storage.CurrentStorage;
import memoranda.util.Local;
import memoranda.util.MimeTypesList;

import java.awt.event.*;

public class PreferencesDialog extends JDialog {

  JPanel topPanel = new JPanel(new BorderLayout());
  JTabbedPane tabbedPanel = new JTabbedPane();
  JPanel generalPanel = new JPanel(new GridBagLayout());
  GridBagConstraints gbc;

  JLabel windowMinimizeActionLabel = new JLabel();
  ButtonGroup minimizeGroup = new ButtonGroup();
  JRadioButton minimizeToTaskBarRadioButton = new JRadioButton();
  JRadioButton hideOnMinimizeRadioButton = new JRadioButton();

  ButtonGroup closeGroup = new ButtonGroup();
  JLabel windowCloseActionLabel = new JLabel();
  JRadioButton exitOnCloseRadioButton = new JRadioButton();
  JRadioButton hideOnCloseRadionButton = new JRadioButton();

  JLabel lookAndFeelLabel = new JLabel();
  ButtonGroup lookAndFeelGroup = new ButtonGroup();
  JRadioButton systemLookAndFeelRadioButton = new JRadioButton();
  JRadioButton javaLookAndFeelRadioButton = new JRadioButton();
  JRadioButton customLookAndFeelRadioButton = new JRadioButton();
  JLabel lookAndFeelClassNameLabel = new JLabel();
  JCheckBox confirmOnExitCheckBox = new JCheckBox();
  JTextField lookAndFeelClassName = new JTextField();

  JLabel startupLabel = new JLabel();
  JCheckBox enableSystrayCheckBox = new JCheckBox();
  JCheckBox startMinimizedCheckBox = new JCheckBox();
  JCheckBox enableSplashCheckBox = new JCheckBox();
  JCheckBox enableL10nCheckBox = new JCheckBox();
  JCheckBox firstDayOfWeekMondayCheckBox = new JCheckBox();

  JPanel resourcePanel = new JPanel(new BorderLayout());
  ResourceTypePanel resourceTypePanel = new ResourceTypePanel();
  Border rstPanelBorder;
  JPanel rsBottomPanel = new JPanel(new GridBagLayout());
  TitledBorder rsbpBorder;
  JButton okButton = new JButton();
  JButton cancelButton = new JButton();
  JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
  JLabel pathLabel = new JLabel();
  JTextField browserPath = new JTextField();
  JButton browseButton = new JButton();
  JLabel exitLabel = new JLabel();

  JPanel soundPanel = new JPanel();
  JCheckBox enableSoundCheckBox = new JCheckBox();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  ButtonGroup soundGroup = new ButtonGroup();
  JPanel jPanel2 = new JPanel();
  JButton soundFileBrowseButton = new JButton();
  GridLayout gridLayout1 = new GridLayout();
  JPanel jPanel1 = new JPanel();
  JRadioButton soundBeepRadioButton = new JRadioButton();
  JLabel soundFileLabel = new JLabel();
  JTextField soundFile = new JTextField();
  JRadioButton defaultSoundRadioButton = new JRadioButton();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel jPanel3 = new JPanel();
  JRadioButton customSoundRadioButton = new JRadioButton();
  BorderLayout borderLayout2 = new BorderLayout();

  JPanel editorConfigPanel = new JPanel(new BorderLayout());
  JPanel econfPanel = new JPanel(new GridLayout(5, 2));
  Vector fontNames = getFontNames();
  JComboBox normalFontComboBox = new JComboBox(fontNames);
  JComboBox headerFontComboBox = new JComboBox(fontNames);
  JComboBox monoFontComboBox = new JComboBox(fontNames);
  JSpinner baseFontSize = new JSpinner();
  JCheckBox antiAliasCheckBox = new JCheckBox();
  JLabel normalFontLabel = new JLabel();
  JLabel headerFontLabel = new JLabel();
  JLabel monoFontLabel = new JLabel();
  JLabel baseFontSizeLabel = new JLabel();

  public PreferencesDialog(Frame frame) {
    super(frame, Local.getString("Preferences"), true);
    try {
      jbInit();
    } catch (Exception ex) {
      new ExceptionDialog(ex);
    }
  }

  public PreferencesDialog() {
    this(null);
  }

  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(
        Color.white, new Color(156, 156, 158)), Local
        .getString("Sound"));
    this.setResizable(false);
    // Build Tab1
    windowMinimizeActionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    windowMinimizeActionLabel.setText(Local.getString("Window minimize action:"));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 10, 0, 15);
    gbc.anchor = GridBagConstraints.EAST;
    enableSoundCheckBox.setText(Local.getString("Enable sound notifications"));
    enableSoundCheckBox.addActionListener(this::enableSoundCB_actionPerformed);
    soundPanel.setLayout(borderLayout1);
    soundFileBrowseButton.setText(Local.getString("Browse"));
    soundFileBrowseButton.addActionListener(this::soundFileBrowseB_actionPerformed);
    gridLayout1.setRows(4);
    jPanel1.setBorder(titledBorder1);
    jPanel1.setLayout(gridLayout1);
    soundBeepRadioButton.setText(Local.getString("System beep"));
    soundBeepRadioButton.addActionListener(this::soundBeepRB_actionPerformed);
    soundFileLabel.setText(Local.getString("Sound file") + ":");
    defaultSoundRadioButton.setText(Local.getString("Default"));
    defaultSoundRadioButton.addActionListener(this::soundDefaultRB_actionPerformed);
    jPanel3.setLayout(borderLayout3);
    customSoundRadioButton.setText(Local.getString("Custom"));
    customSoundRadioButton.addActionListener(this::soundCustomRB_actionPerformed);
    jPanel2.setLayout(borderLayout2);
    soundPanel.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(defaultSoundRadioButton, null);
    jPanel1.add(soundBeepRadioButton, null);
    jPanel1.add(customSoundRadioButton, null);
    this.soundGroup.add(defaultSoundRadioButton);
    this.soundGroup.add(soundBeepRadioButton);
    this.soundGroup.add(customSoundRadioButton);
    jPanel1.add(jPanel3, null);
    jPanel3.add(soundFile, BorderLayout.CENTER);
    jPanel3.add(soundFileBrowseButton, BorderLayout.EAST);
    jPanel3.add(soundFileLabel, BorderLayout.WEST);
    generalPanel.add(windowMinimizeActionLabel, gbc);
    minimizeGroup.add(minimizeToTaskBarRadioButton);
    minimizeToTaskBarRadioButton.setSelected(true);
    minimizeToTaskBarRadioButton.setText(Local.getString("Minimize to taskbar"));
    minimizeToTaskBarRadioButton.addActionListener(this::minimizeToTaskbarRB_actionPerformed);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(minimizeToTaskBarRadioButton, gbc);
    minimizeGroup.add(hideOnMinimizeRadioButton);
    hideOnMinimizeRadioButton.setText(Local.getString("Hide"));
    hideOnMinimizeRadioButton.addActionListener(this::hideOnMinimizeRB_actionPerformed);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(hideOnMinimizeRadioButton, gbc);
    windowCloseActionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    windowCloseActionLabel.setText(Local.getString("Window close action:"));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(2, 10, 0, 15);
    gbc.anchor = GridBagConstraints.EAST;
    generalPanel.add(windowCloseActionLabel, gbc);
    closeGroup.add(exitOnCloseRadioButton);
    exitOnCloseRadioButton.setSelected(true);
    exitOnCloseRadioButton.setText(Local.getString("Close and exit"));
    exitOnCloseRadioButton.addActionListener(this::closeOnExitRB_actionPerformed);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(exitOnCloseRadioButton, gbc);

    closeGroup.add(hideOnCloseRadionButton);
    hideOnCloseRadionButton.setText(Local.getString("Hide"));
    hideOnCloseRadionButton.addActionListener(this::hideOnCloseRB_actionPerformed);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(hideOnCloseRadionButton, gbc);
    lookAndFeelLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    lookAndFeelLabel.setText(Local.getString("Look and feel:"));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.insets = new Insets(2, 10, 0, 15);
    gbc.anchor = GridBagConstraints.EAST;
    generalPanel.add(lookAndFeelLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(systemLookAndFeelRadioButton, gbc);
    lookAndFeelGroup.add(systemLookAndFeelRadioButton);
    systemLookAndFeelRadioButton.setText(Local.getString("System"));
    systemLookAndFeelRadioButton.addActionListener(this::onSelectSystemLookAndFeel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(javaLookAndFeelRadioButton, gbc);
    lookAndFeelGroup.add(javaLookAndFeelRadioButton);
    javaLookAndFeelRadioButton.setText(Local.getString("Java"));
    javaLookAndFeelRadioButton.addActionListener(this::onSelectJavaLookAndFeel);

    lookAndFeelGroup.add(customLookAndFeelRadioButton);
    customLookAndFeelRadioButton.setText(Local.getString("Custom"));
    customLookAndFeelRadioButton.addActionListener(this::onSelectCusomLookAndFeel);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 7;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(customLookAndFeelRadioButton, gbc);
    lookAndFeelClassNameLabel.setEnabled(false);
    lookAndFeelClassNameLabel.setText(Local.getString("L&F class name:"));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 8;
    gbc.insets = new Insets(2, 20, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(lookAndFeelClassNameLabel, gbc);
    lookAndFeelClassName.setEnabled(false);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 9;
    gbc.insets = new Insets(7, 20, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    generalPanel.add(lookAndFeelClassName, gbc);
    startupLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    startupLabel.setText(Local.getString("Startup:"));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 10;
    gbc.insets = new Insets(2, 10, 0, 15);
    gbc.anchor = GridBagConstraints.EAST;
    generalPanel.add(startupLabel, gbc);
    enableSystrayCheckBox.setText(Local.getString("Enable system tray icon"));
    enableSystrayCheckBox.addActionListener(this::onEnableSystray);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 10;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(enableSystrayCheckBox, gbc);
    startMinimizedCheckBox.setText(Local.getString("Start minimized"));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 11;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(startMinimizedCheckBox, gbc);
    enableSplashCheckBox.setText(Local.getString("Show splash screen"));
    enableSplashCheckBox.addActionListener(this::onEnableSplash);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 12;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(enableSplashCheckBox, gbc);
    enableL10nCheckBox.setText(Local.getString("Enable localization"));
    enableL10nCheckBox.addActionListener(this::onEnableL10N);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 13;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(enableL10nCheckBox, gbc);
    firstDayOfWeekMondayCheckBox.setText(Local.getString("First day of week - Monday"));
    firstDayOfWeekMondayCheckBox.addActionListener(e -> {
    });
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 14;
    gbc.insets = new Insets(2, 0, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(firstDayOfWeekMondayCheckBox, gbc);
    exitLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    exitLabel.setText(Local.getString("Exit") + ":");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 15;
    gbc.insets = new Insets(2, 10, 10, 15);
    gbc.anchor = GridBagConstraints.EAST;
    generalPanel.add(exitLabel, gbc);
    confirmOnExitCheckBox.setSelected(true);
    confirmOnExitCheckBox.setText(Local.getString("Ask confirmation"));
    confirmOnExitCheckBox.addActionListener(this::confirmOnExitRB_actionPerformed);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 15;
    gbc.insets = new Insets(2, 0, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    generalPanel.add(confirmOnExitCheckBox, gbc);

    // Build Tab2
    rstPanelBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    resourceTypePanel.setBorder(rstPanelBorder);
    resourcePanel.add(resourceTypePanel, BorderLayout.CENTER);
    rsbpBorder = new TitledBorder(BorderFactory.createEmptyBorder(5, 5, 5,
        5), Local.getString("Web browser executable"));
    rsBottomPanel.setBorder(rsbpBorder);
    pathLabel.setText(Local.getString("Path") + ":");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 5, 0, 5);
    gbc.anchor = GridBagConstraints.WEST;
    rsBottomPanel.add(pathLabel, gbc);
    browserPath.setPreferredSize(new Dimension(250, 25));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 5, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    rsBottomPanel.add(browserPath, gbc);
    browseButton.setText(Local.getString("Browse"));
    browseButton.setPreferredSize(new Dimension(110, 25));
    browseButton.addActionListener(this::browseB_actionPerformed);
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    // gbc.insets = new Insets(0, 0, 0, 0);
    gbc.anchor = GridBagConstraints.EAST;
    rsBottomPanel.add(browseButton, gbc);

    resourcePanel.add(rsBottomPanel, BorderLayout.SOUTH);

    // Build editorConfigPanel
    normalFontLabel.setText(Local.getString("Normal text font"));
    normalFontLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    headerFontLabel.setText(Local.getString("Header font"));
    headerFontLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    monoFontLabel.setText(Local.getString("Monospaced font"));
    monoFontLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    baseFontSizeLabel.setText(Local.getString("Base font size"));
    baseFontSizeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    antiAliasCheckBox.setText(Local.getString("Antialias text"));
    JPanel bfsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    bfsPanel.add(baseFontSize);
    econfPanel.add(normalFontLabel);
    econfPanel.add(normalFontComboBox);
    econfPanel.add(headerFontLabel);
    econfPanel.add(headerFontComboBox);
    econfPanel.add(monoFontLabel);
    econfPanel.add(monoFontComboBox);
    econfPanel.add(baseFontSizeLabel);
    econfPanel.add(bfsPanel);
    econfPanel.add(antiAliasCheckBox);
    econfPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
    ((GridLayout) econfPanel.getLayout()).setHgap(10);
    ((GridLayout) econfPanel.getLayout()).setVgap(5);
    editorConfigPanel.add(econfPanel, BorderLayout.NORTH);
    // Build TabbedPanel
    tabbedPanel.add(generalPanel, Local.getString("General"));
    tabbedPanel.add(resourcePanel, Local.getString("Resource types"));
    tabbedPanel.add(soundPanel, Local.getString("Sound"));
    tabbedPanel.add(editorConfigPanel, Local.getString("Editor"));

    // Build TopPanel
    topPanel.add(tabbedPanel, BorderLayout.CENTER);

    // Build BottomPanel
    okButton.setMaximumSize(new Dimension(100, 25));
    okButton.setPreferredSize(new Dimension(100, 25));
    okButton.setText(Local.getString("Ok"));
    okButton.addActionListener(this::okB_actionPerformed);
    this.getRootPane().setDefaultButton(okButton);
    bottomPanel.add(okButton);
    cancelButton.setMaximumSize(new Dimension(100, 25));
    cancelButton.setPreferredSize(new Dimension(100, 25));
    cancelButton.setText(Local.getString("Cancel"));
    cancelButton.addActionListener(this::cancelB_actionPerformed);
    bottomPanel.add(cancelButton);

    // Build Preferences-Dialog
    getContentPane().add(topPanel, BorderLayout.NORTH);
    getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    soundPanel.add(enableSoundCheckBox, BorderLayout.NORTH);

    // set all config-values
    setValuesFromConfig();
  }

  void setValuesFromConfig() {
    // window minimize action
    if (Configuration.get("ON_MINIMIZE").toString().equalsIgnoreCase("normal")) {
      this.minimizeToTaskBarRadioButton.setSelected(true);
    } else {
      this.hideOnMinimizeRadioButton.setSelected(true);
    };

    // window close action
    String onClose = Configuration.get("ON_CLOSE").toString();
    if (onClose.equals("exit")) {
      this.exitOnCloseRadioButton.setSelected(true);
      // this.askConfirmChB.setEnabled(true);
    } else {
      this.hideOnCloseRadionButton.setSelected(true);
      // this.askConfirmChB.setEnabled(false);
    }

    // look and feel
    enableCustomLookAndFeel(false);
    String lookAndFeel = Configuration.get("LOOK_AND_FEEL").toString();
    switch (lookAndFeel) {
      case "system" -> systemLookAndFeelRadioButton.setSelected(true);
      case "custom" -> {
        customLookAndFeelRadioButton.setSelected(true);
        enableCustomLookAndFeel(true);
        lookAndFeelClassName.setText(lookAndFeel);
      }
      default -> javaLookAndFeelRadioButton.setSelected(true);
    }

    // startup
    enableSystrayCheckBox.setSelected(!Configuration.get("DISABLE_SYSTRAY").toString().equalsIgnoreCase("yes"));
    startMinimizedCheckBox.setSelected(Configuration.get("START_MINIMIZED").toString().equalsIgnoreCase("yes"));
    enableSplashCheckBox.setSelected(Configuration.get("SHOW_SPLASH").toString().equalsIgnoreCase("yes"));
    enableL10nCheckBox.setSelected(!Configuration.get("DISABLE_L10N").toString().equalsIgnoreCase("yes"));
    firstDayOfWeekMondayCheckBox.setSelected(Configuration.get("FIRST_DAY_OF_WEEK").toString().equalsIgnoreCase("mon"));
    confirmOnExitCheckBox.setSelected(!Configuration.get("ASK_ON_EXIT").toString().equalsIgnoreCase("no"));



    if (!System.getProperty("os.name").startsWith("Win")) {
      this.browserPath.setText(MimeTypesList.getAppList()
          .getBrowserExec());
    }
    if (Configuration.get("NOTIFY_SOUND").equals("")) {
      Configuration.put("NOTIFY_SOUND", "DEFAULT");
    }

    boolean enableSnd = !Configuration.get("NOTIFY_SOUND").toString().equalsIgnoreCase("DISABLED");
    enableSoundCheckBox.setSelected(enableSnd);
    if (Configuration.get("NOTIFY_SOUND").toString().equalsIgnoreCase("DEFAULT")
        || Configuration.get("NOTIFY_SOUND").toString().equalsIgnoreCase("DISABLED")) {
      this.defaultSoundRadioButton.setSelected(true);
      this.enableCustomSound(false);
    } else if (Configuration.get("NOTIFY_SOUND").toString().equalsIgnoreCase("BEEP")) {
      this.soundBeepRadioButton.setSelected(true);
      this.enableCustomSound(false);
    } else {
      System.out.println(Configuration.get("NOTIFY_SOUND").toString());
      this.customSoundRadioButton.setSelected(true);
      this.soundFile.setText(Configuration.get("NOTIFY_SOUND").toString());
      this.enableCustomSound(true);
    }
    this.enableSound(enableSnd);

    antiAliasCheckBox.setSelected(Configuration.get("ANTIALIAS_TEXT").toString().equalsIgnoreCase("yes"));
    if (!Configuration.get("NORMAL_FONT").toString().isEmpty()) {
      normalFontComboBox.setSelectedItem(Configuration.get("NORMAL_FONT").toString());
    } else {
      normalFontComboBox.setSelectedItem("serif");
    }

    if (!Configuration.get("HEADER_FONT").toString().isEmpty()) {
      headerFontComboBox.setSelectedItem(Configuration.get("HEADER_FONT").toString());
    } else {
      headerFontComboBox.setSelectedItem("sans-serif");
    }

    if (!Configuration.get("MONO_FONT").toString().isEmpty()) {
      monoFontComboBox.setSelectedItem(Configuration.get("MONO_FONT").toString());
    } else {
      monoFontComboBox.setSelectedItem("monospaced");
    }

    if (!Configuration.get("BASE_FONT_SIZE").toString().isEmpty()) {
      baseFontSize.setValue(Integer.decode(Configuration.get("BASE_FONT_SIZE").toString()));
    } else {
      baseFontSize.setValue(16);
    }
  }

  void apply() {
    // window minimize actions
    if (this.minimizeToTaskBarRadioButton.isSelected()) {
      Configuration.put("ON_MINIMIZE", "normal");
    } else {
      Configuration.put("ON_MINIMIZE", "hide");
    }

    // window close actions
    if (this.exitOnCloseRadioButton.isSelected()) {
      Configuration.put("ON_CLOSE", "exit");
    } else {
      Configuration.put("ON_CLOSE", "minimize");
    }

    // look and feel actions
    String lookAndFeel = Configuration.get("LOOK_AND_FEEL").toString();
    String newLookAndFeel = "";

    if (this.systemLookAndFeelRadioButton.isSelected()) {
      newLookAndFeel = "system";
    } else if (this.javaLookAndFeelRadioButton.isSelected()) {
      newLookAndFeel = "default";
    } else if (this.customLookAndFeelRadioButton.isSelected()) {
      newLookAndFeel = this.lookAndFeelClassName.getText();
    }

    if (!lookAndFeel.equalsIgnoreCase(newLookAndFeel)) {
      Configuration.put("LOOK_AND_FEEL", newLookAndFeel);
      try {
        if (Configuration.get("LOOK_AND_FEEL").equals("system")) {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } else if (Configuration.get("LOOK_AND_FEEL").equals("default")) {
          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } else if (!Configuration.get("LOOK_AND_FEEL").toString().isEmpty()) {
          UIManager.setLookAndFeel(Configuration.get("LOOK_AND_FEEL").toString());
        }

        SwingUtilities.updateComponentTreeUI(App.getMainAppFrame());

      } catch (Exception e) {
        Configuration.put("LOOK_AND_FEEL", lookAndFeel);
        new ExceptionDialog(
            e,
            "Error when initializing a pluggable look-and-feel. Default LF will be used.",
            "Make sure that specified look-and-feel library classes are on the CLASSPATH.");
      }
    }

    // startup actions
    if (this.enableSystrayCheckBox.isSelected()) {
      Configuration.put("DISABLE_SYSTRAY", "no");
    } else {
      Configuration.put("DISABLE_SYSTRAY", "yes");
    }

    if (this.startMinimizedCheckBox.isSelected()) {
      Configuration.put("START_MINIMIZED", "yes");
    } else {
      Configuration.put("START_MINIMIZED", "no");
    }


    if (this.enableSplashCheckBox.isSelected()) {
      Configuration.put("SHOW_SPLASH", "yes");
    } else {
      Configuration.put("SHOW_SPLASH", "no");
    }

    if (this.enableL10nCheckBox.isSelected()) {
      Configuration.put("DISABLE_L10N", "no");
    } else {
      Configuration.put("DISABLE_L10N", "yes");
    }

    if (this.firstDayOfWeekMondayCheckBox.isSelected()) {
      Configuration.put("FIRST_DAY_OF_WEEK", "mon");
    } else {
      Configuration.put("FIRST_DAY_OF_WEEK", "sun");
    }

    if (this.confirmOnExitCheckBox.isSelected()) {
      Configuration.put("ASK_ON_EXIT", "yes");
    } else {
      Configuration.put("ASK_ON_EXIT", "no");
    }

    String brPath = this.browserPath.getText();
    if (new java.io.File(brPath).isFile()) {
      MimeTypesList.getAppList().setBrowserExec(brPath);
      CurrentStorage.get().storeMimeTypesList();
    }

    if (!this.enableSoundCheckBox.isSelected()) {
      Configuration.put("NOTIFY_SOUND", "DISABLED");
    } else if (this.defaultSoundRadioButton.isSelected()) {
      Configuration.put("NOTIFY_SOUND", "DEFAULT");
    } else if (this.soundBeepRadioButton.isSelected()) {
      Configuration.put("NOTIFY_SOUND", "BEEP");
    } else if ((this.customSoundRadioButton.isSelected())
        && (!this.soundFile.getText().trim().isEmpty())) {
      Configuration.put("NOTIFY_SOUND", this.soundFile.getText().trim());
    }

    if (antiAliasCheckBox.isSelected()) {
      Configuration.put("ANTIALIAS_TEXT", "yes");
    } else {
      Configuration.put("ANTIALIAS_TEXT", "no");
    }

    Configuration.put("NORMAL_FONT", normalFontComboBox.getSelectedItem());
    Configuration.put("HEADER_FONT", headerFontComboBox.getSelectedItem());
    Configuration.put("MONO_FONT", monoFontComboBox.getSelectedItem());
    Configuration.put("BASE_FONT_SIZE", baseFontSize.getValue());
    App.getMainAppFrame().workPanel.dailyItemsPanel.editorPanel.editor.editor.setAntiAlias(
        antiAliasCheckBox.isSelected());
    App.getMainAppFrame().workPanel.dailyItemsPanel.editorPanel.initCSS();
    App.getMainAppFrame().workPanel.dailyItemsPanel.editorPanel.editor.repaint();

    Configuration.saveConfig();

  }

  void enableCustomLookAndFeel(boolean is) {
    this.lookAndFeelClassNameLabel.setEnabled(is);
    this.lookAndFeelClassName.setEnabled(is);
  }

  void enableCustomSound(boolean is) {
    this.soundFile.setEnabled(is);
    this.soundFileBrowseButton.setEnabled(is);
    this.soundFileLabel.setEnabled(is);
  }

  void enableSound(boolean is) {
    this.defaultSoundRadioButton.setEnabled(is);
    this.soundBeepRadioButton.setEnabled(is);
    this.customSoundRadioButton.setEnabled(is);
    enableCustomSound(is);

    this.soundFileBrowseButton.setEnabled(is && customSoundRadioButton.isSelected());
    this.soundFile.setEnabled(is && customSoundRadioButton.isSelected());
    this.soundFileLabel.setEnabled(is && customSoundRadioButton.isSelected());

  }

  void okB_actionPerformed(ActionEvent e) {
    apply();
    this.dispose();
  }

  void cancelB_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  void minimizeToTaskbarRB_actionPerformed(ActionEvent e) {

  }

  void hideOnMinimizeRB_actionPerformed(ActionEvent e) {

  }

  void closeOnExitRB_actionPerformed(ActionEvent e) {
    // this.askConfirmChB.setEnabled(true);
  }

  void confirmOnExitRB_actionPerformed(ActionEvent e) {

  }

  void hideOnCloseRB_actionPerformed(ActionEvent e) {
    // this.askConfirmChB.setEnabled(false);
  }

  void onSelectSystemLookAndFeel(ActionEvent e) {
    this.enableCustomLookAndFeel(false);
  }

  void onSelectJavaLookAndFeel(ActionEvent e) {
    this.enableCustomLookAndFeel(false);
  }

  void onSelectCusomLookAndFeel(ActionEvent e) {
    this.enableCustomLookAndFeel(true);
  }

  void onEnableSystray(ActionEvent e) {

  }

  void onEnableSplash(ActionEvent e) {

  }

  void onEnableL10N(ActionEvent e) {

  }

  void browseB_actionPerformed(ActionEvent e) {
    // Fix until Sun's JVM supports more locales...
    UIManager.put("FileChooser.lookInLabelText", Local
        .getString("Look in:"));
    UIManager.put("FileChooser.upFolderToolTipText", Local
        .getString("Up One Level"));
    UIManager.put("FileChooser.newFolderToolTipText", Local
        .getString("Create New Folder"));
    UIManager.put("FileChooser.listViewButtonToolTipText", Local
        .getString("List"));
    UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
        .getString("Details"));
    UIManager.put("FileChooser.fileNameLabelText", Local
        .getString("File Name:"));
    UIManager.put("FileChooser.filesOfTypeLabelText", Local
        .getString("Files of Type:"));
    UIManager.put("FileChooser.openButtonText", Local.getString("Open"));
    UIManager.put("FileChooser.openButtonToolTipText", Local
        .getString("Open selected file"));
    UIManager
        .put("FileChooser.cancelButtonText", Local.getString("Cancel"));
    UIManager.put("FileChooser.cancelButtonToolTipText", Local
        .getString("Cancel"));

    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);
    chooser.setDialogTitle(Local
        .getString("Select the web-browser executable"));
    chooser.setAcceptAllFileFilterUsed(true);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setPreferredSize(new Dimension(550, 375));
    if (System.getProperty("os.name").startsWith("Win")) {
      chooser.setFileFilter(new AllFilesFilter(AllFilesFilter.EXE));
      chooser.setCurrentDirectory(new File("C:\\Program Files"));
    }
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      this.browserPath.setText(chooser.getSelectedFile().getPath());
    }
  }

  void enableSoundCB_actionPerformed(ActionEvent e) {
    enableSound(enableSoundCheckBox.isSelected());
  }

  void soundFileBrowseB_actionPerformed(ActionEvent e) {
    // Fix until Sun's JVM supports more locales...
    UIManager.put("FileChooser.lookInLabelText", Local
        .getString("Look in:"));
    UIManager.put("FileChooser.upFolderToolTipText", Local
        .getString("Up One Level"));
    UIManager.put("FileChooser.newFolderToolTipText", Local
        .getString("Create New Folder"));
    UIManager.put("FileChooser.listViewButtonToolTipText", Local
        .getString("List"));
    UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
        .getString("Details"));
    UIManager.put("FileChooser.fileNameLabelText", Local
        .getString("File Name:"));
    UIManager.put("FileChooser.filesOfTypeLabelText", Local
        .getString("Files of Type:"));
    UIManager.put("FileChooser.openButtonText", Local.getString("Open"));
    UIManager.put("FileChooser.openButtonToolTipText", Local
        .getString("Open selected file"));
    UIManager
        .put("FileChooser.cancelButtonText", Local.getString("Cancel"));
    UIManager.put("FileChooser.cancelButtonToolTipText", Local
        .getString("Cancel"));

    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);
    chooser.setDialogTitle(Local.getString("Select the sound file"));
    chooser.setAcceptAllFileFilterUsed(true);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setPreferredSize(new Dimension(550, 375));
    chooser.setFileFilter(new AllFilesFilter(AllFilesFilter.WAV));
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      this.soundFile.setText(chooser.getSelectedFile().getPath());
    }
  }

  void soundDefaultRB_actionPerformed(ActionEvent e) {
    this.enableCustomSound(false);
  }

  void soundBeepRB_actionPerformed(ActionEvent e) {
    this.enableCustomSound(false);
  }

  void soundCustomRB_actionPerformed(ActionEvent e) {
    this.enableCustomSound(true);
  }

  Vector getFontNames() {
    GraphicsEnvironment gEnv =
        GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] envfonts = gEnv.getAvailableFontFamilyNames();
    Vector fonts = new Vector();
    fonts.add("serif");
    fonts.add("sans-serif");
    fonts.add("monospaced");
    fonts.addAll(Arrays.asList(envfonts));
    return fonts;
  }
}