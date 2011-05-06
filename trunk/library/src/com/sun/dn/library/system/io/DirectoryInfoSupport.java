/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.IO;

import java.io.*;

public class DirectoryInfoSupport extends FileSystemInfoSupport {

	public DirectoryInfoSupport(String s) {
		super(s);
	}

	public DirectoryInfoSupport getParentDirectoryInfo() {
		try {
			File parentFile = this.getParentFile();
			if (parentFile == null) {
				parentFile = new File("");
			}
			return new DirectoryInfoSupport(parentFile.toString());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public DirectoryInfoSupport getRoot() {
		//System.out.println("Get root for " + this);
		File rootParent = this;
		boolean done = false;
		while (!done) {
			if (rootParent.getParent() != null) {
				rootParent = rootParent.getParentFile();
			} else {
				done = true;
			}
		}
		if (rootParent == this) {
			//System.out.println("Root is same as this " + this);

			return this;
		} else {
			//System.out.println("Root is " + rootParent);

			return new DirectoryInfoSupport(rootParent.toString());
		}
		


		
	}
	
}
