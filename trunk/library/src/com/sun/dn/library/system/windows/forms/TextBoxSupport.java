/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class TextBoxSupport extends JScrollPane {
	JTextArea ta = new JTextArea();

	public TextBoxSupport() {
                ta.setLineWrap(true);
		this.setViewportView(ta);
	}	

	public String getText() {
		return ta.getText();
	}

	public void setText(String s) {
		ta.setText(s);
	}
}
