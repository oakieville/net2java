/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
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
            StringTokenizer str = new StringTokenizer(statement, "\n");
           
            while (str.hasMoreTokens()) {
                String nextStatement = str.nextToken().trim();
                boolean reachedEnd = as.parseLine(nextStatement.trim(), "[" + CS_ASSEMBLY + ":");
                if (reachedEnd) {
                    return as;
                }
            }
            return as;
        }


	private boolean parseLine(String statement, String assemblyString) {
		if (statement.startsWith(assemblyString)) {
			String toParse = statement.substring(assemblyString.length(), statement.length() - 1);
			String propertyName = toParse.substring(0, toParse.indexOf("(")).trim();
			String propertyValue = 	toParse.substring(toParse.indexOf("(") + 1, toParse.length()-1).trim();
			propertyMap.put(propertyName, propertyValue);		
			return false;
		} else {
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
