
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


package com.sun.dn.java;

import com.sun.dn.Library;
import com.sun.dn.library.JavaExpression;
import com.sun.dn.parser.DNType;
import com.sun.dn.parser.DNVariable;
import com.sun.dn.parser.Expression;
import com.sun.dn.parser.expression.InvocationExpression;
import com.sun.dn.parser.expression.MemberAccessExpression;
import com.sun.dn.parser.expression.ObjectCreationExpression;
import com.sun.dn.parser.statement.Assignment;
import com.sun.dn.util.Debug;
import com.sun.dn.util.Util;
import java.util.List;


public class JavaExpressionMatcher {
    private JavaExpression je;
    private Library library;
    
    /** Creates a new instance of JavaExpressionMatcher */
    public JavaExpressionMatcher(JavaExpression je, Library library) {
        this.je = je;
        this.library = library;
    }
    
    	public String getJavaForAssignment(Assignment as, boolean ensureUpcast) {
            //Expression e = as.getAssignee();
            Expression parent = as.getMemberAccessExpression().getExpression();
            String valueString = as.getJavaForValue(ensureUpcast);
            String valueToken = "${"+JavaExpression.VALUE+"}";
            String getJava = Util.replaceString(je.getExpression(), valueToken, valueString);
            String thisToken = "${"+JavaExpression.THIS+"}";
            getJava = Util.replaceString(getJava, thisToken, parent.asJava());
            return getJava;
	}

	public String getJavaForNew(ObjectCreationExpression oce) {
            Debug.logn("Get Java for template " + je.getExpression(), this);
            Debug.logn("for " + oce, this);
            List args = oce.getArgs();
            String javaForArgs = this.getJavaForArgs(args, null);
            return javaForArgs;
	}

	public String getJavaForMAE(MemberAccessExpression mae) {
            String thisToken = "${"+JavaExpression.THIS+"}";
            String getJava = Util.replaceString(je.getExpression(), thisToken, mae.getExpression().asJava());
            return getJava;
	}

	public String getJava(InvocationExpression ie) {
            //System.out.println("Get Java for template " + this.ex + " for method " + ie.getMethodName());
            Debug.logn("for " + ie, this);
            List args = ie.getArgs();
            Expression target = null;
            if (je.isIndirect()) {
            	MemberAccessExpression mae = (MemberAccessExpression) ie.getTarget();
            	target = mae.getExpression();
            } else {
		target = ie.getTarget();
            }
            return this.getJavaForArgs(args, target);
	}

	public String getJavaForIndirect(InvocationExpression ie) {
            Debug.logn("for " + ie, this);
            List args = ie.getArgs();
            Expression target = ie.getTarget();
            return this.getJavaForArgs(args, target);
	}

		// TRUE if the sig arg is a Object or other superclass of the VB types
		// corresponding to one of the Java primitive types
		// AND the type of the active arg is primitive.
		// this covers the case of passing a primitive into
		// a method with an objeect argument.
	private boolean needsCoercion(DNVariable sigArg) {
            return this.needsCoercionType(sigArg.getType());
	}

	private boolean needsCoercionType(String type) {
            if (je.getSignature() == null) {
		return false; // rules out the property expressions
            } else if (DNType.shortNameEquivalent("System.Object", type)) {
		return true; // should also cover arrays
            } else {
		return false;
            }
	}

	private String getJavaForArgs(List args, Expression target) {
            String getJava = je.getExpression();
            boolean hasMoreArgs = true;
            for (int i = 0; i < args.size(); i++) {
            	String argsString = "${" + JavaExpression.ARG + i + "}";
            	if (je.getExpression().indexOf(argsString) != -1) {
                    //System.out.println("here " + ex);
                    Object o = args.get(i);
                    boolean coerce = this.needsCoercion((DNVariable) je.getSignature().getArgs().get(i));
                    if (o instanceof DNVariable) {
			DNVariable var = (DNVariable) o;
			String javaArgExp = null;
			if (coerce) {
                            javaArgExp = JavaPrimitives.primitiveToClass(var.getName(), library.getJavaTypeFor(var.getType()));
			} else {
                            javaArgExp = var.getName();
			}
					
			getJava = Util.replaceString(getJava, argsString, javaArgExp);
                    } else if (o instanceof Expression) {
			Expression e = (Expression) o;
			String javaArgExp = null;
			if (coerce) {
                            javaArgExp = JavaPrimitives.primitiveToClass(e.asJava(), library.getJavaTypeFor(e.getTypeName()));
			} else {
                            javaArgExp = e.asJava();
			}
			getJava = Util.replaceString(getJava, argsString, javaArgExp );
                    } else {
			throw new RuntimeException("shouldn't be here");
                    }
		} 
            }
            if (target != null) {
		String targetJava = "";
		// always coerce these up if possible
                if (getJava.indexOf ("${"+JavaExpression.THIS+"}") != -1) {
                    targetJava = JavaPrimitives.primitiveToClass(target.asJava(), library.getJavaTypeFor(target.getTypeName()));
                    getJava = Util.replaceString(getJava, "${"+JavaExpression.THIS+"}", targetJava );
                }
            }
            Debug.logn("Result " + getJava, this);
            Debug.logn("NEED IMPORT " + je.getImportStrings(), this);
            return getJava;
	}
    
}

 