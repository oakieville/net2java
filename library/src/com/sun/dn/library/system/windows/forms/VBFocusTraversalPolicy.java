/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;


public class VBFocusTraversalPolicy extends FocusTraversalPolicy { 

	public static void setOrder(Component c, int order) {
		// (dannyc)
	}

	public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
		return null;
	}

 	public Component getComponentBefore(Container focusCycleRoot, Component aComponent) { 
		return null;
	}

	
	public Component getFirstComponent(Container focusCycleRoot) {
		return null;
	}

	public Component getLastComponent(Container focusCycleRoot) {

		return null;
	}
	public Component getDefaultComponent(Container focusCycleRoot) {
		return null;
	}

	public Component getInitialComponent(Window window) {
		return null;
	}

}
