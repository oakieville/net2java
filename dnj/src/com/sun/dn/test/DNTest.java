
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
package com.sun.dn.test;

import junit.framework.*;
import java.util.*;
import com.sun.dn.util.*;
import com.sun.dn.java.JavaProgram;
import com.sun.dn.Interpreter;
import com.sun.dn.parser.TranslationPolicy;
import java.io.File;
import java.io.InputStream;

    /** @author danny.coward@sun.com */

public class DNTest extends TestCase {
    private String language;
    private List files;
    private String mainClassname;
    private String writeDirectory;
    private String libraryPath;
    private String libraryClasspath;
    private String programType = "";
    private boolean didPass = false;
    
    public DNTest(String name, String language, List files, String mainClassname, String programType, String writeDirectory, String libraryPath, String libraryClasspath) {
        super.setName(name);
        //System.out.println("New Test with language " + language);
        this.language = language;
        this.files = files;
        this.mainClassname = mainClassname;
        this.writeDirectory = writeDirectory;
        this.libraryPath = libraryPath;
        this.libraryClasspath = libraryClasspath;
        this.programType = programType;
        if (this.programType.equals("")) {
            this.programType = JavaProgram.CMDLINE_TYPE;
        }
    }
    
    public boolean didPass() {
        return this.didPass;
    }
    
    private void makePass() {
        didPass = true;
    }
    
    private void makeFail() {
        //this.assertTrue(false);
        didPass = false;
    }
    
    public void runTest() { 
        JavaProgram jp = null;
            try {
		Interpreter interpreter = new Interpreter(System.out, this.language);
		// parse code and translate it to Java
		jp = interpreter.createJavaProgram(this.files, libraryPath, this.mainClassname, this.programType, TranslationPolicy.GENTLE); 
		// write Java code and compile it
		int exitValue = interpreter.writeAndCompileJavaProgram(jp, writeDirectory, libraryClasspath);
		if (exitValue != 0) {
                    System.out.println("Exiting.");
                    makeFail();
                    return;
		}
                if (jp.getMainClass() == null) {
                    System.out.println("No main class to run.");
                    makeFail();
                    return;
                }
		// run it
		String runCommand = "java -classpath " + writeDirectory + File.pathSeparatorChar + libraryClasspath + " " + jp.getMainClass().getName();
		System.out.println(runCommand);
		Process p = Runtime.getRuntime().exec(runCommand);
		InputStream is = p.getInputStream();
		Util.read(is, "dnj"); // blocks here
		exitValue = p.exitValue();
		if (exitValue != 0) {
                    System.out.println("Execute failed.");
                    makeFail();
                    return;
                }

            } catch (Throwable t) {
		//Debug.dumpEntries();
		t.printStackTrace();
                makeFail();
                return;

            }	
            makePass();
	}
    
    public String toString() {
        return "DNTest(" + super.getName() + ")";
    }
    
}

 