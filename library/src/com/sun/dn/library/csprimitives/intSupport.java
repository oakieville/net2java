/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.CSPrimitives;

public class intSupport {

	public static int parseInt(String s) {
		if (s.indexOf(".") != -1) {
			return (int) Double.parseDouble(s);
		} else {
			return Integer.parseInt(s);
		}
	}

}
