
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
import com.sun.dn.parser.*;
import com.sun.dn.util.*;
import com.sun.dn.parser.expression.*;

	/** A VB Erase statement.
	@author danny.coward@sun.com
	**/

public class EraseStatement extends StatementAdapter {
        private List expressions = new ArrayList();
        private static String COUNTER_NAME = "counter";
	
	public static boolean isVBEraseStatement(String code, InterpretationContext context) {
		return code.trim().startsWith(VBKeywords.VB_Erase + " ");
	}

	public EraseStatement (String code, InterpretationContext context) {
                super(code, context);
                String eList = code.substring(VBKeywords.VB_Erase.length(), code.length());
                List eItems = Util.tokenizeIgnoringEnclosers(eList, ",");
                for (Iterator itr = eItems.iterator(); itr.hasNext();) {
                    String next = ((String) itr.next()).trim();
                    VBExpressionFactory factory = new VBExpressionFactory();
                    Expression e = factory.getExpression(next, context);
                    if (!(e instanceof LocalVariableExpression)) {
                        throw new RuntimeException("Aaagh !");
                    }
                    expressions.add(e);
                }
              
	}

        protected List tryGetJava() {
		List l = new ArrayList();
                for (Iterator itr = this.expressions.iterator(); itr.hasNext();) {
                    LocalVariableExpression lve = (LocalVariableExpression) itr.next();
                    l.addAll(this.getJavaFor(lve));
                }
                //l.add("// Program defined BreakPoint");
                return l;
	}
        
        private List getJavaFor(LocalVariableExpression e) {
            List l = new ArrayList();
            String s = "for (int " + COUNTER_NAME + " = 0; " + COUNTER_NAME + " < " + e.asJava() + ".length; " + COUNTER_NAME + "++) {";
            l.add(s);
            s = "\t" + e.asJava() + "[" + COUNTER_NAME + "] = null;";
            l.add(s);
            l.add("}");
            return l;
        }
}

 