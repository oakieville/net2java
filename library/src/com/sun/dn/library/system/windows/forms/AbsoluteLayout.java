/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class AbsoluteLayout implements LayoutManager {
	private java.util.List components = new ArrayList();

	public void addLayoutComponent(String name, Component comp) {
		System.out.println("Add component " + comp);
		components.add(comp);
		if (comp instanceof ButtonSupport) {
			System.out.println("Here");
			comp.setSize(comp.getPreferredSize());
		}
	}

	public void removeLayoutComponent(Component comp) {
		components.remove(comp);
	}

	public Dimension preferredLayoutSize(Container parent) {
		//System.out.println("Get preferred size " + parent);
		return parent.getSize();
	}

	public Dimension minimumLayoutSize(Container parent) {
		//System.out.println("Get minimum size " + parent);
		return parent.getMinimumSize();
	}

	public void layoutContainer(Container parent) {
		//System.out.println("Layout container");
		

	}

}
