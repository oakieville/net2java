
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

    /** @author danny.coward@sun.com */    

public class UsingBlock extends StatementAdapter implements InterpretationContext {
        private Expression expression;
        private CodeBlock codeBlock;
       
	
	public static boolean isCSUsingBlock(String code, InterpretationContext context) {
            if ( code.trim().startsWith(CSKeywords.CS_Using)) {
                    String rest = code.trim().substring(CSKeywords.CS_Using.length(), code.trim().length());
                    if (rest.trim().startsWith("(")) {
                        return true;
                    }
                } 
                return false;
	}
        
        public static boolean isVBUsingBlock(String firstLine, InterpretationContext context) {
            StringTokenizer str = new StringTokenizer(firstLine);
            if (str.nextToken().equals(VBKeywords.VB_Using)) {
                return true;
            }
            return false;
        }
        
        public static String getVBUsingBlockCode(String firstLine, Iterator itr, InterpretationContext context) {
            String s = firstLine;
            while (itr.hasNext()) {
                String next = ((String) itr.next()).trim();
                if (isEndUsing(next)) {
                    return s;
                }
                s = s + "\n" + next;
            }
            return s;
        }
        
        private static boolean isEndUsing(String line) {
            StringTokenizer str = new StringTokenizer(line);
            if (str.hasMoreTokens()) {
                String first = str.nextToken();
                if (str.hasMoreTokens()) {
                    String second = str.nextToken();
                    if (first.equals(VBKeywords.VB_End) && second.equals(VBKeywords.VB_Using)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
	private UsingBlock(String code, InterpretationContext context) {
		super(code, context);
	}
        
        private void parseCS(String code, InterpretationContext context) {
            String rest = code.trim().substring(CSKeywords.CS_Using.length(), code.trim().length()).trim();
            String expressionS = rest.substring(0, rest.indexOf("{"));
            expressionS = Util.stripBrackets(expressionS.trim());
            this.expression = (new CSExpressionFactory()).getExpression(expressionS, this.context);
                
            String codeBlockS = rest.substring(rest.indexOf("{")+1, rest.length()-1).trim();
                
            this.codeBlock = new CodeBlock(this);
            List stmnts = CSMetaClass.tokenizeToClassStatements(codeBlockS);
            this.codeBlock.parseCS(stmnts);
        }
        
         private void parseVB(String code, InterpretationContext context) {
            List l = Util.tokenizeIgnoringEnclosers(code, "\n");
            String usingLine = ((String) l.get(0)).trim();
            String rest = usingLine.substring(VBKeywords.VB_Using.length(), usingLine.length()).trim();
            rest = Util.stripBrackets(rest);
            this.expression = (new VBExpressionFactory()).getExpression(rest, context);
            this.codeBlock = new CodeBlock(this);
            List blockStatements = new ArrayList();
            for (int i = 1; i < l.size(); i++) {
                blockStatements.add(l.get(i));
            }
            this.codeBlock.parseVB(blockStatements);
        }

	public static UsingBlock createCSUsingBlock(String code, InterpretationContext context) {
		UsingBlock ub = new UsingBlock(code, context);
                ub.parseCS(code, context);
                return ub;
	}
        
        public static UsingBlock createVBUsingBlock(String code, InterpretationContext context) {
            UsingBlock ub = new UsingBlock(code, context);
            ub.parseVB(code, context);
            return ub;
        }

	public List tryGetJava() {
		List l = new ArrayList();
                l.add("// a 'using' block: start block ");
		List codeBlockJava = this.codeBlock.getJava();
                l.addAll(codeBlockJava);
                l.add("// a 'using' block: end block ");
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

 