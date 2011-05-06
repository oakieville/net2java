
 /* 
 * Copyright (c) 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *  
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *  
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *  
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.dn.container.web;

import java.io.*;
import java.util.*;
import com.sun.dn.util.*;
import com.sun.dn.*;
import java.util.zip.*;
import com.sun.dn.java.*;

	/** Class for creating J2EE WAR files.
	@author danny.coward@sun.com
	*/

public class WebAppPackager {



	public void packageApp(String dir, String warName) throws Exception {
		List webFiles = Util.getAllEntryFilesIn(new File(dir), "");
		FileOutputStream fos = new FileOutputStream(warName);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		for (Iterator itr = webFiles.iterator(); itr.hasNext();) {
			File nextFilename = (File) itr.next();
			String jarEntryName = nextFilename.toString();
			System.out.println(jarEntryName);
			jarEntryName = jarEntryName.substring(dir.length()+1, jarEntryName.length());
			jarEntryName = Util.replaceString(jarEntryName, "\\", "" + '/');
			boolean isDirectory = nextFilename .isDirectory();
			ZipEntry ze = null;
			if (isDirectory) {
				ze = new ZipEntry(jarEntryName  + "/");
			} else {
				ze = new ZipEntry(jarEntryName );
			}
			
			zos.putNextEntry(ze);
			if (!isDirectory) {
				String data = Util.getString(nextFilename.toString());
				zos.write(data.getBytes(), 0, data.getBytes().length);
			}
			zos.closeEntry();
		}
		
		zos.close();
		fos.close();

		System.out.println("Done packaging " + warName);
	}

	

	


}
 