
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
package com.sun.dn.parser.statement;

import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;
import com.sun.dn.java.*;
import com.sun.dn.*;
import com.sun.dn.library.LibraryData;

    /** @author danny.coward@sun.com */

public class PropertyStatement extends StatementAdapter {
        List accessKeywords = new ArrayList();
        List readWriteKeywords = new ArrayList();
        List overloadsOverridesKeywords = new ArrayList();
        DNVariable propertyAsVariable;
        List pStatements = new ArrayList();
        boolean isDefault = false;
        boolean canBeOverriden = true;
        boolean hidesBase = false;
       


	public static boolean isVBPropertyStatement(String code, InterpretationContext context) {
                Debug.clogn("is property statement: " + code, PropertyStatement.class);
		List tokens = Util.tokenizeIgnoringEnclosers(code, " ");
                for (Iterator itr = tokens.iterator(); itr.hasNext();) {
                    String nextToken = (String) itr.next();
                   
                    if (nextToken.trim().equals(VBKeywords.VB_Property)) {
                        Debug.clogn("yes", PropertyStatement.class);
                        return true;
                    }   
                }
                Debug.clogn("no", PropertyStatement.class);
                return false;
	}
        
        public static boolean isCSPropertyStatement(String code, InterpretationContext context) {
            Debug.clogn("is property statement: " + code, PropertyStatement.class);
            if (code.indexOf ("{") != -1) {
                String inside = code.substring(code.indexOf("{") + 1, code.length()-1);
                
                if (inside.trim().startsWith(CSKeywords.CS_Get) ||  
                        inside.trim().startsWith(CSKeywords.CS_Set)) { 
                    Debug.clogn("yes it is", PropertyStatement.class);
                    return true;
                }
            } 
            Debug.clogn(" no it isn't", PropertyStatement.class);
            return false;
            
            
        }
        
        public String getPropertyName() {
            return this.propertyAsVariable.getName();
        }
        
        public String getPropertyTypeName() {
            return this.propertyAsVariable.getType();
        }
        
        public DNVariable getPropertyAsVariable() {
            return this.propertyAsVariable;
        }
        
        public DNType getDNType() {
            Library l = this.context.getLibrary();
            return l.getProgramDefinedOrLibraryDNTypeFor(this.propertyAsVariable.getType());
        }
        
