/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class PictureBoxSupport extends JPanel {

	public PictureBoxSupport () {
		this.setMinimumSize(new Dimension(200,200));
	}

	public void setBorderStyle(Border b) {
		System.out.println("Picture Box Support no op");
	}

	public void setSizeMode(int i) {
		System.out.println("Picture Box Support no op");
	}
	

}
