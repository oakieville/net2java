/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.event.*;
import com.sun.dn.library.System.*;

public class TreeViewEventArgsSupport extends EventArgsSupport {
	TreeNodeSupport treeNode;
	
	public TreeViewEventArgsSupport(java.util.EventObject eo) {
		super(eo);
		if (eo instanceof TreeSelectionEvent) {
			Object o = ((TreeSelectionEvent) eo).getPath().getLastPathComponent();
			this.treeNode = (TreeNodeSupport) o;
		} else {
			System.out.println("tree node not assigned in " + this);
		}
	}

	public TreeNodeSupport getNode() {
		return (TreeNodeSupport) treeNode;

	}

	public void setTreeNode(TreeNodeSupport tns) {}


}
