
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
import com.sun.dn.java.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/** A .NET statement declaring a local variable.
	** @author danny.coward@sun.com
	*/

public class LocalVariableDeclaration extends StatementAdapter {
	
        private List variables = new ArrayList();
	private Expression expression;
	private Library library;
        private boolean alreadyDeclared = false;
        private boolean preserveOnRedeclare = false;

	public static boolean isVBDimStatement(String code, InterpretationContext context) {
		return Util.codeContains(code, VBKeywords.VB_Dim) ||
                        Util.codeContains(code, VBKeywords.VB_ReDim);
	}

	public static boolean isCSLocalVariableDeclaration(String code, InterpretationContext context) {
		Debug.clogn("is a local var decl ? " + code, LocalVariableDeclaration.class);
		List tokens = Util.tokenizeIgnoringEnclosers(code, " ");
                String firstString = (String) tokens.get(0);
                String typeString;
                if (firstString.trim().equals(CSKeywords.CS_Const)) {
                    typeString = (String) tokens.get(1);
                } else {
                    typeString = (String) tokens.get(0);
                }
		 
		String type = DNVariable.parseCSType(typeString);
		// if this is a type in context
                Debug.clogn("type is " + type, LocalVariableDeclaration.class);
		Library library = context.getLibrary();
		try {
			DNType c = library.getProgramDefinedOrLibraryDNTypeFor(type);
                        Debug.clogn("library DN type " + c, LocalVariableDeclaration.class);
			if (c != null) {
				Debug.clogn("yes " + c, LocalVariableDeclaration.class);
				return true;
			}
		} catch (TypeResolveException tre) {
			// do nothing
                   
		}	
		Debug.clogn("no ", LocalVariableDeclaration.class);
		return false;
	}


	private LocalVariableDeclaration (String code, InterpretationContext context) {
		super(code, context);
		if (library == null) {
			library = context.getLibrary();
		}
	}

	LocalVariableDeclaration (InterpretationContext context, DNVariable variable) {
		this("<DimStatement was manufactured using the variable " + variable.getName() + ">", context);
		this.variables.add( variable);
	}

	LocalVariableDeclaration (InterpretationContext context, DNVariable variable, Expression expression) {
		this(context, variable);
		this.expression = expression;
	}
        
        public static LocalVariableDeclaration createVBLocalVariableDeclaration (String code, InterpretationContext context) {
            LocalVariableDeclaration lvd = new LocalVariableDeclaration(code, context);
            lvd.parseVB(code);
           return lvd;
        }
        
         public static LocalVariableDeclaration createCSLocalVariableDeclaration (String code, InterpretationContext context) {
            LocalVariableDeclaration lvd = new LocalVariableDeclaration(code, context);
            lvd.parseCS(code);
           return lvd;
        } 

	// used by invocation expressions with literal array args
	public static LocalVariableDeclaration createVBLocalVariableDeclaration (InterpretationContext context, ArrayCreationExpression ace, String name) {
		LocalVariableDeclaration lvd = new LocalVariableDeclaration("<LVD was manufactured using the variable " + name + ">", context);
		lvd.expression = ace;
                DNVariable v = DNVariable.createVBVariable(name, ace.getName() + "()");
		lvd.variables.add(v);
		return lvd;
	}

	public static LocalVariableDeclaration createCSLocalVariableDeclaration (InterpretationContext context, ArrayCreationExpression ace, String name) {
		LocalVariableDeclaration lvd = new LocalVariableDeclaration("<LVD was manufactured using the variable " + name + ">", context);
		lvd .expression = ace;
		DNVariable v = DNVariable.createCSVariable(name, ace.getName() + "[]");
                lvd.variables.add(v);
		return lvd;
	}

	private void parseCS(String code) {
            Debug.logn("Parse ." + code + ".", this);
            List pieces = Util.tokenizeIgnoringEnclosers(code, " ");
            String firstString = (String) pieces.get(0);
            String typeString;
            if (firstString.trim().equals(CSKeywords.CS_Const)) {
                typeString = ((String) pieces.get(1));
            } else {
                typeString = ((String) pieces.get(0));
            }
            String rest = code.substring(code.indexOf (typeString) + typeString.length() + 1, code.length());
            
            String nameString = "";
            List equalsList = Util.tokenizeIgnoringEnclosers(rest, "=");
            if (equalsList.size() > 1) {
                nameString = ((String) equalsList.get(0)).trim();
                String expressionString = ((String) equalsList.get(1)).trim();
		this.expression = (new CSExpressionFactory()).getExpression(expressionString, this.context);
            } else {
                nameString = rest;
            }
            StringTokenizer stt = new StringTokenizer(nameString, ",");
            while(stt.hasMoreTokens()) {
                String nextName = stt.nextToken().trim();
                DNVariable nextVariable = DNVariable.createCSVariable(nextName, typeString);
                this.variables.add(nextVariable);
                Debug.logn("Variable is a point ?" + nextVariable.isPoint(), this);
            } 
            Debug.logn("Parsed", this);
	}
        
        private void parseNames(String name, Iterator itr, List names) {
            String nextName = name;
            names.add(stripComma(nextName));
            boolean reachedEnd = !nextName.trim().endsWith(",");
            while (itr.hasNext() && !reachedEnd)
            if (nextName.trim().endsWith(",")) {
                nextName = (String) itr.next();
                names.add(stripComma(nextName));
            } else {
                reachedEnd = true;
            }
        }
        
