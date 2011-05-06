/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.sun.dn.library.System.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class TreeViewCancelEventArgsSupport extends EventArgsSupport {
	TreeNodeSupport treeNode;
	
	public TreeViewCancelEventArgsSupport(java.util.EventObject eo) {
		super(eo);
		if (eo instanceof TreeExpansionEvent) {
			Object o = ((TreeExpansionEvent) eo).getPath().getLastPathComponent();
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
