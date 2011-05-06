
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
import com.sun.dn.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

public class CSEnumStatement extends CSMetaClass implements EnumStatement { 
        String varType;

	public CSEnumStatement(String code, InterpretationContext context) {
		super(code, context);
	}
        
        protected void registerType() {
            DNType type = this.getDNType();
            type.setEnum(true);
        }
        
        public String getType() {
            return this.varType;
        }
        // name string -? init value string
        public Map getVariableToExpressionMap() {
            Map mm = new LinkedHashMap();
            int i = 0;
            for (Iterator itr = super.variableMembers.iterator(); itr.hasNext();) {
                
                VariableMemberDeclaration vmd = (VariableMemberDeclaration) itr.next();

                for (Iterator itrr = vmd.getVariables().iterator(); itrr.hasNext();) {
                    DNVariable dnv = (DNVariable) itrr.next();
                    Expression e = vmd.getExpression();
                    if (e == null) {
                        e = IntegerLiteral.createCSIntegerLiteral(""+i, context);
                    }
                    mm.put(dnv, e);
                    
                }
                i++;
            }

            return mm;
        }
        
        protected String myKeyword() {
            return CSKeywords.CS_Enum;
        }
        
        protected void parseBody(String code) {
            Debug.logn("Parse " + code, this);
            List l = Util.tokenizeIgnoringEnclosers(code, ",");

            for (Iterator itr = l.iterator(); itr.hasNext();) {
                String next = ((String) itr.next()).trim();
                String varName = "";
                String varValue = "";
                varType = CSKeywords.CS_Int; // this is the default
                if (next.indexOf("=") != -1) {
                    varName = next.substring(0, next.indexOf("=")).trim();
                    varValue = next.substring(next.indexOf("=") + 1, next.length()).trim();
                    
                } else {
                    varName = next;
                } 
                DNVariable variable = DNVariable.createCSVariable(varName, varType);
                List mods = new ArrayList();
                mods.add(CSKeywords.CS_Static);
                mods.add(CSKeywords.CS_Public);
                VariableMemberDeclaration vmd = VariableMemberDeclaration.createCSVariableMemberDeclaration(variable, mods, varValue, super.context);
                super.variableMembers.add(vmd);
                
            }
            
        }


	public static boolean isCSEnumStatement(String code, InterpretationContext context) {
            Debug.clogn("a enum statement ? " + code, CSEnumStatement.class);
		if (code.indexOf("{") != -1) {
			String potentialDeclaration = Util.getUpToOpeningCurlyBracket(code.trim());
			List l = getDeclarationAsTokens(potentialDeclaration);
			boolean b =  l.contains(CSKeywords.CS_Enum);
                        Debug.clogn("it is ? " + b, CSEnumStatement.class);
                        return b;
		}
		return false;
	}


}
 