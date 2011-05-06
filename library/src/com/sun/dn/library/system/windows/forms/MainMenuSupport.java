/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import javax.swing.*;

public class MainMenuSupport extends JMenuBar {

        // not referenced any more
	public void addAll(JMenuItem[] o) {
		for (int i = 0; i < o.length; i++) {
			if (o[i] instanceof JMenu) {
				this.add(o[i]);
			} else {
				if (true) throw new RuntimeException("sd");
				JMenu m = new JMenu(o[i].getText());
				this.add(m);
			}
		}
	}

}
