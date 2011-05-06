
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

		

public class GetOrSetStatement extends StatementAdapter implements StatementWithStatements { 
        public static String GET = "Get";
        public static String SET = "Set";
        private String type;
        List statements = new ArrayList();
        PropertyStatement ps;
        List args = new ArrayList();
        DNVariable implicitSetVariable;
        DNVariable implicitGetVariable;
        String returnTypeName;
        List modifiers = new ArrayList();
        

	public static boolean isVBGetOrSetStatement(String code, InterpretationContext context) {
            StringTokenizer tok = new StringTokenizer(code);
            while (tok.hasMoreTokens()) {
                String nextToken = tok.nextToken();
                if (startsWithGetOrSet(nextToken)) {
                    return true;
                }
            }
            return false;
	}
        
        private static boolean startsWithGetOrSet(String code) {
            return code.trim().equals(VBKeywords.VB_Get) ||
                        code.trim().startsWith(VBKeywords.VB_Set + "(")
                        || code.trim().endsWith(VBKeywords.VB_Set);
        }
        
        public static boolean isCSGetOrSetStatement(String code, InterpretationContext context) {
		String s = code.trim();
                return s.startsWith(CSKeywords.CS_Get) || s.startsWith(CSKeywords.CS_Set);
	}
        
        public boolean isGet() {
            return this.type.equals(GET);
        }
        
        public boolean isSet() {
            return this.type.equals(SET);
        }
        
        public MetaClass getMetaClass() {
            return super.context.getMetaClass();
        }
        
        public InterpretationContext getParent() {
            return super.context.getParent();
        }
        
        public Library getLibrary() {
            return super.context.getLibrary();
        }
        
       public DNVariable getVariable(String name) {
            for (Iterator itr = this.getVariables().iterator(); itr.hasNext();) {
		DNVariable var = (DNVariable) itr.next();
		if (var.getName().equals(name)) {
                    return var;
		}
            }
            return null;
	}
        
        public List getVariables() {
            List l = super.context.getVariables();
            l.addAll(this.args);
            if (this.implicitSetVariable != null) {
                l.add(this.implicitSetVariable);
            }
             if (this.implicitGetVariable != null) {
                l.add(this.implicitGetVariable);
            }
            return l;
        }
        
        private static String endGet() {
            return VBKeywords.VB_End + " " + VBKeywords.VB_Get;
        }
        
        private static String endSet() {
             return VBKeywords.VB_End + " " + VBKeywords.VB_Set;
        }
        
        public List getJavaSupplements() {
            return new ArrayList();
        }
        
        public List getStatements() {
            return this.statements;
        }
        
        public String getJavaName() {
            if (this.type.equals(GET)) {
                return "get" + this.ps.getPropertyName();
            } else {
                return "set" + this.ps.getPropertyName();
            }
        }
        
        public List getArgs() {
            return this.args;
        }
        
        public List getModifiers() {
            List l = new ArrayList();
            if (!this.modifiers.isEmpty()) {
                return this.modifiers;
            } else {
                return this.ps.accessKeywords;
            }
        }
        
        public String getReturnTypeName() {
           return this.returnTypeName;
        }
        
