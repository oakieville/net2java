/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System;

public class DecimalSupport {
	
	public static double createDoubleFromIntArray(int[] array) {
		if (array.length < 4) {
			throw new RuntimeException("This isn't supposed to happen");
		}
		String s = "" + array[0] + "." + array[1] + array[2] + array[3];
                
		double d = Double.valueOf(s).doubleValue();
                System.out.println("double=" + d);
                return d;
	}	
}
