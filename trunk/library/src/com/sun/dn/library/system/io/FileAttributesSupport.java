/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.IO;

import java.io.*;

public class FileAttributesSupport {
	File f;

	public FileAttributesSupport(File f) {
		this.f = f;
	}

	public String toString() {
		String s = "";
		if (f.isDirectory()) {
			s = "Hidden";
		} else {
			s = "";
		}
		String t = null;
		if (f.isDirectory()) {
			t = "Directory";
		} else {
			t = "Archive";
		}
		if (s.length() > 0) {
			return s + ", " + t;
		} else {
			return t;
		}

		


	}


	
}
