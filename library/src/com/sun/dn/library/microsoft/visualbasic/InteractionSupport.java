/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/

package com.sun.dn.library.Microsoft.VisualBasic;

import com.sun.dn.*;
import java.util.*;
import java.io.*;
import javax.swing.*;


public class InteractionSupport {
	
	public static String getCommand() {
		return "Danny you have not implemented this properly yet";
	}

	public static void showMsgBox(String s) {
		JOptionPane.showMessageDialog(null, s, null, JOptionPane.INFORMATION_MESSAGE, null);
	}

	public static void showMsgBox(String s, String t) {
		JOptionPane.showMessageDialog(null, s, t, JOptionPane.INFORMATION_MESSAGE, null);
	}

	

}