
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
import com.sun.dn.parser.expression.*;
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

		/**
	A VB static or instance variable declaration. Defined as  <br>
	VariableMemberDeclaration ::= <br>
	[ Attributes ] [ VariableModifier+ ] [ Dim ] VariableDeclarators LineTerminator <br> <br>

	VariableModifier ::= AccessModifiers | Shadows | Shared | ReadOnly | WithEvents <br> <br>

	VariableDeclarators ::= <br> 
   		VariableDeclarator | <br>
   		VariableDeclarators , VariableDeclarator <br> <br>

	VariableDeclarator ::= <br> 
   		VariableIdentifiers [ As TypeName ] | <br>
		VariableIdentifier [ As [ New ] TypeName [ ( ArgumentList ) ] ] [ = VariableInitializer ] <br> <br>
	
	VariableIdentifiers ::= <br>
   		VariableIdentifier | <br>
   		VariableIdentifiers , VariableIdentifier <br> <br>

	VariableIdentifier ::= Identifier [ ArrayNameModifier ] <br> <br>

	@author danny.coward@sun.com
	**/

public class VariableMemberDeclaration extends StatementAdapter {
	private List modifiers = new ArrayList();
	private List variables = new ArrayList();
	private Expression expression;
	private boolean isConstant = false;
        private boolean hidesBase = false;

	private VariableMemberDeclaration(String code, InterpretationContext context) {
		super(code, context);
	}
        
        public static VariableMemberDeclaration createVBVariableMemberDeclaration(String code, InterpretationContext context) {
            VariableMemberDeclaration vmd = new VariableMemberDeclaration(code, context);
            vmd.parseVB(code);
            return vmd;
        }
    
        public static VariableMemberDeclaration createCSVariableMemberDeclaration(String code, InterpretationContext context) {
            VariableMemberDeclaration vmd = new VariableMemberDeclaration(code, context);
            vmd.parseCS(code);
            return vmd;
        }
        
        public static VariableMemberDeclaration createCSVariableMemberDeclaration(DNVariable variable, List mods, String initValueString, InterpretationContext context) {
            VariableMemberDeclaration vmd = new VariableMemberDeclaration("<This was created by the parser for the variable" + variable + ">", context);
            vmd.variables.add(variable);
            vmd.modifiers.addAll(mods);
            if (initValueString != null && !"".equals(initValueString)) {
                vmd.expression = (new CSExpressionFactory()).getExpression(initValueString, context);
            }
            return vmd;
        }

	public static boolean isCSVariableMember(String code, InterpretationContext context) {
                String beforeEquals = code;
                if (Util.tokenizeIgnoringEnclosers(code, "=").size() > 1) {
                    beforeEquals = code.substring(0, code.indexOf("="));
                }
		boolean isIt = beforeEquals.indexOf("{") == -1 && !Util.codeContains(beforeEquals, CSKeywords.CS_Abstract) && !Util.codeContains(beforeEquals, CSKeywords.CS_Extern);
                Debug.clogn("Is ? " + isIt, VariableMemberDeclaration.class);
                return isIt;
	}
        
        public boolean isConstant() {
            return this.isConstant;
        }

	public List getVariables() {
		return variables;
	}
        
        public String getTypeName() {
            // ok they all should have the same type - check the parsing methods
            return ((DNVariable) this.variables.get(0)).getType();
        }
        
        public boolean definesVariableOfName(String name) {
            for (Iterator itr = this.variables.iterator(); itr.hasNext();) {
                DNVariable v = (DNVariable) itr.next();
                if (v.getName().equals(name)) {
                    return true;
                }
            } 
            return false;
        }

	public Expression getExpression() {
		return this.expression;
	}

	public List getModifiers() {
		return this.modifiers;
	}

	public boolean isWithEvents() {
		return modifiers.contains(VBKeywords.VB_WithEvents);
	}

	/**
		ms-help://MS.VSCC.2003/MS.MSDNQTR.2003FEB.1033/csspec/html/vclrfcsharpspec_10_4.htm
	*/
	
