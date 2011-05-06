/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.IO;

import java.io.*;
import java.util.*;

public class DirectorySupport {

	public static String[] getFiles(String path) {
		//System.out.println("Get Files for ." + path + ".");
		File master = new File(path);
		//System.out.println("Here");
		File[] listFiles = master.listFiles();
		
		if (listFiles == null) {
			String[] s = {};
			return s;
		}
		//System.out.println(" there are " + listFiles.length);
		List l = new ArrayList();
		for (int i = 0; i < listFiles .length; i++) {
			File nextFile = listFiles [i];
			if (!nextFile.isDirectory()) {
				String nextAsString = nextFile.toString();
				l.add(nextAsString);
			}		
		}
		//System.out.println(" files of " + path + " are " + l);
		String[] files = new String[l.size()];
		for (int i = 0; i < files .length; i++) {
			files [i] = (String) l.get(i);
		}
		return files ;

	}

	public static String[] getDirectories(String path) {
		//System.out.println("Get Directories for ." + path + ".");
		File master = new File(path);
		if (!master.exists()) {
			//System.out.println(" which doesn't exist so return logical drives");
			return getLogicalDrives();
		}
		File[] listFiles = master.listFiles();
		//System.out.println(" there are " + listFiles.length);
		if (listFiles == null) {
			String[] s = {};
			return s;
		}
		List l = new ArrayList();
		for (int i = 0; i < listFiles .length; i++) {
			File nextFile = listFiles [i];
			if (nextFile.isDirectory()) {
				String nextAsString = nextFile.toString();
				if (nextAsString.endsWith(File.separator)) {
					l.add(nextAsString);
				} else {
					l.add(nextAsString + File.separator);
				}
			}		
		}
		//System.out.println(" directories of " + path + " are " + l);
		String[] dirs = new String[l.size()];
		for (int i = 0; i < dirs.length; i++) {
			dirs[i] = (String) l.get(i);
		}
		return dirs;
	}

	public static String[] getLogicalDrives() {
		File[] drives = File.listRoots();
		String[] driveStrings = new String[drives.length];
		for (int i = 0; i < driveStrings.length; i++) {
			driveStrings[i] = drives[i].toString();
		}
		return driveStrings;
	}

	
}
