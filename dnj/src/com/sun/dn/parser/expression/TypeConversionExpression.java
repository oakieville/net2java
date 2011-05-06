
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
package com.sun.dn.parser.expression;

import com.sun.dn.*;
import java.util.*;
import com.sun.dn.parser.statement.MethodCall;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/** A VB expression that converts an expression into a given type.
	** @author danny.coward@sun.com
	**/


public class TypeConversionExpression extends MethodCall implements Expression {
	public static String CBOOL = "CBool";
	public static String CBYTE = "CByte";
	public static String CCHAR = "CChar";
	public static String CDATE = "CDate";
	public static String CDBL = "CDbl";
	public static String CDEC = "CDec";
	public static String CINT = "CInt";
	public static String CLNG = "CLng";
	public static String COBJ = "CObj";
	public static String CSHORT = "CShort";
	public static String CSNG = "CSng";
	public static String CSTR = "CStr";
	public static String CTYPE = "CType";
        public static String TRYCAST = "TryCast";
	public static String CS_CAST = "CS_CAST";
	public String csClassname;
	public Library library;
	//private String code;

	public static boolean isVBTypeConversionExpression(String code, InterpretationContext context) {
		return code.trim().startsWith(CBOOL)
			|| code.trim().startsWith(CBYTE )
			|| code.trim().startsWith(CCHAR )
			|| code.trim().startsWith(CDATE )
			|| code.trim().startsWith(CDBL )
			|| code.trim().startsWith(CDEC )
			|| code.trim().startsWith(CDEC )
			|| code.trim().startsWith(CINT)
			|| code.trim().startsWith(CLNG )
			|| code.trim().startsWith(COBJ )
			|| code.trim().startsWith(CSHORT )
			|| code.trim().startsWith(CSNG )
			|| code.trim().startsWith(CSTR )
                        || code.trim().startsWith(TRYCAST) 
			|| code.trim().startsWith(CTYPE);
	}
	
	public static boolean isCSTypeConversionExpression(String code, InterpretationContext context) {
		String innerCode = Util.stripBrackets(code);
                Debug.clogn("Is TypeConversionExpression? " + code, TypeConversionExpression.class);
                
                // There are 2 kinds, a cast like in Java 
                // (String) object
                // or using the as keyword
                if (innerCode.startsWith("(")) {
                    List l = Util.tokenizeIgnoringEnclosers(innerCode, ")");
                    String potentialClass = (String) l.get(0);
                    potentialClass = potentialClass.substring(1, potentialClass.length());

                    DNType c = context.getLibrary().getLibraryData().getLibraryClass(potentialClass);
                
                    if (c != null || context.getLibrary().containsProgramDefinedDNType(potentialClass)) {
                        Debug.clogn("YES ", TypeConversionExpression.class);
                        return true;
                    }
                }
                List l = Util.tokenizeIgnoringEnclosers(code, " ");
                if (l.size() == 3 && CSKeywords.CS_As.equals(l.get(1))) {
                     Debug.clogn("YES ", TypeConversionExpression.class);
                    return true;
                }
                Debug.clogn("NO ", TypeConversionExpression.class);
		return false; 
	}

	public static TypeConversionExpression createCSTypeConversionExpression(String code,  InterpretationContext context) {
		TypeConversionExpression tce = new TypeConversionExpression(code, context);
		String innerCode = Util.stripBrackets(code);
                List l = Util.tokenizeIgnoringEnclosers(code, " ");
               
                if (l.size() == 3 && CSKeywords.CS_As.equals(l.get(1))) {
                    tce.csClassname = (String) l.get(2);
                    String paramS = (String) l.get(0);
                    tce.setName(CS_CAST, tce);
                    tce.parseParamStrings(paramS, new CSExpressionFactory());
                    
                } else {
                    tce.csClassname = innerCode.substring(1, innerCode.indexOf(")"));

                    tce.setName(CS_CAST, tce);
                    String paramS =  innerCode.substring(innerCode .indexOf(")")+1, innerCode.length());

                    paramS = Util.stripBrackets(paramS);
                    tce.parseParamStrings(paramS, new CSExpressionFactory());
                }
		return tce;
	}

	public static TypeConversionExpression createVBTypeConversionExpression(String code,  InterpretationContext context) {
		TypeConversionExpression tce = new TypeConversionExpression(code, context);
		String s = code.trim();

		String nm = s.substring(0, s.indexOf("("));
		tce.setName(nm, tce);

		String paramS =  s.substring(s.indexOf("(")+1, s.length());
		paramS = paramS.substring(0, paramS.length() -1 );

		tce.parseParamStrings(paramS, new VBExpressionFactory());
                if (nm.equals(TRYCAST)) {
                    tce.csClassname = ((Expression) tce.getParameters().get(1)).getOriginalCode();
                }
		return tce;
	}

	private TypeConversionExpression(String code, InterpretationContext context) {
		super(code, context);
		this.library = context.getLibrary();
	}

