/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;


public class ButtonSupport extends JButton implements Anchorable {
	private boolean isAnchored = false;

	public ButtonSupport() {
		this.setText("Default1 ");
		this.setSize(this.getPreferredSize());
	}

	public void setAnchorHack() {
		// this is because I can't get into VB layout and anchor things right now
		this.isAnchored = true;
	}

	public boolean isAnchored() {
		return this.isAnchored;
	}

	public void setDialogResult(int i) {
		System.out.println("Button Support noop");
	}

	public void setText(String s) {
		int i = s.indexOf("&");
		if (i != -1) {
			String startS = s.substring(0,i);
			String endS = s.substring(i+1, s.length());
			char mn = s.charAt(i+1);
			this.setMnemonic(mn);
			super.setText(startS + endS);
		} else {
			super.setText(s);
		}
	}

}
