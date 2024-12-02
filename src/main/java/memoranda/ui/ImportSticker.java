package memoranda.ui;

import javax.swing.JOptionPane;

import memoranda.util.Local;

public class ImportSticker {

  String name;

  public ImportSticker(String x) {
    name = x;
  }

  public boolean import_file() {

    JOptionPane.showMessageDialog(null, Local.getString("Sorry, but Memoranda can't import your document yet!"));
    return true;
  }


}