	public void parseCS(String s) {
		Debug.logn("Parse ." + s + ".", this);
                List l = Util.tokenizeForWhitespace(s);
                Debug.logn("Pieces =  ." + l + ".", this);
		Iterator pieces = Util.tokenizeForWhitespace(s).iterator();
		// modifiers
		boolean isReading = true;
		String nextKW = null;
		while (isReading) {
			nextKW = ((String) pieces.next()).trim();
			if (CSKeywords.getMemberModifiers().contains(nextKW) ||
                                nextKW.equals(CSKeywords.CS_Readonly) ||
                                nextKW.equals(CSKeywords.CS_Volatile) ||
                                nextKW.equals(CSKeywords.CS_Const)) {
                            modifiers.add(nextKW);      
			} else if (nextKW.equals(CSKeywords.CS_New)) {
                            this.hidesBase = true;
                        } else if (nextKW.equals(CSKeywords.CS_Override)) {
                            this.hidesBase = false;
                        } else {
				isReading = false;
			}
		}
                Debug.logn("nextKW: ." + nextKW + ".", this);
		String type = nextKW;
		String name = (String) pieces.next();
		DNVariable variable = DNVariable.createCSVariable(name, type);
		variables.add(variable);
		if (pieces.hasNext()) {
                        String equals = (String) pieces.next();
                        Debug.logn("equals = " + equals, this);
                        String initExpression = (String) pieces.next();
                        while (pieces.hasNext()) {
                            initExpression = initExpression + " " + pieces.next();
                        }
			
			Debug.logn("Init expression =." + initExpression + ".", this);
                        
			this.expression = (new CSExpressionFactory()).getExpression(initExpression, context);
		}

	}

	public void parseVB(String s) {
		Debug.logn("Parse ." + s + ".", this);

		// VariableModifier := AccessModifiers | Shadows | Shared | ReadOnly | WithEvents

		Iterator pieces = Util.tokenizeIgnoringEnclosers(s, " ").iterator();

		boolean isReading = true;
		String nextKW = null;
		while (isReading) {
			nextKW = (String) pieces.next();
			if (VBKeywords.getVariableModifierKeywords().contains(nextKW)) {
				modifiers.add(nextKW);
                                if (nextKW.equals(VBKeywords.VB_Shadows)) {
                                    this.hidesBase = true;
                                }
			} else if (nextKW.equals(VBKeywords.VB_Const)) {
				isConstant = true;
			} else if (nextKW.equals(VBKeywords.VB_Dim)) {

			} else if (isVBAttribute(nextKW)) {
                            String commentS = "'Translator: Uses the following metadata: " + getAttributeString(nextKW);
                            VBComment comment = new VBComment(commentS, super.context);
                            this.addConstructedPreStatement(comment);
                        } else {
				isReading = false;
			}
		}
		Debug.logn("Modifiers " + modifiers, this); 
		//assume no duplicate modifiers....

		// the rest of the string is VariableDeclarators 

		List rawNames = new ArrayList();
		boolean readingNames = true;
		while (readingNames) {
                    String name;
                    if (nextKW.equals(VBKeywords.VB_Dim)) {
                        // doesn't seem to do anything...
			name = (String) pieces.next();
                    } else {
			name = nextKW;
                    }
                    rawNames.add(name);
                    if (!name.endsWith(",")) {
			readingNames = false;
                    } else {
			nextKW = (String) pieces.next();
                    }
		}
                Debug.logn("rawNames " + rawNames, this);
		String type = "";
		if (pieces.hasNext()) {
                    String asOrEquals = (String) pieces.next();;
                    if (asOrEquals.equals(VBKeywords.VB_As)) {
                        String nextPiece = (String) pieces.next();
                        if (nextPiece.equals(VBKeywords.VB_New)) {
                            String nextAfterNew = (String) pieces.next();
                            String rest = nextPiece + " " + nextAfterNew;
                            Debug.logn("Rest = " + rest, this);
                            expression = (new VBExpressionFactory()).getExpression(rest, this.context);
                            type = expression.getTypeName();
                            
                            
                        } else {
                            type = nextPiece;
                        }
                    } else if (asOrEquals.equals("=")) {

                    } else {
			throw new RuntimeException("Shouldn't get here " + s);
                    }
		}

		if (type.equals("")) {
			throw new RuntimeException("Cannot handle non typed variable declarations: " + s);
		}
		for (Iterator itr = rawNames.iterator(); itr.hasNext();) {
			String rawName = (String) itr.next();
			String name = this.trimRawName(rawName);
			DNVariable variable = DNVariable.createVBVariable(name, type);
			variable.setMember(true);
			Debug.logn("Adding Var " + variable, this);
			variables.add(variable);
			if (s.indexOf("=") != -1) {
				String defaultValueString = s.substring(s.indexOf("=") + 1, s.length()).trim();
				Debug.logn("Default Value " + defaultValueString, this);
				this.expression = (new VBExpressionFactory()).getExpression(defaultValueString, this.context);
			}
		}
                Debug.logn("Variables are " + variables, this);
                Debug.logn("Expression is " + expression, this);

	}
        
        public String toString() {
            return "VariableMember: vars:" + variables + " ex:" + expression;
        }

	private String trimRawName(String rawName) {
		if (rawName.endsWith(",")) {
			return rawName.substring(0, rawName.length()-1);
		} else {
			return rawName;
		}
	}

}

 