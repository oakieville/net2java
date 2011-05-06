/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.IO;

import java.io.*;

public class PathSupport {

	public static String getFileName(String filepath) {
		//System.out.println("get Fiile name for " + filepath);
		File file = new File(filepath);
		String fileName = file.getName();
		if (fileName.equals("")) {
			//System.out.println("Result is " + filepath);
			return filepath;
		} else {
			String result = fileName;
			if (file.isDirectory()) {
				result = fileName + File.separator;
			} 		
			//System.out.println("Result is " + result);
			return result;
		}
	}

	
}
