
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
import com.sun.dn.java.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/** A .NET statement within a member of a metaclass contains the
	** return value expression.
	@author danny.coward@sun.com
	*/


public class ReturnStatement extends StatementAdapter  {
	Expression valueExpression ; 

	public static boolean isVBReturnStatement(String code, InterpretationContext context) {
		return Util.codeContains(code, VBKeywords.VB_Return);
	}
        
        public static boolean isCSReturnStatement(String code, InterpretationContext context) {
                return code.trim().startsWith(CSKeywords.CS_Return)
                    && (Util.tokenizeIgnoringEnclosers(code.trim(), " ").size() > 0);
        }

	private ReturnStatement(String code, InterpretationContext context) {
		super(code, context);
	}
        
        public static ReturnStatement createVBReturnStatement(String code, InterpretationContext context) {
           ReturnStatement rs = new ReturnStatement(code, context);
           rs.parseVB(code);
           return rs;
        }
        
        public static ReturnStatement createCSReturnStatement(String code, InterpretationContext context) {
           ReturnStatement rs = new ReturnStatement(code, context);
           rs.parseCS(code);
           return rs;
        }
        
        public static ReturnStatement createVBReturnStatement(InterpretationContext context, DNVariable variable) {
            ReturnStatement rs = new ReturnStatement("<Created by parser because of the context>", context);
            rs.valueExpression = LocalVariableExpression.createVBLocalVariableExpression(variable, context);
            return rs;
        }
        
	

	private void parseVB(String code) {
		String codeToParse = code.trim();
		StringTokenizer st = new StringTokenizer(codeToParse);
		String keyword = st.nextToken();// that was the keyword
		String value = codeToParse.substring(keyword.length(), codeToParse.length());
		if (!value.trim().equals("")) {
			valueExpression = (new VBExpressionFactory()).getExpression(value, this.context);
		}
	}
        
        private void parseCS(String code) {
		String codeToParse = code.trim();
		StringTokenizer st = new StringTokenizer(codeToParse);
		String keyword = st.nextToken();// that was the keyword
		String value = codeToParse.substring(keyword.length(), codeToParse.length());
		if (!value.trim().equals("")) {
			valueExpression = (new CSExpressionFactory()).getExpression(value, this.context);
		}
	}

	private String asJava() {
		if (valueExpression != null) {
			return JavaKeywords.J_RETURN + " " + valueExpression.asJava() + ";";	
		} else {
			return JavaKeywords.J_RETURN + ";";
		}
	}

	protected List tryGetJava() {
		List java = new ArrayList();
		java.add(this.asJava());
		return java;
	}



	public String toString() {
		return "ReturnStatement: " + valueExpression;
	}
}

 