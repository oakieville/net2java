
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

package com.sun.dn;

import com.sun.dn.parser.*;
import com.sun.dn.java.*;
import com.sun.dn.container.web.*;
import com.sun.dn.util.*;
import java.io.*;
import java.util.*;
import com.sun.dn.library.LibraryData;

	/** The Interpreter is the entry point for creating java programs
	** from .NET source code.
	** @author danny.coward@sun.com
	**/

public class Interpreter {
	private PrintStream output;
	private String language;
	public static String VB_LANGUAGE = "vb";
	public static String CS_LANGUAGE = "cs";
	private static boolean isGUI = false;

	public Interpreter(PrintStream output, String language) {
		this.output = output;
		this.language = language;
	}

	public static boolean isGUI() {
		return isGUI;
	}

	public static void setGUI(boolean b) {
		isGUI = b;
	}

	public JavaProgram createJavaProgram(List sourceFiles, 
                                            String libraryPath, 
                                            String mainClassname, 
                                            String projectType,
                                            String policyType) throws Exception {
		//Debug.setOn(true);
		long l = Debug.getTime();
		LibraryData libraryData = new LibraryData(new File(libraryPath), projectType, language, false);
                Library library = new Library(libraryData);
		Parser parser = Parser.createParser(this.language, sourceFiles, library, new TranslationPolicy(policyType));

		long lll = Debug.getTime();
		System.out.println("Library Loaded in " + (lll - l) + "ms");

		ParseTree tree = parser.parse();
		TranslationReport report = tree.getTranslationReport();

		long ll = Debug.getTime();
		System.out.println("Code parsed in " + (ll-lll) + "ms");
		Debug.setOn(true);

		Translator tt = new Translator(this.language);
		JavaProgram jp = tt.createJavaProgram(tree, mainClassname, projectType);

		if (report.hasTypeResolveErrors() || report.hasTranslationWarnings()) {
			List errors = new ArrayList();
			errors.addAll(report.getTypeResolveExceptions());
			report.doReport(errors, report.getTranslationWarnings());
		} 

		l = Debug.getTime();
		System.out.println("Translated in " + (l - ll) + "ms");

		jp.setTypeResolved(!report.hasTypeResolveErrors()); // resolved if no errors
		
		return jp;
	}

	public WebProgram createWebProgram(List asps, JavaProgram jp) throws Exception {
		WebProgram wp = new WebProgram(asps, jp);

		for (Iterator itr = asps.iterator(); itr.hasNext();) {
			ASP asp = (ASP) itr.next();
			asp.translate();	
		}

		// do the WebForm initialisation code
		List javaWebForms = wp.getWebForms();
		Library library = jp.getLibrary();
		for (Iterator itr = javaWebForms.iterator(); itr.hasNext();) {
			JavaClass jc = (JavaClass) itr.next();
			WebTranslator.addJavaComponentInitialisation(jc, library);
			doExposePageLoadMethod(jc);
		}


		// do event hookups
		List hookups = jp.getEventHookupClasses();
		for (Iterator itr = hookups.iterator(); itr.hasNext();) {
			EventSupport es = (EventSupport) itr.next();
			//System.out.println(es.getVBSender());
			for (Iterator itrr = asps.iterator(); itrr.hasNext();) {
				ASP asp = (ASP) itrr.next();
				DNVariable v = asp.getComponent(es.getVBSender().getName());
				if (v != null) {
					asp.addActionListenerFor(v, es.getName());
				}	
			}
		}
		
		return wp;

	}

	private void doExposePageLoadMethod(JavaClass jc) {
		boolean hitOne = false;
		for (Iterator itr = jc.findMethodsByName("Page_Load").iterator(); itr.hasNext();) {
			JavaMethod jm = (JavaMethod) itr.next();
			jm.addModifier(JavaKeywords.J_PUBLIC);
			hitOne = true;
		}
		if (!hitOne) {
			throw new RuntimeException(jc + " is a form class, but has no Page_Load method...");
		}

	}

	public int writeWebProgram(WebProgram wp, String webRoot, String classpath) throws Exception {
		String javaClassesDir = webRoot + File.separator + "WEB-INF" + File.separator + "classes";
		JavaProgram jp = wp.getJavaProgram();
		int i = this.writeAndCompileJavaProgram(jp, javaClassesDir, classpath);

		for (Iterator itr = wp.getASPs().iterator(); itr.hasNext();) {
			ASP asp = (ASP) itr.next();
			String aspFilename = Util.replaceString(asp.getName(), "aspx", "jsp");
			FileOutputStream fos = new FileOutputStream(new File(webRoot, aspFilename));
			asp.write(fos);
			//asp.write(System.out);
		}
		return i;
	}

	public int writeJavaProgram(JavaProgram jp, String writeDirectory) throws Exception {
		jp.write(writeDirectory);
		System.out.println("Java written to " + writeDirectory);
		Debug.flush();	
		return 0;
	}

	public int compileJavaProgram(JavaProgram jp, String writeDirectory, String classpath) throws Exception {
		if (!jp.isTypeResolved()) {
			System.out.println("Compilation will not take place since there were type resolution errors.");
			return -1;
		}

		String fileList = jp.getFilesAsString();
		String command = "javac -d " + writeDirectory + " -classpath " + classpath + " " + fileList;
		//System.out.println(command);
		System.out.print("Compiling...");

		Process p = Runtime.getRuntime().exec(command);
		// need to squirt the output into the 
		InputStream is = p.getErrorStream();

		Util.read(is, "compiler"); // blocks here
		int exitValue = p.exitValue();
		return exitValue;
	}

	public int writeAndCompileJavaProgram(JavaProgram jp, 
									String writeDirectory,
									String classpath) throws Exception {
		this.writeJavaProgram(jp, writeDirectory);
	
		return this.compileJavaProgram(jp, writeDirectory, classpath);
	}



	

	


}
 