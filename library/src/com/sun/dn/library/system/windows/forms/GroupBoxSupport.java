/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class GroupBoxSupport extends PanelSupport {
	TitledBorder tb;
	String text;
	
	public GroupBoxSupport() {
		tb = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "                 ");
		this.setBorder(tb);
		setLayout(new AbsoluteLayout());
	}

	public void setText(String text) {
		this.text = text;
		tb = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), text);
		this.setBorder(tb);
		this.doLayout();
	}

	public String getText() {
		return this.text;
	}

}
