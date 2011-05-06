
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
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

    /** @author danny.coward@sun.com */

public class DNTestSuite extends TestSuite {
    private String writeDirectory;
    private String libraryPath;
    private String libraryClasspath;
    private List testDescriptions = new ArrayList();
    private static String TESTS = "tests";
    private static String LANGUAGE = "language";
    private static String MAIN_CLASSNAME = "main-class";
    private static String TEST = "test";
    private static String FILES = "files";
    private static String NAME = "name";
    private static String PROGRAM_TYPE = "program-type";
    
    List testsThatPassed = new ArrayList();
    List testsThatFailed = new ArrayList();
    
    
   
    public DNTestSuite(String writeDirectory, String libraryPath, String libraryClasspath ) {
        this.writeDirectory = writeDirectory;
        this.libraryPath = libraryPath;
        this.libraryClasspath = libraryClasspath;
        Debug.init("");
    }
    
    private void init(String testListFilename) {
        File f = new File(testListFilename); 
	
	try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
            Document d = db.parse(f); 
            this.parseFromNode((Element) d.getFirstChild());
            
        } catch (Throwable t) {
            System.out.println("Error in file " + f);
            t.printStackTrace();
            throw new Stop(this.getClass());
	}
    }
    
    private void parseFromNode(Element root) {
        List elements = Util.getElts(root);
        for (Iterator itr = elements.iterator(); itr.hasNext();) {
            Element e = (Element) itr.next();
            List testElements = Util.getElts(e);
            String name = "";
            String mainClassname = "";
            List filenames = new ArrayList();
            String language = "";
            String programType = "";
            for (Iterator itrr = testElements.iterator(); itrr.hasNext();) {
                Element nextTestElement = (Element) itrr.next();
                
                if (nextTestElement.getTagName().equals(NAME)) { 
                    name = Util.getText(nextTestElement); 
                    //System.out.println("Name is: " + name);
                } else if (nextTestElement.getTagName().equals(MAIN_CLASSNAME)) {
                    mainClassname = Util.getText(nextTestElement); 
                    //System.out.println("mainClassname is: " + mainClassname);
                } else if (nextTestElement.getTagName().equals(LANGUAGE)) {
                    language = Util.getText(nextTestElement);
                    //System.out.println("language is: " + language);
                } else if (nextTestElement.getTagName().equals(FILES)) { 
                    List fs = Util.getElts(nextTestElement);
                    for (Iterator ittr = fs.iterator(); ittr.hasNext();) {
                        Element fe = (Element) ittr.next();
                        filenames.add(Util.getText(fe));
                    }
                    //System.out.println("filenames is: " + filenames);
                } else if (nextTestElement.getTagName().equals(PROGRAM_TYPE)) {
                    programType = Util.getText(nextTestElement);
                }
            }
            TestDescription td = new TestDescription(name, filenames, language, mainClassname);
            td.programType = programType;
            this.testDescriptions.add(td);
        }
        
    }
    
    public void runTests(String testListFilename) {
        // iterate
        this.init(testListFilename);
        int i = 1;
        for (Iterator itr = this.testDescriptions.iterator(); itr.hasNext();) {
            TestDescription td = (TestDescription) itr.next();
            File testWriteDirectory = new File(this.writeDirectory, td.language);
            testWriteDirectory = new File(testWriteDirectory, td.name);
            DNTest dnt = new DNTest(td.name, td.language, td.filenames, td.mainClassname, td.programType, testWriteDirectory.getAbsolutePath(), this.libraryPath, this.libraryClasspath);
		System.out.println("");
            System.out.println("____ (" + i + "/" + testDescriptions.size() + ") Start: " + dnt.getName() + " test.");
            dnt.runTest();
            if (dnt.didPass()) {
                this.testsThatPassed.add(dnt);
            } else {
                this.testsThatFailed.add(dnt);
            }
            System.gc();
            i++;
            System.out.println("____ End: " + dnt.getName() + " passed: " + dnt.didPass());
        }
        
        System.out.println("____ Test Summary: " + this.testsThatPassed.size() + " passed and " + this.testsThatFailed.size() + " failed.");
        if (this.testsThatFailed.size() > 0) {
            System.out.println("____ Failed tests: " + this.testsThatFailed);
        }
        
    } 
    
} 

class TestDescription {
    String name;
    List filenames;
    String language;
    String mainClassname;
    String programType;
    
    public TestDescription(String name, List filenames, String language, String mainClassname) {
        this.name = name;
        this.filenames = filenames;
        this.language = language;
        this.mainClassname = mainClassname;
    }
}
 
 