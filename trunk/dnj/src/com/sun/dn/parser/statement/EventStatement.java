
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
import com.sun.dn.*;
import com.sun.dn.util.*;

/** @author danny.coward@sun.com */	

public class EventStatement extends StatementAdapter {
        protected List modifiers = new ArrayList();
        protected DNVariable variable;
    
        public static boolean isCSEventStatement(String code, InterpretationContext context) {
            List l = Util.tokenizeIgnoringEnclosers(code, " ");
            if (l.contains(CSKeywords.CS_Event)) {
                return true;
            }
            return false;
        }
        
        public static String getVBEventDelegateTypeName(String en) {
            return "DN" + en;
        }
        
        public static boolean isVBEventStatement(String code, InterpretationContext context) {
            return Util.codeContains(code.trim(), VBKeywords.VB_Event);
        }
        
        public static EventStatement createVBEventStatement(String code, InterpretationContext context) {
            EventStatement es = new EventStatement(code, context);
            List l = Util.tokenizeIgnoringEnclosers(code, " ");
            for (Iterator itr = l.iterator(); itr.hasNext();) {
                String nextWord = (String) itr.next();
                if (isVBAttribute(nextWord)) {
                    VBComment comment = new VBComment("Translator: Event uses metadata: " + getAttributeString(nextWord), context);
                } else if (VBKeywords.getAccessKeywords().contains(nextWord)) {
                    es.modifiers.add(nextWord);
                } else if (VBKeywords.VB_Shadows.equals(nextWord)) {
                    // whatever..
                }
            }
            String rest = code.substring(code.indexOf(VBKeywords.VB_Event) + VBKeywords.VB_Event.length() + 1, code.length());
            
            String eventName = rest.substring(0, rest.indexOf("("));
            es.variable = DNVariable.createVBVariable(eventName, getVBEventDelegateTypeName(eventName));
            String argString = rest.substring(rest.indexOf("("), rest.length());
            String synthDelegateDecl = VBKeywords.VB_Sub + " " + getVBEventDelegateTypeName(eventName) + argString;
                    
            ParseTree pt = ParseTree.getParseTree(context);
            DelegateStatement ds = DelegateStatementImpl.createVBDelegateStatement(synthDelegateDecl, context);
            pt.addDelegate(ds);
           
            
                
            return es;
        }
        
        private EventStatement(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public DNVariable getVariable() {
            return this.variable;
        }
        
        public static EventStatement createCSEventStatement(String code, InterpretationContext context) {
            EventStatement es = new EventStatement(code, context);
            List l = Util.tokenizeIgnoringEnclosers(code, " ");
            for (Iterator itr = l.iterator(); itr.hasNext();) {
                String nextWord = (String) itr.next();
                if (isCSAttribute(nextWord)) {
                    CSComment comment = new CSComment("Translator: Event uses metadata: " + getAttributeString(nextWord), context);
                    es.addConstructedPreStatement(comment);
                } else if (CSKeywords.getMemberModifiers().contains(nextWord)) {
                    es.modifiers.add(nextWord);
                } else if (nextWord.equals(CSKeywords.CS_Event)) {
                    String oneAfter = ((String) itr.next()).trim();
                    String delegateTypeName = oneAfter;
                    String eventName = ((String) itr.next()).trim();
                    es.variable = DNVariable.createCSVariable(eventName, delegateTypeName);
                    break;
                }
            }
            if (code.indexOf("{") != -1) {
                String aDeclarations = Util.getInsideFirstCurlyBrackets(code);
                CSComment comment = new CSComment("// Couldn't translate " + aDeclarations, context);
                es.addConstructedPreStatement(comment);
                
            }
            
            return es;
        }
        
        public String getJavaVariableName() {
            return this.getVariable().getName() + "DNListeners";
        }

	protected List tryGetJava() {
		throw new RuntimeException("Subclass should implement this.");
	}

	
}

 