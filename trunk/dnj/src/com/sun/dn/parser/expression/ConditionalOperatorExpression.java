
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
import com.sun.dn.util.*;

	/** An expression composite of the form 'cond-expr ? expr1 : expr2' (C# only).
         * @author danny.coward@sun.com
         */	

public class ConditionalOperatorExpression extends ExpressionAdapter  {
	Expression conditionExpression;
	Expression trueExpression;
	Expression falseExpression;

        private ConditionalOperatorExpression(String code, InterpretationContext context) {
            super(code, context);
        }
        
	public static boolean isCSConditionalOperatorExpression(String code, InterpretationContext context) {
		return Util.tokenizeIgnoringEnclosers(code, "?").size() > 1;
	}

	public static ConditionalOperatorExpression createCSConditionalOperatorExpression(String code, InterpretationContext context) {
		ConditionalOperatorExpression coe = new ConditionalOperatorExpression(code, context);
		List sides = Util.tokenizeIgnoringEnclosers(code, "?");
		String conditionString = (String) sides.get(0);
		conditionString = Util.stripBrackets(conditionString);
		String resultString = (String) sides.get(1);
		List trueFalseStrings =  Util.tokenizeIgnoringEnclosers(resultString, ":");
		String trueString = (String) trueFalseStrings.get(0);
		String falseString = (String) trueFalseStrings.get(1);
		ExpressionFactory factory = new CSExpressionFactory();
		coe.conditionExpression = factory.getExpression(conditionString, context);
		coe.trueExpression = factory.getExpression(trueString, context);
		coe.falseExpression = factory.getExpression(falseString, context);

		return coe;
	}

	public DNType getDNType() {
		return trueExpression.getDNType();
	}

	public String getTypeName() {
		return trueExpression.getTypeName();
	}

	public String tryAsJava() {
            
		return conditionExpression.asJava() + " ? " + trueExpression.asJava() + " : " + falseExpression.asJava();
	}

}
 