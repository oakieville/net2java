/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TabPageSupport extends PanelSupport {
	private String text;
// not used
	public void addOne(JComponent comp) {
		this.add(comp);
	}

	public void setText(String s) {
		this.text = s;
	}

	public String getText() {
		return text;
	}


}
