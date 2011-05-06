
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

import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.Library;

	/** A .NET expression for creating new objects. Defined as <br>
	NewExpression ::=
	ObjectCreationExpression |
	ArrayCreationExpression |
	DelegateCreationExpression
	@author danny.coward@sun.com
	**/

public abstract class NewExpression extends ExpressionAdapter implements HasConstructedStatements {
	protected List constructedPreStatements = new ArrayList();

	public static boolean isVBNewExpression(String code, InterpretationContext context) {
		return code.startsWith(VBKeywords.VB_New + " ");
	}

	public static boolean isCSNewExpression(String code, InterpretationContext context) {
		return code.startsWith(CSKeywords.CS_New + " ");
	}
        
       protected NewExpression(String code, InterpretationContext context) {
           super(code, context);
       }

	public static NewExpression createCSNewExpression(String code, InterpretationContext context) {
            Library library = context.getLibrary();
            List args = new ArrayList();
            String className = "";
            List arrayInitializer = null;
            String s = code.trim();

            Debug.clogn("Parse expresson " + s, NewExpression.class);
            StringTokenizer st = new StringTokenizer(s);
            String newK = st.nextToken();  
    
          
            String rest = s.substring(newK.length(), s.length()).trim();
             
            
      
            if (Util.tokenizeIgnoringEnclosers(rest, "[").size() == 1) { 
            //if (rest.indexOf("[") == -1) {
                         
            	className = rest.substring( 0, rest.indexOf("(") );
                
                Debug.clogn("Class Name " + className, NewExpression.class);
		String argString = rest.substring( rest.indexOf("(")+1, rest.lastIndexOf(")") );
		
		Debug.clogn("Arg String " + argString, NewExpression.class);
		DNType c = library.getProgramDefinedOrLibraryDNTypeFor(className);
		if (c.isDelegate()) {
                    Debug.clogn("Its a delegate " + s, NewExpression.class);
                    Expression e = null;
                    String methodName = "";
                    if (argString.indexOf(".") != -1) {
                        String expressionS = argString.substring(0, argString.lastIndexOf("."));
                        methodName = argString.substring(argString.lastIndexOf(".") + 1, argString.length());
                        e = (new CSExpressionFactory()).getExpression(expressionS, context);
                    } else {
                        e = new CSThisExpression(code, context);
                        methodName = argString.trim();
                    }
                    return new DelegateCreationExpression(code, className, e, methodName, context);
		} else {
                    Debug.clogn("Its a new object " + s, NewExpression.class);
                    List argList = Util.tokenizeIgnoringEnclosers(argString, ",");
                    //Debug.clogn("argList " + argList, NewExpression.class);
                    for (Iterator itr = argList.iterator(); itr.hasNext();) {
			String nextArg = ((String) itr.next()).trim();
			Expression e = (new CSExpressionFactory()).getExpression(nextArg, context);
			Debug.clogn("Adding arg " + e, NewExpression.class);
			args.add(e);
                    }
                    List possiblyReplacedArgs = new ArrayList();
                    List constructedPreStatements = MethodCall.createCSArrayCreationDeclarationsAndSubstituteParams(args, possiblyReplacedArgs, context);

                   
                    ObjectCreationExpression oce = new ObjectCreationExpression(code, className, possiblyReplacedArgs, CSKeywords.CS_New, context);
                    oce.constructedPreStatements = constructedPreStatements;
                    return oce;
                }
            } else {
		Debug.clogn("this is an array initializer: " + rest, NewExpression.class);
		// oops. If I'm not the right hand side of an assignment then we are in trouble.

    		className = rest.substring(0, rest.indexOf("["));
		Debug.clogn("array Classname: " + className, NewExpression.class);

		arrayInitializer = new ArrayList();
		String initExpressionArrayString = rest.substring(rest.indexOf("{") + 1, rest.lastIndexOf("}"));
		Debug.clogn("initExpressionArrayString: " + initExpressionArrayString, NewExpression.class);
		for (Iterator itr = Util.tokenizeIgnoringEnclosers(initExpressionArrayString, ",").iterator(); itr.hasNext();) {
                    String nextE = ((String) itr.next()).trim();
                    if (!CSComment.stripComments(nextE, context).trim().equals("")) {
                        Expression nextExp = (new CSExpressionFactory()).getExpression(nextE, context);
                        arrayInitializer.add(nextExp);
                    }
		}
		return new ArrayCreationExpression(code, className, arrayInitializer, args, context);
            }
	}

	public static NewExpression createVBNewExpression(String code, InterpretationContext context) {
		List args = new ArrayList();
		String className = "";
		List arrayInitializer = null;
		String s = code.trim();
		Debug.clogn("Parse " + s, NewExpression.class);
		StringTokenizer st = new StringTokenizer(s);
		String newK = st.nextToken();
		String rest = s.substring(newK.length() + 1, s.length());
		Debug.clogn("--" + rest, NewExpression.class);
		if (rest.indexOf("(") == -1) {
			className = rest.trim();
		} else {
			className = rest.substring( 0, rest.indexOf("(") );
			String argString = rest.substring( rest.indexOf("(")+1, rest.lastIndexOf(")") );
			Debug.clogn("Class Name " + className, NewExpression.class);
			Debug.clogn("Arg String " + argString, NewExpression.class);                   
			StringTokenizer stt = new StringTokenizer(argString, ",");
			while(stt.hasMoreTokens()) {
				String nextArg = stt.nextToken();
				Expression e = (new VBExpressionFactory()).getExpression(nextArg, context);
				args.add(e);
			}
		}

		// is there an ArrayElementInitializer  ??
		if (code.indexOf('{') != -1) {
			arrayInitializer = new ArrayList();
			String initString = code.substring( code.indexOf('{') + 1, code.indexOf('}') );
			List initStrings = Util.tokenizeIgnoringEnclosers(initString, ",");
			for (Iterator itr = initStrings.iterator(); itr.hasNext();) {
				String nextInitString = ((String) itr.next()).trim();
				Expression e = (new VBExpressionFactory()).getExpression(nextInitString, context);
				arrayInitializer.add(e);
			}
		}
                DNType type = context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(className);
                if (type.isDelegate()) {
                    
                    AddressOfExpression aoe = null;
                    if (!(args.get(0) instanceof AddressOfExpression)) {
                        throw new RuntimeException("Expecting a method address");
                    }
                    aoe = (AddressOfExpression) args.get(0);
     
                    Expression targetE = aoe.getTargetExpression();
                    String methodName = aoe.getMethodName();
                    return new DelegateCreationExpression(code, className, targetE, methodName, context);
                } else {
                    if (arrayInitializer != null) {
			return new ArrayCreationExpression(code, className, arrayInitializer, args, context);
                    } else {
			return new ObjectCreationExpression(code, className, args, VBKeywords.VB_New, context);
                    }
                }
	} 

	public List getConstructedPreStatements() {
		return this.constructedPreStatements;
	}

	protected String writeArgs(List args) {
		String s = "";
		for (Iterator itr = args.iterator(); itr.hasNext();) {
                    Object o = itr.next();
                    
			Expression e = (Expression) o;
			s = s + e.asJava();
			if (itr.hasNext()) {
				s = s + ",";
			}
		}
		return s;
	}

	

}
 