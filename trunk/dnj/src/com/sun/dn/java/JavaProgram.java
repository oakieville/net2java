
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
package com.sun.dn.java;

import java.util.*;
import java.io.*;
import java.util.logging.*;
import com.sun.dn.*;
import com.sun.dn.parser.TranslationReport;
import com.sun.dn.util.*;
import com.sun.dn.Library;
	
	/** Object containing all the structure and code
	** of a Java application.
	** @author danny.coward@sun.com
	*/

public class JavaProgram {
        public static String UNTRANSLATED_KEYWORD = "UntranslatedKeyword";
    
	public static String CMDLINE_TYPE = "cmdline";
	public static String GUI_TYPE = "gui";
	public static String WEB_TYPE = "web";
	List classes = new ArrayList();
	List files = new ArrayList();
	JavaClass mainClass = null;
	String type;
	Library library;
	private boolean isTypeResolved = false;
	private TranslationReport report;

	public JavaProgram(String type, Library library) {
		this.library = library;
		if (type.equals(GUI_TYPE)) {
			this.type = GUI_TYPE;
		} else if (type.equals(WEB_TYPE)) {
			this.type = WEB_TYPE;
		} else if (type.equals(CMDLINE_TYPE)) {
			this.type = CMDLINE_TYPE;
		} else {
			throw new RuntimeException("Unknown type " + type);
		}
	}

	public boolean isTypeResolved() {
		return this.isTypeResolved;
	}
	
	public void setTypeResolved(boolean isTypeResolved) {
		this.isTypeResolved = isTypeResolved;
	}

	public void setTranslationReport(TranslationReport report) {
		this.report = report;
	}
	
	public TranslationReport getTranslationReport() {
		return this.report;
	}

	public String getType() {
		return this.type;
	}

	public List getClasses() {
		return this.classes;
	}
	
	public Library getLibrary() {
		return this.library;
	}

	public List getEventHookupClasses() {
		List l = new ArrayList();
		for (Iterator itr = this.getClasses().iterator(); itr.hasNext();) {
			JavaClass jc = (JavaClass) itr.next();
			if (jc instanceof EventSupport) {
				l.add(jc);
			}
		}
		return l;
	}
	
	public void addClass(JavaClass jClass) {
		//System.out.println("Add class " + jClass.getName());
		classes.add(jClass);
	}

	public void addAllClasses(List javaClasses) {
		for (Iterator itr = javaClasses.iterator(); itr.hasNext();) {
			JavaClass c = (JavaClass) itr.next();
			this.addClass(c);
		}
	}

	public void setMainClass(JavaClass jClass) {
            if (jClass != null) {
		this.classes.add(jClass);
            }
		this.mainClass = jClass;
	}

	public JavaClass getMainClass() {
		return this.mainClass;
	}

	public void write(String directory) throws Exception {
		//Logger.global.log(Level.INFO, "Write to -" + directory);
		List fileNames = new ArrayList();
		(new File(directory)).mkdirs();
                //System.out.println("Here");
		for (Iterator itr = classes.iterator(); itr.hasNext();) {
			JavaClass jc = (JavaClass) itr.next();
			//System.out.println("jc = " + jc);
			File f = new File(directory);
			if (!jc.getPackageName().equals("")) {
				f = new File( f, Util.convertPackageToDir(jc.getPackageName()) );				
			}
			f.mkdirs();
			f = new File(f, Util.createLegalFilename(jc.getName()) + ".java");		
			files.add(f);
			FileOutputStream fos = new FileOutputStream(f);
			String code = jc.asCode();
			//System.out.println("");
			//System.out.println("__" + f + "__");
			//System.out.println(code);
			//System.out.println("___________end___");
			//System.out.println("");
			fileNames.add(f.getName());
			fos.write(code.getBytes());
			fos.close();
		}
		System.out.println("Written classes: " + fileNames);
	}

	public List getFiles() {
		return this.files;
	}

	public String getFilesAsString() {
		String s = "";
		for (Iterator itr = files.iterator(); itr.hasNext();) {
			File nextFile = (File) itr.next();
			if (itr.hasNext()) {
				s = s + nextFile.toString() + " ";
			} else {
				s = s + nextFile.toString();
			}
		}
		return s;
	}


}
 