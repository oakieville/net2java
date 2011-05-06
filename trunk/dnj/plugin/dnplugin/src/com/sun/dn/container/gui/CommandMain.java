
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
package com.sun.dn.container.gui;

import com.sun.dn.parser.*;
import com.sun.dn.*;
import java.io.*;
import java.util.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;

	/** Main class for command line .NET applications.
	*@author danny.coward@sun.com
	**/

public class CommandMain {
	// takes the path to a visual basic project and runs it

	public static void main (String[] args) {
		if (args.length != 9) {
			printUseage();
			return;
		}
		String language = args[0];
		String projectType = args[1];
		String policyType = args[2];
		String debugClasses = args[3];
		String libraryPath = args[4];
		String libraryClasspath = args[5];
		String writeDirectory = args[6];
		String mainClassname = args[7];
		String codeFilepaths = args[8];
		

		if (mainClassname == null || "".equals(mainClassname)) {
			System.out.println("Please provide a main class name ");
			System.exit(-1);
		}

		if (language == null 
			|| (!language.equals(Interpreter.VB_LANGUAGE) 
				&& !language.equals(Interpreter.CS_LANGUAGE))) {
			System.out.println("You provided " + language + " as a language signifier.");
			System.out.println("Please provide a valid language signifier: vb or cs ");
			System.exit(-1);
		}

		System.out.println("Run " + codeFilepaths  + " as Java.");
		Debug.init(debugClasses);		
		runProgram(codeFilepaths, 
				writeDirectory, 
				libraryPath, 
				libraryClasspath, 
				mainClassname, 
				projectType, 
				policyType,
				language);
	}
	
	public static void printUseage() {
		System.out.println("Java tool for running command line and gui apps from .NET source.");
		System.out.println("");
		System.out.println("args: <language> <project-type> <translation-policy> <debug-classes> <library-translations> <library-classes> <java-output> <main-classname> <code>");
		System.out.println("");
		System.out.println("language			vb | cs");
		System.out.println("project-type		gui | cmdline");
		System.out.println("translation-policy		strict | gentle");
		System.out.println("debug-classes		Class1,Class2,...,Classn (shortnames)");
		System.out.println("library-translations	root directory for lib.xml files");
		System.out.println("library-classes		root directory for lib support classes");
		System.out.println("java-output			output directory for Java code");
		System.out.println("main-classname		name of VB main class or module");
		System.out.println("code			dir/Module1.vb;Main.vb;dir/subdir/Table.vb");
		System.out.println("");
		System.out.println(" danny.coward@sun.com ");
	}

	public static void runProgram(String codeFilepaths, String writeDirectory, 
				String libraryPath, String libraryClasspath, 
				String mainClassname, String projectType, 
				String policyType, String language) {
		JavaProgram jp = null;
		try {
			//Debug.setOn(false);
			long l = Debug.getTime();
			List sourceFiles = Util.getFilepaths(codeFilepaths, language);

			Interpreter interpreter = new Interpreter(System.out, language);
	
			// parse code and translate it to Java
			jp = interpreter.createJavaProgram(sourceFiles, libraryPath, mainClassname, projectType, policyType);
			
			// write Java code and compile it
			int exitValue = interpreter.writeAndCompileJavaProgram(jp, writeDirectory, libraryClasspath);

			if (exitValue != 0) {
				System.out.println("Exiting.");
				return;
			}
                        
                        if (jp.getMainClass() == null) {
                            System.out.println("No main class to run.");
                            System.exit(-1);
                        }
			// run it
			String runCommand = "java -classpath " + writeDirectory + File.pathSeparatorChar + libraryClasspath + " " + jp.getMainClass().getName();
			System.out.println(runCommand);
			Process p = Runtime.getRuntime().exec(runCommand);
			InputStream is = p.getInputStream();

			Util.read(is, "dnj"); // blocks here

			exitValue = p.exitValue();
                        System.out.println("(Exit value was " + exitValue + ")");
			if (exitValue != 0) {
				System.out.println("Execute failed.");
			} else {
				//System.out.println("Done.");
			}


			
		} catch (Stop stop) {
			System.out.println("____ [Stop] __ [" + stop.getStopper().getName() + "] ____");
		} catch (Throwable t) {
			//Debug.dumpEntries();
			t.printStackTrace();

		}		
	}


}
 