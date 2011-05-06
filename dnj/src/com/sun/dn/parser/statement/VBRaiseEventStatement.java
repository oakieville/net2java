
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
import com.sun.dn.parser.*;
import com.sun.dn.java.DelegateHelper;
import com.sun.dn.*;
import com.sun.dn.util.*;

	/** Abstract implementation class for .NET statemnts. 
         *  This could be, for example,
	**  a class declaration,
	** or a return statement.
	** @author danny.coward@sun.com
	*/

public class VBRaiseEventStatement extends StatementAdapter { 
        private String eventType;
        private List eventArgs = new ArrayList();

	protected List tryGetJava() {
            List l = new ArrayList();
            String s = "";

            DelegateStatement ds = this.getDelegateStatement();
            
            EventStatement es = this.context.getMetaClass().getEventStatementFor(eventType);
           
            l.add("for (Iterator dnItr = " + es.getJavaVariableName() + ".iterator(); dnItr.hasNext();) {\n");
            l.add("\t" + ds.getDNType().getName() + " dnDelegate = (" + ds.getDNType().getName() + ") dnItr.next();\n");
            l.add("\tdnDelegate." + DelegateHelper.getInvokeMethodNameFor() + "(" + this.getArgsAsJava() +");");
            l.add("}");
            l.add(s);
            return l;
	}
        
        private String getArgsAsJava() {
            String s = "";
            for (Iterator itr = this.eventArgs.iterator(); itr.hasNext();) {
                Expression nextE = (Expression) itr.next();
                s = s + nextE.asJava();
                if (itr.hasNext()) {
                    s = s + ",";
                }
            }
            return s;
        }
        
        private DelegateStatement getDelegateStatement() {
            String delegateName = EventStatement.getVBEventDelegateTypeName(eventType);
            return ParseTree.getParseTree(context).getDelegateFor(delegateName);
        }
        private VBRaiseEventStatement(String code, InterpretationContext context) {
            super(code, context);
        }

	public static boolean isVBRaiseEventStatement(String code, InterpretationContext context) {
            return code.trim().startsWith(VBKeywords.VB_RaiseEvent);
        }
        
        public static VBRaiseEventStatement createVBRaiseEventStatement(String code, InterpretationContext context) {
            VBRaiseEventStatement res = new VBRaiseEventStatement(code, context); 
            String rest = code.trim().substring(VBKeywords.VB_RaiseEvent.length() + 1, code.trim().length());
            res.eventType = rest.substring(0, rest.indexOf("("));
            String argString = rest.substring(rest.indexOf("("), rest.length());
            List l = Util.tokenizeIgnoringEnclosers(Util.stripBrackets(argString), ",");
            for (Iterator itr = l.iterator(); itr.hasNext();) {
                String next = ((String) itr.next()).trim();
                Expression e = (new VBExpressionFactory()).getExpression(next, context);
                res.eventArgs.add(e);
            }
            return res;
        }
}

 