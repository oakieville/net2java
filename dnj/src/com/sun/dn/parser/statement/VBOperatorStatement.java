
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
/*
 * VBOperatorStatement.java
 *
 * Created on March 31, 2006, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.dn.parser.statement;

import com.sun.dn.parser.*;
import java.util.*;
import com.sun.dn.util.*;
import com.sun.dn.parser.expression.*;

/**
 *
 * @author danny.coward@sun.com
 */
public class VBOperatorStatement extends VBFunctionStatement implements OperatorStatement {
	

	public VBOperatorStatement(String code, InterpretationContext context) {
		super(code, context);
        }

	public static boolean isOperatorStatement(String code, InterpretationContext context) {
            Debug.clogn("Is " + code + " an operator expression ? ", VBOperatorStatement.class);
		if (code.indexOf("(") != -1) {
                    String decl = code.substring(0, code.indexOf("("));
                    List l = Util.tokenizeIgnoringEnclosers(decl, " ");
                    boolean b =  l.contains(VBKeywords.VB_Operator);
                    if (b) {
                        Debug.clogn("YES ", VBOperatorStatement.class);
                    } else {
                         Debug.clogn("NO ", VBOperatorStatement.class);
                    }
                    return b;
                } 
                Debug.clogn("NO ", VBOperatorStatement.class);
                return false;
	}
        
        public boolean matchesBinaryOperatorExpression(BinaryOperatorExpression boe) {
            return OperatorStatementHelper.matchesBinaryOperatorExpression(this, boe, this.context);
            
        }
        
        public static String getJavaNameFor(String n) {
            if (VBOperators.getBinaryOperators().contains(n)) {
                return VBOperators.getReadableNameForBinaryOperator(n);
            } 
            return "__" + n;
        }
        
        public boolean isBinary() {
            return this.sig.getArgs().size() == 2;
        }
        
       
        
        public boolean isUnary() {
            return false;
        }
           
        protected boolean isEndFunction(String s) {
            return s.trim().startsWith(VBKeywords.VB_End + " " + VBKeywords.VB_Operator);
        }
        
        public String getJavaName() {
            return getJavaNameFor(this.sig.getName());
        }

	public boolean isConstructor() {
		return false;
	}
        
        public String toString() {
            return "CustomOperator with " + this.getName();
        }

    
}

 