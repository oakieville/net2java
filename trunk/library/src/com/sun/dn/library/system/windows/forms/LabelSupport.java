/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class LabelSupport extends JTextField {
	
	public LabelSupport () {
		this.setEditable(false);
                this.setOpaque(true);
		JLabel j = new JLabel("s");
		this.setFont( j.getFont() );
	}	

	public void setText(String s) {
		//System.out.println("Label Support set text to " + s);
		super.setText(s);

		int x = this.getSize().width;
		int px = this.getPreferredSize().width;
		Dimension newS = new Dimension(px, this.getSize().height);
		this.setSize(newS); // sorry...but vb labels resize themselves
	}

}
