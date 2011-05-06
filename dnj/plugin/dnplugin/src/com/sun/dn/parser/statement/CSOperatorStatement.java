
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

	/** Class representing a C# Method member.
	** @author danny.coward@sun.com
	*/


public class CSOperatorStatement extends CSMethodStatement implements OperatorStatement {
	

	public CSOperatorStatement(String code, boolean isInInterface, InterpretationContext context, List bodyStatements) {
		super(code, isInInterface, context, bodyStatements);
        }

	public static boolean isOperatorStatement(String code, InterpretationContext context) {
		if (code.indexOf("(") != -1) {
                    String decl = code.substring(0, code.indexOf("("));
                    List l = Util.tokenizeIgnoringEnclosers(decl, " ");
                    return l.contains(CSKeywords.CS_Operator);
                } 
                return false;
	}
        
        public static String getJavaNameFor(String n) {
            if (CSOperators.getUnaryOperators().contains(n)) {
                return  CSOperators.translateUnaryOperator(n);
            } else if (CSOperators.getBinaryOperators().contains(n)) {
                return CSOperators.translateBinaryOperator(n);
            } 
            return "__" + n;
        }
        
        public boolean isBinary() {
            return this.sig.getArgs().size() == 2;
        }
        
        public boolean matchesBinaryOperatorExpression(BinaryOperatorExpression boe) {
            return OperatorStatementHelper.matchesBinaryOperatorExpression(this, boe, this.context);
            
        }
        
        public boolean isUnary() {
            return this.sig.getArgs().size() == 1;
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


	public void parse(List stmts) {

	}


}

 