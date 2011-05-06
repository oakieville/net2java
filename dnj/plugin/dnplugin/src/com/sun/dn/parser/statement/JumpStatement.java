
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
import com.sun.dn.util.*;
import com.sun.dn.java.*;
import java.util.*;

        /** Jump statements give explicit direction as to where
         * next to evaluate statements in running code. For example, 
         * 'break' is a C# jump statement.
         * @author danny.coward@sun.com
         */

public class JumpStatement extends StatementAdapter {
        private String jkw;
        private String label;
	
	public static boolean isCSJumpStatement(String code, InterpretationContext context) {
                
		return !ReturnStatement.isCSReturnStatement(code, context) &&
                        startsWithCSJumpStatement(code);
	}
        
        public static boolean isVBJumpStatement(String code, InterpretationContext context) {
		return code.startsWith(VBKeywords.VB_GoTo + " ") ||
                        code.startsWith(VBKeywords.VB_Resume) ||
                        code.startsWith(VBKeywords.VB_Continue);
	}

	 static boolean startsWithCSJumpStatement(String statement) {
		return getCSJumpKeyword(statement) != null;
	}
         
        static String getCSJumpKeyword(String code) {
            List js = CSKeywords.getJumpKeywords();
		for (Iterator itr = js.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (code.startsWith(next)) {
                                if (next.startsWith(CSKeywords.CS_Goto)) {
                                    
                                    if (code.startsWith(CSKeywords.CS_Goto + " ")) {
                                        
                                        return next;
                                    } else {
                                        return null;
                                    }
                                } else {
                                    return next;
                                }
			}
		}
		return null;
        }
        
        private JumpStatement(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static JumpStatement createVBJumpStatement(String code, InterpretationContext context) {
                JumpStatement js = new JumpStatement(code, context);
                if (code.startsWith(VBKeywords.VB_GoTo)) {
                    js.jkw = VBKeywords.VB_GoTo; 
                    js.label = code.trim().substring(VBKeywords.VB_GoTo.length() + 1, code.trim().length()).trim();
                } else if (code.startsWith(VBKeywords.VB_Resume)) {
                    js.jkw = VBKeywords.VB_Resume; 
                    js.label = VBKeywords.VB_Resume; 
                } else if (code.startsWith(VBKeywords.VB_Continue)) {
                    js.jkw = VBKeywords.VB_Continue; 
                    js.label = code.trim().substring(VBKeywords.VB_Continue.length(), code.trim().length()).trim();
                }
                TranslationWarning w = new TranslationWarning(code, "Translator cannot translate this jump statement .NET [ " + code + " ]");
                ParseTree.getParseTree(js.context).getTranslationReport().addTranslationWarning(w);
                ParseTree.getParseTree(js.context).addLineLabel(js.label); 
		return js; 
	}

	public static JumpStatement createCSJumpStatement(String code, InterpretationContext context) {
                JumpStatement js = new JumpStatement(code, context);
                js.jkw = getCSJumpKeyword(code); 
                if (js.jkw.equals(CSKeywords.CS_Goto)) { 
                    js.label = code.trim().substring(CSKeywords.CS_Goto.length() + 1, code.trim().length());
                    TranslationWarning w = new TranslationWarning(code, "Translator cannot translate goto statements.");
                    ParseTree.getParseTree(js.context).getTranslationReport().addTranslationWarning(w);
                    ParseTree.getParseTree(js.context).addLineLabel(js.label); 
                }
		return js;
	}
        
        private boolean isAllowedBreakContinue() {
            ControlFlowStatement cfs = this.getControlFlowStatement();
            if (cfs != null) {
                if ( cfs instanceof SelectStatement) {
                    return false;
                }
            }
            return true;
        }
        
        private ControlFlowStatement getControlFlowStatement() {
            boolean keepGoing = true;
            InterpretationContext ic = this.context;
            while(keepGoing) {
                if (ic instanceof ControlFlowStatement) {
                    return (ControlFlowStatement) ic;
                } else {
                    ic = ic.getParent();
                }
                if (ic == null) {
                    return null;
                }
            }
            return null;
        }

	public List tryGetJava() {
		List l = new ArrayList();
		
                if (super.getOriginalCode().equals(CSKeywords.CS_Break)) {
                    if (this.isAllowedBreakContinue()) {
                        l.add(JavaKeywords.J_BREAK + ";");
                    } else {
                        l.add("//" + JavaKeywords.J_BREAK + ";");
                    }
                } else if (super.getOriginalCode().equals(CSKeywords.CS_Continue)) {
                    if (this.isAllowedBreakContinue()) {
                        l.add(JavaKeywords.J_CONTINUE + ";");
                    } else {
                        l.add("//" + JavaKeywords.J_CONTINUE + ";");
                    }
                } else if (this.jkw.equals(CSKeywords.CS_Goto)) {
                    l.add("// Translator: C# program directs flow to the line label: " + label);
                } else if (this.jkw.equals(VBKeywords.VB_GoTo)) {
                    l.add("// Translator: VB program directs flow to the line label: " + label);
                } else if (this.jkw.equals(VBKeywords.VB_Resume)) {
                    l.add("// Translator: VB program directs flow to the line that caused the error.");
                } else if (this.jkw.equals(VBKeywords.VB_Continue)) {
                    if (label == null) {
                        l.add(JavaKeywords.J_CONTINUE);
                        
                    } else {
                        l.add("// Translator: VB program directs a Continue to the " + label + " loop.");
                    }
                } else {
                    l.add("//Jump statement (" + super.getOriginalCode() + ")");
                }
		return l;
	}
	
	


}

 