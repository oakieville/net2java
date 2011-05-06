/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TabControlSupport extends JTabbedPane {

	public TabControlSupport () {
		//this.setMinimumSize(new Dimension(200,200));
	}

	public void setAutoScaleBaseSize(Point d) {
		System.out.println("Library: TabControlSupport does not implement setAutoScaleBaseSize(Point d)");
	}

	public void resumeLayout(boolean b) {
		for (int i = 0; i < this.getTabCount(); i++) {
			TabPageSupport tps = (TabPageSupport) this.getComponentAt(i);
			this.setTitleAt(i, tps.getText());
			//System.out.println("done " + tps.getText());
		}
	}

	//public void setSelectedIndex(int i) {
	//	System.out.println("HELLO"); // ok this next bit is weird
		//super.fireStateChanged();
	//	super.setSelectedIndex(i);
	//}

	public void suspendLayout() {
		//System.out.println("Library: TabControlSupport does not implement resumeLayout(boolean b)");
	}

	protected void Dispose(boolean b) {
		System.out.println("Library: TabControlSupport does not implement Dispose(boolean b)");

	}
// not used
	public void addAll(Object[] o) {
		for (int i = 0; i < o.length; i++) {
			Component c = (Component) o[i];
			this.add(c);
		}
	}
	

}
