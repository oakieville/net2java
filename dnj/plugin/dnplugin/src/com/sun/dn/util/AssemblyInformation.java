
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
package com.sun.dn.util;

import java.util.*;

	/** This class knows how to parse the assembly information
	** for a VB application.
	** @author danny.coward@sun.com
	*/

public class AssemblyInformation {
	public static String VB_ASSEMBLY = "Assembly";
        public static String CS_ASSEMBLY = "assembly";
	private Map propertyMap = new HashMap();
	private static AssemblyInformation ai = new AssemblyInformation();

	public static boolean isVBAssemblyInformation(String statement) {
		return statement.trim().startsWith("<" + VB_ASSEMBLY);
	}
        
        public static boolean isCSAssemblyInformation(String statement) {
		return statement.trim().startsWith("[" + CS_ASSEMBLY);
	}

	public static AssemblyInformation getInstance() {
		return ai;
	}

	public AssemblyInformation() {
		ai = this;
	}
        
        public static AssemblyInformation createVBAssemblyInformation(String statement, Iterator itr) {
            AssemblyInformation as = new AssemblyInformation();
		String nextStatement = statement.trim();
		boolean reachedEnd = as.parseLine(nextStatement, "<" + VB_ASSEMBLY + ":");
		if (reachedEnd) {
			return as;
		}
		while(itr.hasNext()) {
			nextStatement = (String) itr.next();
			reachedEnd = as.parseLine(nextStatement, "<" + VB_ASSEMBLY + ":");
			if (reachedEnd) {
				return as;
			}
		}
		throw new RuntimeException("Something weird about the AssemblyInfo format");
        
        }
        
         public static AssemblyInformation createCSAssemblyInformation(String statement, Iterator itr) {
            AssemblyInformation as = new AssemblyInformation();
           
            List l = Util.tokenizeIgnoringEnclosers(statement, "\n");
            for (Iterator statementItr = l.iterator(); statementItr.hasNext();) {
                String nextStatement = ((String) statementItr.next()).trim();
                boolean reachedEnd = as.parseLine(nextStatement.trim(), "[" + CS_ASSEMBLY + ":");
                if (reachedEnd) {
                    return as;
                }
            }
            return as;
        }


	private boolean parseLine(String statement, String assemblyString) {
		Debug.logn("Parse " + statement, this);
		
		if (statement.startsWith(assemblyString)) {
			String toParse = statement.substring(assemblyString.length(), statement.length() - 1);
			Debug.logn("AssemblyInformation " + toParse, this);
			String propertyName = toParse.substring(0, toParse.indexOf("(")).trim();
			String propertyValue = 	toParse.substring(toParse.indexOf("(") + 1, toParse.length()-1).trim();
			propertyMap.put(propertyName, propertyValue);		
			return false;
		} else {
			Debug.logn(this.propertyMap, this);
			return true;
		}
	}

	public String getValueFor(String name) {
		//return (String) this.propertyMap.get(name);
		return "I need to be serialized, read back in and avaiable at runtime";
	}

	public void parse(String code) {

	}

}
 