/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/


package com.sun.dn.library.System.Windows.Forms;

import javax.swing.*;
import java.awt.*;

public class FileDialogSupport extends JFileChooser {
    
    /** Creates a new instance of FileDialogSupport */
    public FileDialogSupport() {
    }
    
  
    
    public static JFileChooser getOpenDialog() {
        JFileChooser jfc = new FileDialogSupport();
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        return jfc;
    }
    
     public static JFileChooser getSaveDialog() {
        JFileChooser jfc = new FileDialogSupport();
        jfc.setDialogType(JFileChooser.SAVE_DIALOG);
        return jfc;
    }
     
     public static void setFilterIndex(JFileChooser fc, int index) {
         // no op
     }
     
      public static int getFilterIndex(JFileChooser fc) {
         return 0;
     }
      
      public int showDialog(Component parent, String label)
               throws HeadlessException {
          String buttonText = "OK";
          if (this.getDialogType() == JFileChooser.SAVE_DIALOG) {
              buttonText = "Save";
          } else if (this.getDialogType() == JFileChooser.OPEN_DIALOG) {
              buttonText = "Open";
          }
          return super.showDialog(parent, buttonText);
      }
}

