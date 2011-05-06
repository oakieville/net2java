
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
import com.sun.dn.*;
import java.util.zip.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;

	/** Main class for running ASP applications.
	* @author danny.cowar@sun.com
	*/

public class WebMain {

	public static void main(String[] args) {
		try {
			if (args.length != 8) {
				printUseage();
				System.exit(-1);
			}
			String language = args[0];
			String policyType = args[1];
			String debugClasses = args[2];
			String libraryTranslationFilePath = args[3];
			String libraryClasspath = args[4];
			String j2eeJsfClasspath = args[5];
			String webProjectDir = args[6];
			String projectFileList = args[7];

			if (language == null 
				|| (!language.equals(Interpreter.VB_LANGUAGE) 
					&& !language.equals(Interpreter.CS_LANGUAGE))) {
				System.out.println("You provided " + language + " as a language signifier.");
				System.out.println("Please provide a valid language signifier: vb or cs ");
				System.exit(-1);
			}

			Debug.init(debugClasses);
			WebMain wm = new WebMain();
			List asps = wm.getFilesByFilter(projectFileList, ".aspx");
			System.out.println(asps);
			List aspObjects = new ArrayList();
			for (Iterator itr = asps.iterator();itr.hasNext();) {
				String aspFile = (String) itr.next();
				String aspFilename = ( new File(aspFile) ).getName();
				String translatedFilename = Util.replaceString(aspFilename, "aspx", "jsp");
				ASP asp = new ASP(Util.getString(aspFile), aspFilename, libraryTranslationFilePath );
				aspObjects.add(asp);
			}
			Interpreter interpreter = new Interpreter(System.out, language);

			List javaFiles = wm.getFilesByFilter(projectFileList, ".vb");
			JavaProgram jp = interpreter.createJavaProgram(javaFiles, libraryTranslationFilePath , "MainClassnameForWeb", "web", policyType);
			System.out.println("Java program created");

			
			WebProgram wp = interpreter.createWebProgram(aspObjects , jp);

			String classpath = libraryClasspath + File.pathSeparator + j2eeJsfClasspath;
			//System.out.println("Classpath = " + classpath);

			int exitValue = interpreter.writeWebProgram(wp, 
									webProjectDir,
									classpath);
			System.out.println("Exit with " + exitValue);

			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void printUseage() {
		System.out.println("Java tool for running web applications from VB source.");
		System.out.println("");
		System.out.println("args: <translation-policy> <debug-classes> <library-translations> <library-classes> <j2ee-jsf-classpath> <web-output> <vb-code>");
		System.out.println("");
		System.out.println("translation-policy		strict | gentle");
		System.out.println("debug-classes		Class1,Class2,...,Classn (shortnames)");
		System.out.println("library-translations	root directory for lib.xml files");
		System.out.println("library-classes		root directory for lib support classes");
		System.out.println("j2ee-jsf-classpath		classpath of J2EE & JSF api defintions");
		System.out.println("web-output			output directory for Java and JSP code");
		System.out.println("vb-code			dir/FrontPage.aspx;Main.vb;dir/subdir/Table.vb");
		System.out.println("");
		System.out.println(" danny.coward@sun.com ");
	}



	public List getFilesByFilter(String fileList, String filter) {
		List l = new ArrayList();
		StringTokenizer st = new StringTokenizer(fileList, ";");
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (filter == null || "".equals(filter)) {
				l.add(s);
			} else {
				if (s.endsWith(filter)) {
					l.add(s);
				}
			}
		}
		return l;
	}

}
 