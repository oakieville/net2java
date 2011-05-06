
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
import com.sun.dn.java.*;


public class DelegateInvocation extends StatementAdapter {
        DNVariable variable;
        boolean isInvokingEvents = false;
        List args = new ArrayList();
        
        public static boolean isCSDelegateInvocation(String code, InterpretationContext context) {
           return isDelegateInvocation(code, context);
        }
        
        private static boolean isDelegateInvocation(String code, InterpretationContext context) {
            if (code.indexOf("(") != -1) {
               String name = code.trim().substring(0, code.indexOf("("));
               if (context.getVariable(name) != null) {
                   String type = context.getVariable(name).getType();
                   DNType dnT = context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(type);
                   if (dnT.isDelegate()) {
                    return true;
                   } else {
                       throw new RuntimeException("I thought this was a delegate invocation, but the type is not a delegate");
                   }
               }
           }
           return false;
        }
        
        public static boolean isVBDelegateInvocation(String code, InterpretationContext context) {
           return isDelegateInvocation(code, context);
        }
        
        protected DelegateInvocation(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static DelegateInvocation createCSDelegateInvocation(String code, InterpretationContext context) {
            return createDelegateInvocation(new CSExpressionFactory(), code, context);
        }
        
        public static DelegateInvocation createVBDelegateInvocation(String code, InterpretationContext context) {
            return createDelegateInvocation(new VBExpressionFactory(), code, context);
        }
        
        private static DelegateInvocation createDelegateInvocation(ExpressionFactory factory, String code, InterpretationContext context) {
            DelegateInvocation di = new DelegateInvocation(code, context);
            Debug.clogn("Parse " + code, DelegateInvocation.class);
            String name = code.trim().substring(0, code.indexOf("("));
            String rest = code.trim().substring(code.indexOf("("), code.trim().length());
            String argsS = Util.stripBrackets(rest);
            Debug.clogn("Name " + name, DelegateInvocation.class);
            Debug.clogn("Args " + argsS, DelegateInvocation.class);
            di.variable = context.getVariable(name);
            if (context.getMetaClass().isEventName(di.variable.getName())) { 
                di.isInvokingEvents = true;
            }
            List argList = Util.tokenizeIgnoringEnclosers(argsS, ",");
            for (Iterator itr = argList.iterator(); itr.hasNext();) {
                String nextArg = ((String) itr.next()).trim();
                Expression nextArgE = factory.getExpression(nextArg, context);
                di.args.add(nextArgE); 
            }
            return di;
        }
        
        private List getJavaForDelegate() {
            List j = new ArrayList();
            DelegateStatement ds = this.getDelegateStatement(); 
            String methodName = DelegateHelper.getInvokeMethodNameFor();
            String s = variable.getName() + "." + methodName + "(";
            s = s + getJavaArgString();
            s = s + ");";
            j.add(s);
            return j;
        }
        
        private String getJavaArgString() {
            String s = "";
            for (Iterator itr = this.args.iterator(); itr.hasNext();) {
                Expression e = (Expression) itr.next();
                s = s + e.asJava();
                if (itr.hasNext()) {
                    s = s + ",";
                }
            }
            return s;
        }
        
        private DelegateStatement getDelegateStatement() {
            ParseTree pt = ParseTree.getParseTree(context);
            DelegateStatement ds = (DelegateStatement) pt.getDelegateFor(this.variable.getType());
            return ds;
        }
        
        private List getJavaForEventInvocations() {
            List l = new ArrayList();
            DelegateStatement ds = this.getDelegateStatement();
            EventStatement es = this.context.getMetaClass().getEventStatementFor(variable.getName());
            
            l.add("for (Iterator dnItr = " + es.getJavaVariableName() + ".iterator(); dnItr.hasNext();) {\n");
            l.add("\t" + ds.getDNType().getName() + " dnDelegate = (" + ds.getDNType().getName() + ") dnItr.next();\n");
            l.add("\tdnDelegate." + DelegateHelper.getInvokeMethodNameFor() + "(" + this.getJavaArgString() +");");
            l.add("}");
            return l;
        }
        
        public List tryGetJava() {
            if (this.isInvokingEvents) {
                return getJavaForEventInvocations();
            } else {
               return getJavaForDelegate(); 
            }
        } 
        
        
        
        
        

	

	

}

 