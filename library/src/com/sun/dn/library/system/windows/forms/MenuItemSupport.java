/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import javax.swing.*;

public class MenuItemSupport extends JMenu {
// not used
	public void addAll(JMenuItem[] o) {
		
		for (int i = 0; i < o.length; i++) {
			if (o[i] instanceof JMenu) {
				//JMenuItem jmi = new JMenuItem();
				//jmi.setText(o[i].getText());
				//this.add(jmi);
				this.add(o[i]);
			} else {
				this.add(o[i]);
			}
		}

	}

	public int getShortcut() {
		return -1;
	}

	public void setShortcut(int i) {

	}

	public void setText(String text) {
		int i = text.indexOf('&');
		String toUse = text;
		if (i != -1) {
			//System.out.println(i);
			char c = text.charAt(i+1);
			//System.out.println(c);
			this.setMnemonic(c);
			toUse = text.substring(0,i) + text.substring(i+1, text.length());
		}
		super.setText(toUse);
	}

}