        private String stripComma(String s) {
            if (s.endsWith(",")) {
                return s.substring(0, s.length()-1);
            } else {
                return s;
            }
        }
	
	private void parseVB(String code) {
            Debug.logn("Parse ." + code + ".", this);
            List pieces = Util.tokenizeIgnoringEnclosers(code, " ");
            Iterator itr = pieces.iterator();
            String name = "";
            List names = new ArrayList();
            String type = "";
            String rest = null;
            boolean isEmbeddedNew = false; // Dim a as New String() would be 'true' instead for example
                
            while(itr.hasNext()) {
		String nextToken = (String) itr.next();
		if (VBKeywords.VB_Dim.equals(nextToken)) {
                    //assume simple form...
                    name = (String) itr.next();
                    parseNames(name, itr, names);
                    
                } else if (VBKeywords.VB_ReDim.equals(nextToken)) {
                    this.alreadyDeclared = true;
                    String next = (String) itr.next();
                    if (next.equals(VBKeywords.VB_Preserve)) {
                        name = (String) itr.next();
                        parseNames(name, itr, names);
                        
                        this.preserveOnRedeclare = true;
                    } else {
                        name = next;
                        parseNames(name, itr, names);
                        
                    }
                } else if (VBKeywords.VB_Preserve.equals(nextToken)) {
                            
                } else if (VBKeywords.VB_As.equals(nextToken)) {
                    String nextAfterAs = (String) itr.next();
                    if (nextAfterAs.equals(VBKeywords.VB_New)) {
                        Debug.logn("Embedded new", this);
                        isEmbeddedNew = true; //e.g. Dim a as New Foo("param1", "param2") 
			String nextAfterNew = (String) itr.next();
			rest = nextAfterAs + " " + nextAfterNew;
                        Debug.logn("Rest = " + rest, this);
			expression = (new VBExpressionFactory()).getExpression(rest, this.context);
			type = expression.getTypeName();

                    } else {
			type = nextAfterAs;
			rest = code.substring( code.indexOf(type) + type.length(), code.length());
			if ( rest.indexOf("=") != -1 ) {
                            rest = rest.substring(rest.indexOf("=") + 1, rest.length()).trim();
                            expression = (new VBExpressionFactory()).getExpression(rest, this.context, type, true);

			}
                    }
		} 
            }
            Debug.logn("type ." + type + ".", this);
            for (Iterator itrr = names.iterator(); itrr.hasNext();) {
                String nextName = (String) itrr.next();
                DNVariable v = null;
                if (!this.alreadyDeclared) {
                    v = DNVariable.createVBVariable(nextName, type);
                    //this.variables.add(v);
                } else {
                    String shortName = DNVariable.parseVBName(nextName);
                    v = this.context.getVariable(shortName);
                    if (v != null) {
                        List newDimension = DNVariable.parseVBDimension(nextName);
                        v.resetDimension(newDimension);
                    } else {
                        throw new RuntimeException("Error locating variable for ReDim");
                    }
                }
                String jType = library.getJavaTypeFor(v.getType());
                v.setJType(jType);
                this.variables.add(v);
            }
            Debug.logn("got " + this, this);
	}
        
        public List getVariables() {
            return this.variables;
        }
        
        private List getJavaForVariable(DNVariable var) {
            String s;
            List l = new ArrayList();
                // this is a whole different case
            if (var.isDimensionlessArray()) {
                if (this.getExpression() == null) {
                    l.add("// array " + var.getName() + " dimensionless - declare later");
                    return l;
		} else if (this.getExpression() instanceof ArrayElementInitializer) {
                    s = var.asJavaDeclaration(library, !this.alreadyDeclared );
                    s = s + " = " + this.getExpression().asJava() + "; // dim statement";
                    l.add(s);
                    //if (true) throw new RuntimeException("here");
                    return l;
		}
            }
		
            s = var.asJavaDeclaration(library, !this.alreadyDeclared);
                
            if (var.isPoint()) {
                if (this.getExpression() != null) {
                    s = s + " = " + this.getExpression().asJava();
		} else {
                    String javaType = library.getJavaTypeFor(var.getVBFullType());
                    if (JavaPrimitives.isPrimitive(javaType)) {
                        s = s + " = " + JavaPrimitives.getDefaultJavaFor(javaType);
                    } else {
			s = s + " = null";
                    }
		}
		s = s + ";";
		l.add(s);
            } else {
		if (this.getExpression() instanceof ArrayCreationExpression) {
                   s = s + " = " + this.getExpression().asJava();
		} else if (this.getExpression() instanceof UntranslatedExpression) {
                   s = s + " = " + this.getExpression().asJava();
                } else if (this.getExpression() != null) {
                    
                    throw new RuntimeException("inconsistent state of a local variable declaration");
		}
		s = s + "; // dim statement";
		l.add(s);
            }
            return l;
        }

	protected List tryGetJava() {
            List l = new ArrayList();
            
            for (Iterator itr = this.variables.iterator(); itr.hasNext();) {
                DNVariable v = (DNVariable) itr.next();
                l.addAll(this.getJavaForVariable(v));
            }
            return l;
	}

	private Expression getExpression() {
		return this.expression;
	}

	public String toString() {
		return "LVD: " + this.variables + " exp: " + this.expression;
	}

}


 