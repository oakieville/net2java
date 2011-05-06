/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.tree.*;

public class TreeNodeSupport extends DefaultMutableTreeNode {
	Object tag;

	public static TreeNodeSupport add(TreeNodeCollectionSupport tc, String s) {
		TreeNodeSupport childNode = new TreeNodeSupport(s); 
		tc.parentNode.add(childNode);
		//System.out.println("Add " + s);
		return childNode;
	}

	public TreeNodeSupport(Object userObject) {
		super(userObject);
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public TreeNodeCollectionSupport getNodes() {
		return new TreeNodeCollectionSupport(this);
	}

	public Object getTag() {
		return this.tag;
	}

	public void setFullPath(String fp) {
	
		
	}

	public String getFullPath() {
		String fullPath = "";
		TreeNode[] nodes = this.getPath();
		for (int i = 1; i < nodes.length; i++) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) nodes[i];
			fullPath = fullPath + treeNode.getUserObject().toString();
		}
		//System.out.println("Full Path of " + tn + " is " + fullPath);
		return fullPath;
	}

}


