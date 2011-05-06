
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
package com.sun.dn.library;

import com.sun.dn.util.Debug;
import com.sun.dn.util.Util;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import com.sun.dn.*;
import java.util.*;


	/** Data structure for a list of namespaces. Used by the
	** VB parser to build the lists of namespaces used by
	** different kinds of VB applications, like web or gui
	** applications.
	*@author danny.coward@sun.com
	*/

 class NamespaceListParser {
	public static String NAMESPACES = "namespaces";
	public static String NAMESPACE = "namespace";
	private File myFile;
	private List namespaces = new ArrayList();
	
	public NamespaceListParser () {

	}
        
       
	public void addFromFile(File f) {
		myFile = f;
                
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document d = db.parse(f);
			this.parseFromNode(EntryParser.getFirstElement(d));
                        //System.out.println("Was ok...");
			Debug.logn("OK - " + f, this);
		} catch (Throwable t) {
			System.out.println("There was an error parsing the namespaces for this conversion. The file was " + f);
			System.out.println("The error was: " + t.getMessage());
			t.printStackTrace();
			throw new RuntimeException("Thhere was an error error parsing the library namespaces for this conversion. The file was " + f);
			//System.exit(0);
		}
	}

	public boolean contains(File f, File directoryRoot) {
		//System.out.println(f);
		//System.out.println(directoryRoot);
		String relativeFilename = f.toString().substring(directoryRoot.toString().length() + 1, f.toString().length());
                if (!namespaces.isEmpty()) {
                    //System.out.println(relativeFilename );
                    //System.out.println("NS" + namespaces);
                }
		if ( relativeFilename.indexOf(File.separator) == -1
			|| this.passes(relativeFilename) ) {
                        
			return true;
		}
		return false;
	}

	private boolean passes(String name) {
                
		String directoryPart = name.substring(0, name.lastIndexOf(File.separator));
		//System.out.println("----" + directoryPart + " " + namespaces);
                for (Iterator itr = namespaces.iterator(); itr.hasNext();) {
                    String next = (String) itr.next();
                 
                    if (directoryPart.startsWith(next)) {
                        return true;
                    }
                    
                }
		return false;
	}

	private void parseFromNode(Element e) {
		List l = Util.getElts(e);
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			Element next = (Element) itr.next();
			String text = Util.getText(next );
			String convertedText = Util.replaceString(text, ".", File.separator);
			namespaces.add(convertedText);
		}
		
	}



}
 