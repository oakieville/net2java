/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PanelSupport extends JPanel {
	private String text;
	ButtonGroup bg = new ButtonGroup();
// not used
	public void addAll(JComponent[] o) {
		for (int i = 0; i < o.length; i++) {
			this.add(o[i]);
		}
	}
// not used
	public Component add(Component c) {
		//System.out.println("Add " + c);
		if (c instanceof AbstractButton) {
			//System.out.println("Add to button group " + c);
			c.setVisible(true);
			bg.add((AbstractButton)c);
		}
		if (c instanceof Anchorable && ((Anchorable) c).isAnchored()) {
			c.setSize(c.getPreferredSize());
		}
		return super.add(c);
	}

	public void resumeLayout(boolean b) {
		//this.doAnchoredComponents();	
		this.doLayout();
	}

	public void suspendLayout() {
		this.setLayout(new AbsoluteLayout());
	}

	public void setParent(JComponent c) {
		Container oldParent = this.getParent();
		oldParent.remove(this);
		c.add(this);
		//System.out.println("Switched parents from " + oldParent + " to " + c);
	}

	

}
