/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.tree.*;

public class TreeViewSupport extends JTree implements NeedsScrollbars {
	private TreeNodeCollectionSupport nodes;

	public TreeViewSupport() {
		TreeNodeSupport dmtn = new TreeNodeSupport("Root"); 
		DefaultTreeModel dtm = new DefaultTreeModel(dmtn );
		this.setModel(dtm);
		//this.setRootVisible(false);
                
	}

	public void setImageIndex(int i) {
		
	}

	public int getImageIndex() {
		return -1;
	}

	public void setSelectedImageIndex(int i) {
		
	}

	public int getSelectedImageIndex() {
		return -1;
	}

	public TreeNodeCollectionSupport getNodes() {
		if (this.nodes == null) {
			this.nodes = new TreeNodeCollectionSupport((DefaultMutableTreeNode) this.getModel().getRoot());
		}
		return this.nodes;
	}



	public void setItemHeight(int i) {
		
	}

	public int getItemHeight() {
		return -1;
	}

	public void setIndent(int i) {
		
	}

	public int getIndent() {
		return -1;
	}


}