        private GetOrSetStatement(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static GetOrSetStatement createCSGetOrSetStatement(PropertyStatement ps, String code, ListIterator itr, InterpretationContext context) {
            GetOrSetStatement goss = new GetOrSetStatement(code, context);
            goss.ps = ps;
            Debug.logn("Parse GetSetStatement " + code, goss);
            if (code.trim().startsWith(CSKeywords.CS_Get)) {
                goss.type = GET;
                goss.returnTypeName = ps.getPropertyTypeName();
            } else if (code.trim().startsWith(CSKeywords.CS_Set)) {
                goss.type = SET;
                goss.implicitSetVariable = DNVariable.createCSVariable("value", ps.getPropertyTypeName());
                goss.args.add(goss.implicitSetVariable);
                goss.returnTypeName = CSKeywords.CS_Void;
            } else {
                throw new RuntimeException("Weirdness !");
            }
            String inside = code.substring(code.indexOf("{") + 1, code.length()-1);
            List stmnts = Util.tokenizeSemiColonChunksAndPanhandles(inside);
            for (ListIterator itrr = stmnts.listIterator(); itrr.hasNext();) { 
                String next = ((String) itrr.next()).trim();
                Statement statement = (new CSStatementFactory()).getStatement(next, itrr,  goss); 
                goss.statements.add(statement);
                
            }
            return goss;
        }
        
        public static GetOrSetStatement createVBGetOrSetStatement(PropertyStatement ps, String code, ListIterator itr, InterpretationContext context) {
            GetOrSetStatement goss = new GetOrSetStatement(code, context);
            goss.ps = ps;
            Debug.logn("Parse GetStatement " + code, goss);
            StringTokenizer tok = new StringTokenizer(code);
            String getOrSetCode = "";
            
            boolean readModifiers = false;
            while (tok.hasMoreTokens()) {
                String nextToken = tok.nextToken();
                if (!readModifiers && startsWithGetOrSet(nextToken)) {
                    readModifiers = true;
                }
                if (readModifiers) {
                    getOrSetCode = getOrSetCode + " " + nextToken;
                } else {
                    goss.modifiers.add(nextToken);
                }
            }
            getOrSetCode = getOrSetCode.trim();
          
            
            boolean usesImpliedSetValue = false;
            if (getOrSetCode.trim().equals(VBKeywords.VB_Get)) {
                goss.type = GET;
                goss.returnTypeName = ps.getPropertyTypeName();
            } else if (getOrSetCode.trim().startsWith(VBKeywords.VB_Set + "(")) {
                goss.type = SET;
                goss.returnTypeName = VBKeywords.VB_Nothing;
            } else if (getOrSetCode.trim().equals(VBKeywords.VB_Set)) {
                goss.type = SET;
                goss.returnTypeName = VBKeywords.VB_Nothing;
                usesImpliedSetValue = true;
            } else {
                throw new RuntimeException("Weirdness !");
            }
            
            if (getOrSetCode.indexOf("(") != -1) {
                String argString = getOrSetCode.substring(3, getOrSetCode.length());
                if (!argString.equals("()")) {
                    goss.args = Signature.parseVBArgs(Util.stripBrackets(argString));
                }
            } else if (usesImpliedSetValue) {
                goss.implicitSetVariable = DNVariable.createCSVariable("Value", ps.getPropertyTypeName());
                goss.args.add(goss.implicitSetVariable);
            }
            
            if (goss.type.equals(GET)) {
                goss.implicitGetVariable = DNVariable.createVBVariable(ps.getPropertyName(), ps.getPropertyTypeName());
                LocalVariableDeclaration lvd = new LocalVariableDeclaration( goss, goss.implicitGetVariable);
                goss.statements.add(lvd);
            }
            boolean hadReturnStatement = false;
            
            while(itr.hasNext()) {
                 String next = (String) itr.next();
                
                 if (!next.equals(endGet()) && !next.equals(endSet())) {
                     Statement nextStatement = (new VBStatementFactory()).getStatement(next, itr, goss);
                     if (nextStatement instanceof ReturnStatement) {
                         hadReturnStatement = true;
                     }
                     goss.statements.add(nextStatement);
                 } else {
                     break;
                 } 
                 
            }
            if (!hadReturnStatement && goss.type.equals(GET)) {
                goss.statements.add(ReturnStatement.createVBReturnStatement(goss, goss.implicitGetVariable));
            }
            Debug.logn("Parsed " + goss.statements, goss);
            return goss;
        }
        

	public String toString() {
		String s = "GetOrSetStatement " + type + " " + ps;
		return s;
	}

	protected List tryGetJava() {
		throw new RuntimeException("Shouldn't get here");
	}


	
}

 