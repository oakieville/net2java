
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

import com.sun.dn.parser.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;
import com.sun.dn.java.*;
import com.sun.dn.*;
import java.util.*;
import com.sun.dn.Library;

        /** Jump statements give explicit direction as to where
         * next to evaluate statements in running code. For example, 
         * 'break' is a C# jump statement.
         * @author danny.coward@sun.com
         */

public class LockStatement extends StatementAdapter implements InterpretationContext {
        private Expression expression;
        private CodeBlock codeBlock;
       
	
	public static boolean isCSLockStatement(String code, InterpretationContext context) {
            if ( code.trim().startsWith(CSKeywords.CS_Lock)) {
                    String rest = code.trim().substring(CSKeywords.CS_Lock.length(), code.trim().length());
                    if (rest.trim().startsWith("(")) {
                        return true;
                    }
                } 
                return false;
	}
        
        public static boolean isVBLockStatement(String code, InterpretationContext context) {
            String ccode = code.trim();
            return ccode.startsWith(VBKeywords.VB_SyncLock + " ");
	}
        
        private LockStatement(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static LockStatement createVBLockStatement(String code, InterpretationContext context) {
                LockStatement ls = new LockStatement(code, context);
                List lines = Util.tokenizeIgnoringEnclosers(code, "\n");
                String firstLine = (String) lines.get(0);
                String expressionS = firstLine.substring(VBKeywords.VB_SyncLock.length(), firstLine.length());
                expressionS = Util.stripBrackets(expressionS);
                
                ls.expression = (new VBExpressionFactory()).getExpression(expressionS, context);
                List codeBlockLines = new ArrayList();
                for (int i = 1; i < lines.size(); i++) {
                    codeBlockLines.add(lines.get(i));
                }
                ls.codeBlock = new CodeBlock(ls);
                ls.codeBlock.parseVB(codeBlockLines); 
		return ls; 
	}
        
        public static String getVBLockStatementLoop(String firstLine, Iterator itr, InterpretationContext context) { 
            String code = firstLine;
            while( itr.hasNext()) {
                String nextLine = (String) itr.next();
                if (!nextLine.equals(VBKeywords.VB_End + " " + VBKeywords.VB_SyncLock)) {
                    code = code + "\n" + nextLine;
                } else {
                    return code;
                }
            }
            throw new RuntimeException("Cannot find end of SyncLock statement");
        } 

	

	public static LockStatement createCSLockStatement(String code, InterpretationContext context) {
                LockStatement ls = new LockStatement(code, context);
                String rest = code.trim().substring(CSKeywords.CS_Lock.length(), code.trim().length()).trim();
                String expressionS = rest.substring(0, rest.indexOf("{"));
                expressionS = Util.stripBrackets(expressionS.trim());
                ls.expression = (new CSExpressionFactory()).getExpression(expressionS, ls.context);
                
                String codeBlockS = rest.substring(rest.indexOf("{")+1, rest.length()-1).trim();
                
                
                ls.codeBlock = new CodeBlock(ls);
                List stmnts = CSMetaClass.tokenizeToClassStatements(codeBlockS);
                ls.codeBlock.parseCS(stmnts);
		return ls;
	}

	public List tryGetJava() {
		List l = new ArrayList();
                l.add(JavaKeywords.J_SYNCHRONIZED + " (" + expression.asJava() + ") {"); 
		List codeBlockJava = this.codeBlock.getJava();
                for (Iterator itr = codeBlockJava.iterator(); itr.hasNext();) {
                    String nextJava = (String) itr.next();
                    l.add("\t" + nextJava);
                }
                l.add("}");
                //l.add(expression.asJava() + ".finalise();");
		return l;
	}
        
        public List getVariables() {
            List l = this.context.getVariables();
            // should add any variables declared in the using () part
            return l;
            
        }
	public DNVariable getVariable(String name) {
            for (Iterator itr = this.getVariables().iterator(); itr.hasNext();) {
                DNVariable var = (DNVariable) itr.next();
                if (var.getName().equals(name)) {
                    return var;
                }
            }
            return null;
        }
        
	public Library getLibrary() {
            return this.context.getLibrary();
        }
        
	public InterpretationContext getParent() {
            return this.context;
        }
	public MetaClass getMetaClass() {
            return this.context.getMetaClass();
        }
	
	


}

 