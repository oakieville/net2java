
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
import com.sun.dn.parser.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;
import com.sun.dn.library.LibraryData;

	/** A VB enumeration.
	@author danny.coward@sun.com
	*/

public class VBEnumStatement extends StatementAdapter implements InterpretationContextWithStatements, EnumStatement {
	protected List statements = new ArrayList();
	protected List modifiers = new ArrayList();
	protected String type = "Integer";
	private String name;
	private Map memberMap = new LinkedHashMap();
        private Map variableToExpressionMap = null;

	public VBEnumStatement (String statement, Iterator nextStatements, InterpretationContext context) {
		super(statement, context);
		List allStatements = new ArrayList();
		allStatements.add(statement);
		String nextStatement = "";
		while (!isEndEnum(nextStatement)) {
			nextStatement = (String) nextStatements.next();
			allStatements.add(nextStatement);
		}
		this.parseFromList(allStatements);
		context.getLibrary().createEnumerationDNType(name, this);
		Debug.logn("" + this, this);
	}

	private Map getMemberMap() {
		return this.memberMap;
	}
        
        public Map getVariableToExpressionMap() {
            if (this.variableToExpressionMap != null) {
                return variableToExpressionMap;
            }
            variableToExpressionMap = new LinkedHashMap();
            int i = 0;
            for (Iterator itr = this.memberMap.keySet().iterator(); itr.hasNext();) {
                String mName = (String) itr.next();
                String mValue = (String) this.memberMap.get(mName);
                if (!"".equals(mValue)) {
                    i = Integer.parseInt(mValue);
                } else {
                    mValue = "" + i;
                }
                DNVariable dnv = DNVariable.createVBVariable(mName, this.type);
                Expression e = (new VBExpressionFactory()).getExpression(mValue, context);
                variableToExpressionMap.put(dnv, e);
                i++;
            }
            
            return variableToExpressionMap;
        }

	public String getType() {
		return type;
	}

	public String toString() {
		return "EnumS: nm: " + name + " md: " + modifiers + " typ: " + type + " mems: " + memberMap; 
	}

	private void parseFromList(List allStatements) {
		Debug.logn("" + allStatements, this);
		String decl = (String) allStatements.get(0);
		this.parseDecl(decl);
		for (int i = 1; i < allStatements.size() -1; i++) {
			String next = ((String) allStatements.get(i)).trim();
			List tokens = Util.tokenizeIgnoringEnclosers(next, " ");
			String nextName = (String) tokens.get(0);
			String initValue = "";
			if ( tokens.size() > 1 ) {
				String equals = (String) tokens.get(1);
				initValue = (String) tokens.get(2);
			}
			this.memberMap.put(nextName, initValue);
		}
	}

	private void parseDecl(String decl) {
		List tokens = Util.tokenizeIgnoringEnclosers(decl, " ");
		Iterator itr = tokens.iterator();
		while (itr.hasNext()) {
			String next = (String) itr.next();
			if (next.equals(VBKeywords.VB_Enum)) {
				name = (String) itr.next();
			} else if (next.equals(VBKeywords.VB_As)) {
				type = (String) itr.next();
			} else {
				this.modifiers.add(next);
			}
		}
	}

	private static boolean isEndEnum(String statement) {
		return statement.trim().equals(VBKeywords.VB_End + " " + VBKeywords.VB_Enum);
	}

	public String getFQName() {
		return this.name;
	}

	public List getModifiers() {
		return this.modifiers ;
	}

	private List getStatements() {
		return this.statements;
	}

	public static boolean isEnumStatement(String statement) {
		List l = Util.tokenizeIgnoringEnclosers(statement, " ");
		return l.contains(VBKeywords.VB_Enum);
	}

	public List getVariables() {
		List variables = new ArrayList();
		return variables;
	}

	public DNVariable getVariable(String name) {
		String realName = name.trim();
		
		for (Iterator itr = this.getVariables().iterator(); itr.hasNext();) {
			DNVariable var = (DNVariable) itr.next();
			if (var.getName().equals(realName )) {
				
				return var;
			}
		}
		
		return null;
	}

	public void addStatement(Statement s) {
		this.statements.add(s);
	}

	public Library getLibrary() {
		return this.context.getLibrary();
	}

	public MetaClass getMetaClass() {
		return this.context.getMetaClass();
	}

	public InterpretationContext getParent() {
		return this.context;
	}
}

 