        private PropertyStatement(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static PropertyStatement createCSPropertyStatement(String code, InterpretationContext context) {
            //Debug.clogn("Parse " + code, PropertyStatement.class);
            PropertyStatement ps = new PropertyStatement(code, context);
            String ccode = code.trim();
            String declaration = ccode.substring(0, ccode.indexOf("{"));
            
            List words = Util.tokenizeIgnoringEnclosers(declaration, " ");
            //Debug.clogn("words " + words, PropertyStatement.class);
            for (Iterator itr = words.iterator(); itr.hasNext();) {
                String next = ((String) itr.next()).trim();
                Debug.clogn("next " + next, PropertyStatement.class);
                if (CSKeywords.getMemberModifiers().contains(next)) {
                    ps.accessKeywords.add(next);
                } else if (CSKeywords.CS_Virtual.equals(next)) {
                    ps.canBeOverriden = true; 
                } else if (CSKeywords.CS_Override.equals(next)) {
                    ps.hidesBase = true; 
                } else if (CSKeywords.CS_New.equals(next)) {
                    ps.hidesBase = false;
                } else if (next.trim().equals("")) {
                    // ignore
                } else {
                    //Debug.clogn("type " + next, PropertyStatement.class);
                    String propertyType = next;
                    String propertyName = ((String) itr.next()).trim();
                    ps.propertyAsVariable = DNVariable.createCSVariable(propertyName, propertyType);
                } 
            }
            return ps;
            
            
        }
        
         // (scope kwds) (readability kwds) (override kwds) nameString As Type
        public static PropertyStatement createVBPropertyStatement(String code, InterpretationContext context) {
            Debug.clogn("Parse " + code, PropertyStatement.class);
            PropertyStatement ps = new PropertyStatement(code, context);
            String propString = "";
            String propTypeString = "";
            String propertyType = "System.Object";
            String propertyName;
            
            List words = Util.tokenizeIgnoringEnclosers(code.trim(), " ");
                for (Iterator itr = words.iterator(); itr.hasNext();) {
                    String nextWord = ((String) itr.next()).trim();
                    if (VBKeywords.getAccessKeywords().contains(nextWord)) {
                        ps.accessKeywords.add(nextWord);
                    } else if (nextWord.equals(VBKeywords.VB_Default)) {
                        ps.isDefault = true;
                    } else if (VBKeywords.getReadWriteKeywords().contains(nextWord)) {
                        ps.readWriteKeywords.add(nextWord);
                    } else if (VBKeywords.getOverLoadsOverRidesKeywords().contains(nextWord)) {
                        ps.overloadsOverridesKeywords.add(nextWord);
                    } else if (nextWord.equals(VBKeywords.VB_Property)) {
                        propString = (String) itr.next();
                    } else if (nextWord.equals(VBKeywords.VB_As)) {
                        
                        propertyType = ((String) itr.next()).trim(); 
                     Debug.clogn("propertyType " + propertyType, PropertyStatement.class);
                    } else if (nextWord.equals(VBKeywords.VB_Overridable) || nextWord.equals(VBKeywords.VB_NotOverridable) ) {
                        
                    } else {
                        throw new RuntimeException("Can't parse " + nextWord);
                    }
                }
                
                propertyName = propString.substring(0, propString.indexOf("(")); 
                ps.propertyAsVariable = DNVariable.createVBVariable(propertyName, propertyType);
                return ps;
        }
        
        public List getGetOrSetStatements() {
            List l = new ArrayList();
            for (Iterator itr = this.pStatements.iterator(); itr.hasNext();) {
                Statement s = (Statement) itr.next();
                if (s instanceof GetOrSetStatement) {
                    l.add(s);
                }
            }
            return l;
        }
        
        public GetOrSetStatement getGetStatement() {
            for (Iterator itr = this.pStatements.iterator(); itr.hasNext();) {
                Statement s = (Statement) itr.next();
                if (s instanceof GetOrSetStatement) {
                    GetOrSetStatement goss = (GetOrSetStatement) s;
                    if (goss.isGet()) {
                        return goss;
                    }
                }
            }
            return null;
        }
        
        public GetOrSetStatement getSetStatement() {
            for (Iterator itr = this.pStatements.iterator(); itr.hasNext();) {
                Statement s = (Statement) itr.next();
                if (s instanceof GetOrSetStatement) {
                    GetOrSetStatement goss = (GetOrSetStatement) s;
                    if (goss.isSet()) {
                        return goss;
                    }
                }
            }
            return null;
        }
        
        public void parseCS(List statements) { 
            for (ListIterator itr = statements.listIterator(); itr.hasNext();) {
                String nextStatement = ((String) itr.next()).trim();
                if (CSComment.isComment(nextStatement, context)) {
                    this.pStatements.add(new CSComment(nextStatement, this.context));
                } else if (GetOrSetStatement.isCSGetOrSetStatement(nextStatement, context)) { 
                    GetOrSetStatement goss = GetOrSetStatement.createCSGetOrSetStatement(this, nextStatement, itr, this.context); 
                    this.pStatements.add(goss);
                } else {
                    throw new RuntimeException("I don't know what this is: " + nextStatement);
                }
            }
           
        }
        
        public void parseVB(List statements) {
           
            ListIterator itr = statements.listIterator();
            itr.next(); // skip the property statement we already parsed
            while (itr.hasNext()) {
                String nextStatement = (String) itr.next();
                if (VBComment.isComment(nextStatement, super.context)) {
                    pStatements.add(new VBComment(nextStatement, super.context));
                } else if (GetOrSetStatement.isVBGetOrSetStatement(nextStatement, super.context)) {
                    GetOrSetStatement gs = GetOrSetStatement.createVBGetOrSetStatement(this, nextStatement, itr, super.context);
                    pStatements.add(gs);
                } else if (nextStatement.equals(endProp())) {
                    break;
                } else {
                    throw new RuntimeException("Shouldn't get here: " + nextStatement);
                }   
            }
        }

	public List getVariables() {
		return super.context.getVariables();
	}
       

	public String toString() {
		String s = "PropertyStatement ";
		return s;
	}

	protected List tryGetJava() {
		List strings = new ArrayList();
		
		return strings;
	}
        
        private static String endProp() {
            return VBKeywords.VB_End + " " + VBKeywords.VB_Property;
        }
        
        public static List parseCSPropertyLoop(String propertyStatement) { 
             String inside = propertyStatement.substring(propertyStatement.indexOf("{") + 1, propertyStatement.length()-1);
             List insideStatements = Util.tokenizeSemiColonChunksAndPanhandles(inside);
             return insideStatements;
        }


	public static List parseVBPropertyLoop(String propertyStatement, Iterator itr) {
		List statements = new ArrayList();
                statements.add(propertyStatement);
                while( itr.hasNext()) {
                    String nextStatement = (String) itr.next();
                    statements.add(nextStatement);
                    if (nextStatement.trim().equals(endProp())) {
                        return statements;
                    }
                }
                throw new RuntimeException("Property STatement didn't end !!");
	}
	
}

 