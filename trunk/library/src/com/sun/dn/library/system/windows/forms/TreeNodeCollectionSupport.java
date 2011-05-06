/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;


public class TreeNodeCollectionSupport extends ArrayList {
	DefaultMutableTreeNode parentNode;

	public TreeNodeCollectionSupport (DefaultMutableTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public void clear() {
		//System.out.println("Clear parent node " + parentNode);
		parentNode.removeAllChildren();
		//System.out.println("Cleared: now there are " + parentNode.getChildCount());
	}
	
}
