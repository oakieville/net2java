/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class NumericUpDownSupport {
	
	public static void init(JSpinner spinner) {
		((SpinnerNumberModel) spinner.getModel()).setMinimum(new Double(0));
		spinner.setValue(new Double(0));
		((SpinnerNumberModel) spinner.getModel()).setMaximum(new Double(1000));

	}

}