	public String getTypeName() {
		if (super.getName().equals(CBOOL)) {
			return "Boolean";
		} else if (super.getName().equals(CBYTE )) {
			return "Byte";
		} else if (super.getName().equals(CCHAR )) {
			return "Char";
		} else if (super.getName().equals(CDATE )) {
			return "Date";
		} else if (super.getName().equals(CDBL )) {
			return "Double";
		} else if (super.getName().equals(CDEC )) {
			return "Decimal";
		} else if (super.getName().equals(CINT)) {
			return "Integer";
		} else if (super.getName().equals(CLNG )) {
			return "Long";
		} else if (super.getName().equals(COBJ )) {
			return "Object";
		} else if (super.getName().equals(CSHORT )) {
			return "Short";
		} else if (super.getName().equals(CSNG )) {
			return "Single";
		} else if (super.getName().equals(CSTR)) {
			return "String";
		} else if (super.getName().equals(CTYPE)) {


			Expression e = (Expression) super.getParameters().get(1);
			return e.getTypeName();
		} else if (super.getName().equals(CS_CAST) || super.getName().equals(TRYCAST)) {
			DNType c = library.getProgramDefinedOrLibraryDNTypeFor(csClassname);
			if (c.isInterface()) {
				Expression e = (Expression) super.getParameters().get(0);
				return e.getTypeName();
			} else {
				return csClassname;
			}
		} else {
			return "not implemented get Type for this kind of type conversion: " + super.getName();
		}
	}

	public DNType getDNType() {
		return this.library.getProgramDefinedOrLibraryDNTypeFor(this.getTypeName());
	}

	public String tryAsJava() {
		if (super.getName().equals(CINT)) {
			String objectE = "new Integer(" + super.paramsAsJava() + ")";
			return "(" + objectE + ").intValue()";
		} else if (super.getName().equals(CBOOL)) {
			String objectE = "new Boolean(" + super.paramsAsJava() + ")";
			return "(" + objectE + ").booleanValue()";
		} else if (super.getName().equals(CDBL) || super.getName().equals(CDEC)) {
			String objectE = "new Double(" + super.paramsAsJava() + ")";
			return "(" + objectE + ").doubleValue()";
		} else if (super.getName().equals(CCHAR)) {
			String primitiveE = "( (String) "+super.paramsAsJava()+" ).toCharArray()[0]";
			return primitiveE;
                } else if (super.getName().equals(CBYTE)) {
			String objectE = "new Byte(\"" + super.paramsAsJava() + "\")";
			return "(" + objectE + ").byteValue()";
                 } else if (super.getName().equals(CSHORT)) {
			String objectE = "new Short(\"" + super.paramsAsJava() + "\")";
			return "(" + objectE + ").shortValue()";
                } else if (super.getName().equals(CSTR)) {
			String objectE =  super.paramsAsJava() + ".toString()";
			return objectE;
                        
                } else if (super.getName().equals(CSNG)) {
			String objectE = "new Float(" + super.paramsAsJava() + ")";
			return "(" + objectE + ").floatValue()";
                } else if (super.getName().equals(CDATE)) {
			String objectE = "new Date(" + super.paramsAsJava() + ")";
			return objectE;
                } else if (super.getName().equals(CLNG)) {
			String objectE = "new Long(" + super.paramsAsJava() + ")";
			return "(" + objectE + ").longValue()";
                } else if (super.getName().equals(COBJ)) {
			return super.paramsAsJava();
		} else if (super.getName().equals(CTYPE)) {
			String vbType = this.getTypeFor(super.getParameters().get(1));
			String jType = library.getJavaTypeFor(vbType);
			return "(" + jType + ") " + this.getJavaFor(super.getParameters().get(0));
		} else if (super.getName().equals(CS_CAST) || super.getName().equals(TRYCAST)) {
			DNType c = library.getProgramDefinedOrLibraryDNTypeFor(csClassname);
			
			if (c.isInterface()) {
				Expression e = (Expression) super.getParameters().get(0);
				return e.asJava();
			} else {
                            String jType = library.getJavaTypeFor(csClassname);
                            String objJava = this.getJavaFor(super.getParameters().get(0));
                            String castJava = "(" + jType + ") " + objJava;
                            if (super.getName().equals(CS_CAST)) {
                                return castJava;
                            } else {
                                return "(" + objJava + " instanceof " + jType + " ? " + castJava + " : null)";
                            }
			}
		} else {
			return "TypeConversion:" + super.getName() + " of " + super.paramsAsJava();
		}
	}

	private String getJavaFor(Object variableOrExpression) {
		if (variableOrExpression instanceof DNVariable) {
			return ((DNVariable) variableOrExpression).getName();
		} else if (variableOrExpression instanceof Expression) {
			return ((Expression) variableOrExpression).asJava();
		} else {
			throw new RuntimeException("wrong type!");
		}

	}

	private String getTypeFor(Object variableOrExpression) {
		if (variableOrExpression instanceof DNVariable) {
			return ((DNVariable) variableOrExpression).getType();
		} else if (variableOrExpression instanceof Expression) {
			return ((Expression) variableOrExpression).getTypeName();
		} else {
			throw new RuntimeException("wrong type!");
		}

	}


	public String toString() {
		return "TypeConv: " + super.getName() + " " + super.getParameters();
	}

}
 